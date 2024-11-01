package ru.practicum.main.service.event.service;

import jakarta.persistence.EntityManager;
import jakarta.persistence.TypedQuery;
import jakarta.servlet.http.HttpServletRequest;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.practicum.main.service.category.model.Category;
import ru.practicum.main.service.category.repository.CategoryRepository;
import ru.practicum.main.service.event.dto.*;
import ru.practicum.main.service.event.mapper.EventMapper;
import ru.practicum.main.service.event.model.Event;
import ru.practicum.main.service.event.model.EventSort;
import ru.practicum.main.service.event.model.EventState;
import ru.practicum.main.service.event.model.StateAction;
import ru.practicum.main.service.event.repository.EventRepository;
import ru.practicum.main.service.exception.EWMBadRequestException;
import ru.practicum.main.service.exception.EWMConflictException;
import ru.practicum.main.service.exception.EWMNotFoundException;
import ru.practicum.main.service.request.dto.RequestDto;
import ru.practicum.main.service.request.dto.RequestUpdateDto;
import ru.practicum.main.service.request.mapper.RequestMapper;
import ru.practicum.main.service.request.model.Request;
import ru.practicum.main.service.request.model.RequestStatus;
import ru.practicum.main.service.request.repository.RequestRepository;
import ru.practicum.main.service.stat.service.StatsService;
import ru.practicum.main.service.user.model.User;
import ru.practicum.main.service.user.repository.UserRepository;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.*;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Slf4j
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class EventServiceImpl implements EventService {

    EventRepository eventRepository;
    UserRepository userRepository;
    CategoryRepository categoryRepository;
    RequestRepository requestRepository;
    StatsService statsService;
    EntityManager entityManager;
    DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");

    @Override
    @Transactional
    public EventResponseLongDto createEventByPrivate(final Long userId, final EventRequestDto eventRequestDto) {
        log.info("Добавление события/");
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new EWMNotFoundException("Такого пользователя id " + userId + " нет."));
        Category category = categoryRepository.findById(eventRequestDto.getCategory())
                .orElseThrow(() -> new EWMBadRequestException("Такой категории нет."));
        if (eventRequestDto.getEventDate().isBefore(LocalDateTime.now().plusHours(2))) {
            throw new EWMBadRequestException("Событие не может начинаться ранее, чем за 2 часа до создания.");
        }
        Event event = eventRepository.save(EventMapper.toEvent(eventRequestDto, user, category));
        log.info("Событие добавлено id {} со статусом {}.", user.getId(), event.getState());
        return EventMapper.toEventResponseLongDto(event);
    }

    @Override
    @Transactional
    public EventResponseLongDto approveRequestsByAdministrator(final Long eventId,
                                                               final EventUpdateDto eventUpdateDto) {
        log.info("Публикация администратором события id = {}.", eventId);
        Event oldEvent = eventRepository.findById(eventId)
                .orElseThrow(() -> new EWMNotFoundException("Такого события id " + eventId + " нет."));
        validateEventUpdate(oldEvent, eventUpdateDto);
        updateEventFields(oldEvent, eventUpdateDto);
        Event event = eventRepository.save(oldEvent);
        log.info("Событие успешно обновлено администратором.");
        return EventMapper.toEventResponseLongDto(event);
    }

    private void validateEventUpdate(Event oldEvent, EventUpdateDto dto) {
        if ((dto.getEventDate() != null && dto.getEventDate().isBefore(LocalDateTime.now().plusHours(1))) ||
                oldEvent.getEventDate().isBefore(LocalDateTime.now().plusHours(1))) {
            throw new EWMConflictException("Событие не может начинаться ранее, чем через 1 час после редактирования администратором.");
        }
        if (oldEvent.getPublishedOn() != null && LocalDateTime.now().plusHours(1).isBefore(oldEvent.getPublishedOn())) {
            throw new EWMConflictException("Дата начала изменяемого события должна быть не ранее, чем за час от даты публикации.");
        }
        if (oldEvent.getState().equals(EventState.PUBLISHED) || oldEvent.getState().equals(EventState.CANCELED)) {
            throw new EWMConflictException("Администратор не может менять статус опубликованного или отмененного события.");
        }
    }

    private void updateEventFields(Event oldEvent, EventUpdateDto dto) {
        if (dto.getAnnotation() != null) oldEvent.setAnnotation(dto.getAnnotation());
        if (dto.getCategory() != null) {
            Category category = categoryRepository.findById(dto.getCategory())
                    .orElseThrow(() -> new EWMBadRequestException("Категория указана неверно."));
            oldEvent.setCategory(category);
        }
        if (dto.getDescription() != null) oldEvent.setDescription(dto.getDescription());
        if (dto.getEventDate() != null) oldEvent.setEventDate(dto.getEventDate());
        if (dto.getLocation() != null) {
            oldEvent.setLon(dto.getLocation().lon());
            oldEvent.setLat(dto.getLocation().lat());
        }
        if (dto.getPaid() != null) oldEvent.setPaid(dto.getPaid());
        if (dto.getParticipantLimit() != null) oldEvent.setParticipantLimit(dto.getParticipantLimit());
        if (dto.getRequestModeration() != null) oldEvent.setRequestModeration(dto.getRequestModeration());
        if (dto.getStateAction() != null && oldEvent.getState().equals(EventState.PENDING)) {
            if (dto.getStateAction().equals(StateAction.PUBLISH_EVENT)) {
                oldEvent.setState(EventState.PUBLISHED);
                oldEvent.setPublishedOn(LocalDateTime.now());
            } else if (dto.getStateAction().equals(StateAction.REJECT_EVENT)) {
                oldEvent.setState(EventState.CANCELED);
                oldEvent.setPublishedOn(null);
            }
        }
        if (dto.getTitle() != null) oldEvent.setTitle(dto.getTitle());
    }

    @Override
    @Transactional
    public Map<String, List<RequestDto>> approveRequestsByPrivate(final Long userId,
                                                                  final Long eventId,
                                                                  final RequestUpdateDto requestUpdateDto) {
        log.info("Изменение статуса переданных заявок.");
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new EWMNotFoundException("Пользователя с id = " + userId + " нет."));
        Event event = eventRepository.findById(eventId)
                .orElseThrow(() -> new EWMNotFoundException("События с id = " + eventId + " нет."));
        if (!event.getInitiator().equals(user)) {
            throw new EWMConflictException("У этого события другой инициатор.");
        }
        List<Request> requests = requestRepository.findRequestByIdIn(requestUpdateDto.getRequestIds());
        validateRequests(event, requests, requestUpdateDto);
        Map<String, List<RequestDto>> requestMap = new HashMap<>();
        if (requestUpdateDto.getStatus().equals(RequestStatus.REJECTED)) {
            requestMap.put("rejectedRequests", rejectRequests(requests));
        } else {
            requestMap = confirmRequests(event, requests, requestUpdateDto);
        }
        return requestMap;
    }

    private void validateRequests(Event event, List<Request> requests, RequestUpdateDto dto) {
        if (event.getRequestModeration() &&
                event.getParticipantLimit().equals(event.getConfirmedRequests()) &&
                event.getParticipantLimit() != 0 &&
                dto.getStatus().equals(RequestStatus.CONFIRMED)) {
            throw new EWMConflictException("Лимит заявок на участие в событии исчерпан.");
        }
        if (!requests.stream().allMatch(request -> request.getEvent().getId().longValue() == event.getId())) {
            throw new EWMConflictException("Неверно передан список запросов. Они должны относиться к одному событию.");
        }
        if (dto.getStatus().equals(RequestStatus.REJECTED) &&
                requests.stream().anyMatch(request -> request.getStatus().equals(RequestStatus.CONFIRMED))) {
            throw new EWMConflictException("Нельзя отменить уже подтвержденные заявки.");
        }
    }

    private List<RequestDto> rejectRequests(List<Request> requests) {
        return requests.stream()
                .peek(request -> request.setStatus(RequestStatus.REJECTED))
                .map(requestRepository::save)
                .map(RequestMapper::toRequestDto)
                .toList();
    }

    private Map<String, List<RequestDto>> confirmRequests(Event event, List<Request> requests, RequestUpdateDto dto) {
        Map<String, List<RequestDto>> requestMap = new HashMap<>();
        long limit = event.getParticipantLimit() - event.getConfirmedRequests();
        List<Request> confirmedList = requests.stream()
                .limit(limit)
                .peek(request -> request.setStatus(RequestStatus.CONFIRMED))
                .map(requestRepository::save)
                .toList();
        requestMap.put("confirmedRequests", confirmedList.stream()
                .map(RequestMapper::toRequestDto)
                .toList());
        List<Request> rejectedList = requests.stream()
                .skip(limit)
                .peek(request -> request.setStatus(RequestStatus.REJECTED))
                .map(requestRepository::save)
                .toList();
        requestMap.put("rejectedRequests", rejectedList.stream()
                .map(RequestMapper::toRequestDto)
                .toList());
        event.setConfirmedRequests(confirmedList.size() + event.getConfirmedRequests());
        eventRepository.save(event);
        return requestMap;
    }

    @Override
    @Transactional(readOnly = true)
    public List<EventResponseLongDto> getEventsByAdministrator(
            final List<Long> usersId,
            final List<String> states,
            final List<Long> categoriesId,
            final String rangeStart,
            final String rangeEnd,
            final int from,
            final int size) {
        log.info("Заявка на получение событий по переданным параметрам.");
        LocalDateTime start = (rangeStart != null) ? LocalDateTime.parse(rangeStart, formatter) : LocalDateTime.now();
        LocalDateTime end = (rangeEnd != null) ? LocalDateTime.parse(rangeEnd, formatter) : LocalDateTime.now().plusYears(20);
        Pageable pageable = PageRequest.of(from / size, size, Sort.by(Sort.Direction.ASC, "id"));
        if (start.isAfter(end)) {
            throw new EWMBadRequestException("Временной промежуток задан неверно.");
        }
        List<User> users = (usersId == null || usersId.isEmpty()) ? userRepository.findAll() : userRepository.findByIdIn(usersId, pageable);
        if (users.isEmpty() || (usersId != null && users.size() != usersId.size())) {
            throw new EWMBadRequestException("Список пользователей передан неверно.");
        }
        List<EventState> eventStates = (states == null || states.isEmpty()) ?
                List.of(EventState.PUBLISHED, EventState.CANCELED, EventState.PENDING) :
                states.stream().map(EventState::valueOf).collect(Collectors.toList());
        List<Category> categories = (categoriesId == null) ? categoryRepository.findAll() : categoryRepository.findByIdIn(categoriesId, pageable);
        if (categories.isEmpty() || (categoriesId != null && categories.size() != categoriesId.size())) {
            throw new EWMBadRequestException("Список категорий передан неверно.");
        }
        List<Event> events = eventRepository.findByInitiatorInAndStateInAndCategoryInAndEventDateAfterAndEventDateBefore(
                users, eventStates, categories, start, end, pageable);
        if (events.isEmpty()) {
            log.info("По данным параметрам не нашлось ни одного события.");
            return Collections.emptyList();
        }
        log.info("Получен список событий по заданным параметрам.");
        return events.stream().map(EventMapper::toEventResponseLongDto).collect(Collectors.toList());
    }

    @Override
    @Transactional(readOnly = true)
    public EventResponseLongDto getEventByPrivate(final Long userId, final Long eventId) {
        log.info("Получение события id = {}.", eventId);
        Event event = eventRepository.findById(eventId)
                .orElseThrow(() -> new EWMNotFoundException("Такого события id " + eventId + " нет."));
        if (!event.getInitiator().getId().equals(userId)) {
            throw new EWMBadRequestException("Только пользователь, создавший событие, может получить его полное описание.");
        }
        log.info("Получено событие id {}.", eventId);
        return EventMapper.toEventResponseLongDto(event);
    }

    @Override
    @Transactional(readOnly = true)
    public List<EventResponseShortDto> getEventsByPrivate(final Long userId, final int from, final int size) {
        log.info("Получение событий пользователя id {}.", userId);
        Pageable pageable = PageRequest.of(from / size, size, Sort.by(Sort.Direction.ASC, "id"));
        List<Event> events = eventRepository.findByInitiatorId(userId, pageable);
        if (events.isEmpty()) {
            log.info("У пользователя id {} нет событий.", userId);
            return Collections.emptyList();
        }
        log.info("Получен список событий пользователя id {}.", userId);
        return events.stream().map(EventMapper::toEventResponseShortDto).collect(Collectors.toList());
    }


    @Override
    @Transactional(readOnly = true)
    public EventResponseLongDto getEventByPublic(final Long eventId, final HttpServletRequest request) {
        log.info("Получение опубликованного события id {}.", eventId);
        Event event = eventRepository.findByIdAndState(eventId, EventState.PUBLISHED)
                .orElseThrow(() -> new EWMNotFoundException("Такого события id " + eventId + " нет."));
        if (!event.getState().equals(EventState.PUBLISHED)) {
            throw new EWMNotFoundException("У события должен быть статус PUBLISHED.");
        }
        Map<Long, Long> view = statsService.getView(List.of(event.getId()), true);
        EventResponseLongDto eventResponseLongDto = EventMapper.toEventResponseLongDto(event);
        eventResponseLongDto.setViews(Math.toIntExact(view.getOrDefault(event.getId(), 0L)));
        statsService.createStat(request.getRequestURI(), request.getRemoteAddr());
        return eventResponseLongDto;
    }

    @Override
    @Transactional(readOnly = true)
    public List<EventResponseShortDto> getEventsByPublic(final String text,
                                                         final List<Long> categories,
                                                         final Boolean paid,
                                                         final String rangeStart,
                                                         final String rangeEnd,
                                                         final boolean onlyAvailable,
                                                         final EventSort sort,
                                                         final int from,
                                                         final int size,
                                                         final HttpServletRequest request) {
        log.info("Получение опубликованных событий.");
        LocalDateTime start = (Objects.nonNull(rangeStart)) ? LocalDateTime.parse(rangeStart, formatter) : LocalDateTime.now();
        LocalDateTime end = (Objects.nonNull(rangeEnd)) ? LocalDateTime.parse(rangeEnd, formatter) : LocalDateTime.now().plusYears(20);
        PageRequest pageRequest = PageRequest.of(from / size, size);
        if (start.isAfter(end)) {
            throw new EWMBadRequestException("Временной промежуток задан неверно.");
        }
        StringBuilder queryStr = new StringBuilder("""
                SELECT e
                FROM Event e
                JOIN FETCH e.category c
                WHERE e.eventDate >= :start AND e.eventDate <= :end
                """);
        if (Objects.nonNull(text) && !text.isEmpty()) {
            queryStr.append(" AND (LOWER(e.annotation) LIKE LOWER(:text) OR LOWER(e.description) LIKE LOWER(:text))");
        }
        if (Objects.nonNull(categories) && !categories.isEmpty()) {
            queryStr.append(" AND e.category.id IN :categories");
        }
        if (Objects.nonNull(paid)) {
            queryStr.append(" AND e.paid = :paid");
        }
        queryStr.append(" AND e.participantLimit > e.confirmedRequests");
        TypedQuery<Event> query = entityManager.createQuery(queryStr.toString(), Event.class)
                .setParameter("start", start)
                .setParameter("end", end);

        if (Objects.nonNull(text) && !text.isEmpty()) {
            query.setParameter("text", "%" + text + "%");
        }
        if (Objects.nonNull(categories) && !categories.isEmpty()) {
            query.setParameter("categories", categories);
        }
        if (Objects.nonNull(paid)) {
            query.setParameter("paid", paid);
        }
        query.setFirstResult(pageRequest.getPageNumber() * pageRequest.getPageSize());
        query.setMaxResults(pageRequest.getPageSize());
        List<Event> events = query.getResultList();
        Map<Long, Long> eventAndViews = statsService.getView(events.stream().map(Event::getId).toList(), false);
        events.forEach(e -> e.setViews(Math.toIntExact(eventAndViews.getOrDefault(e.getId(), 0L))));
        if (Objects.nonNull(sort)) {
            if (sort.equals(EventSort.EVENT_DATE)) {
                events.sort(Comparator.comparing(Event::getEventDate));
            } else if (sort.equals(EventSort.VIEWS)) {
                events.sort(Comparator.comparing(Event::getViews).reversed());
            }
        }
        if (events.stream().noneMatch(e -> e.getState().equals(EventState.PUBLISHED))) {
            throw new EWMBadRequestException("Нет опубликованных событий.");
        }
        List<Event> paginatedEvents = events.stream().skip(from).toList();
        statsService.createStat(request.getRequestURI(), request.getRemoteAddr());
        return paginatedEvents.stream()
                .map(EventMapper::toEventResponseShortDto)
                .peek(dto -> {
                    Long viewCount = eventAndViews.get(dto.getId());
                    dto.setViews(viewCount != null ? viewCount.intValue() : 0);
                }).toList();
    }

    @Override
    @Transactional(readOnly = true)
    public List<RequestDto> getRequestsEventByPrivate(final Long userId,
                                                      final Long eventId) {
        log.info("Получение всех заявок на событие id {}.", eventId);
        final User user = userRepository.findById(userId)
                .orElseThrow(() -> new EWMNotFoundException("Такого пользователя id {} нет." + userId));
        final Event event = eventRepository.findById(eventId)
                .orElseThrow(() -> new EWMNotFoundException("Такого события id {} нет." + eventId));
        if (!event.getInitiator().equals(user)) {
            throw new EWMConflictException("Это событие добавил другой пользователь.");
        }
        final List<Request> requests = requestRepository.findByEventId(eventId);
        if (requests.isEmpty()) {
            log.info("Пока нет заявок на участие в мероприятии id = {}.", eventId);
            return new ArrayList<>();
        }
        log.info("Получен список всех заявок на участие в мероприятии id {}.", eventId);
        return requests.stream().map(RequestMapper::toRequestDto).toList();
    }

    @Override
    @Transactional
    public EventResponseLongDto updateEventByPrivate(final Long userId,
                                                     final Long eventId,
                                                     final EventUpdateUserDto eventUpdateUserDto) {
        log.info("Обновление события id {}.", eventId);
        final User user = userRepository.findById(userId)
                .orElseThrow(() -> new EWMNotFoundException("Такого пользователя id {} нет." + userId));
        final Event oldEvent = eventRepository.findById(eventId)
                .orElseThrow(() -> new EWMNotFoundException("Такого события id {} нет." + eventId));
        if (!oldEvent.getInitiator().getId().equals(user.getId())) {
            throw new EWMBadRequestException("Только пользователь создавший событие может его редактировать.");
        }
        if (oldEvent.getState().equals(EventState.PUBLISHED)) {
            throw new EWMConflictException("Нельзя изменить опубликованное событие, или переданный статут не существует.");
        }
        if (Objects.nonNull(eventUpdateUserDto.getEventDate()) &&
                eventUpdateUserDto.getEventDate().isBefore(LocalDateTime.now().plusHours(2))) {
            throw new EWMBadRequestException("Событие не может начинаться ранее чем через 2 часа после обновления.");
        }
        if (Objects.nonNull(eventUpdateUserDto.getAnnotation())) {
            oldEvent.setAnnotation(eventUpdateUserDto.getAnnotation());
        }
        if (Objects.nonNull(eventUpdateUserDto.getCategory())) {
            final Category category = categoryRepository.findById(eventUpdateUserDto.getCategory())
                    .orElseThrow(() -> new EWMBadRequestException("Категория указана неверно."));
            oldEvent.setCategory(category);
        }
        if (Objects.nonNull(eventUpdateUserDto.getDescription())) {
            oldEvent.setDescription(eventUpdateUserDto.getDescription());
        }
        if (Objects.nonNull(eventUpdateUserDto.getEventDate())) {
            oldEvent.setEventDate(eventUpdateUserDto.getEventDate());
        }
        if (Objects.nonNull(eventUpdateUserDto.getLocation())) {
            oldEvent.setLon(eventUpdateUserDto.getLocation().lon());
            oldEvent.setLat(eventUpdateUserDto.getLocation().lat());
        }
        if (Objects.nonNull(eventUpdateUserDto.getPaid())) {
            oldEvent.setPaid(eventUpdateUserDto.getPaid());
        }
        if (Objects.nonNull(eventUpdateUserDto.getParticipantLimit())) {
            oldEvent.setParticipantLimit(eventUpdateUserDto.getParticipantLimit());
        }
        if (Objects.nonNull(eventUpdateUserDto.getRequestModeration())) {
            oldEvent.setRequestModeration(eventUpdateUserDto.getRequestModeration());
        }
        if (Objects.nonNull(eventUpdateUserDto.getStateAction()) &&
                eventUpdateUserDto.getStateAction().equals(StateAction.SEND_TO_REVIEW)) {
               oldEvent.setState(EventState.PENDING);
        }
        if (Objects.nonNull(eventUpdateUserDto.getStateAction()) &&
                eventUpdateUserDto.getStateAction().equals(StateAction.CANCEL_REVIEW)) {
            oldEvent.setState(EventState.CANCELED);
        }
        if (Objects.nonNull(eventUpdateUserDto.getTitle())) {
            oldEvent.setTitle(eventUpdateUserDto.getTitle());
        }
        final Event event = eventRepository.save(oldEvent);
        log.info("Событие успешно обновлено под id {} и дожидается подтверждения.", eventId);
        return EventMapper.toEventResponseLongDto(event);
    }

}