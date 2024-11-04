package ru.practicum.main.service.request.service;

import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.practicum.main.service.event.model.Event;
import ru.practicum.main.service.event.model.EventState;
import ru.practicum.main.service.event.repository.EventRepository;
import ru.practicum.main.service.exception.EWMConflictException;
import ru.practicum.main.service.exception.EWMNotFoundException;
import ru.practicum.main.service.request.dto.RequestDto;
import ru.practicum.main.service.request.mapper.RequestMapper;
import ru.practicum.main.service.request.model.Request;
import ru.practicum.main.service.request.model.RequestStatus;
import ru.practicum.main.service.request.repository.RequestRepository;
import ru.practicum.main.service.user.model.User;
import ru.practicum.main.service.user.repository.UserRepository;
import java.time.LocalDateTime;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Slf4j
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class RequestServiceImplements implements RequestService {

    RequestRepository requestRepository;
    UserRepository userRepository;
    EventRepository eventRepository;

    @Override
    @Transactional
    public RequestDto createRequest(final Long userId,
                                    final Long eventId) {
        log.info("Создание заявки на участие пользователя id {} в событии id {}.", userId, eventId);
        Event event = eventRepository.findById(eventId)
                .orElseThrow(() -> new EWMNotFoundException("Событие id " + eventId + " не найдено."));
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new EWMNotFoundException("Пользователь id " + userId + " не найден."));
        if (requestRepository.findByRequesterIdAndEventId(userId, eventId) != null) {
            throw new EWMConflictException("Пользователь уже создал заявку на участие в событии.");
        }
        if (event.getInitiator().equals(user)) {
            throw new EWMConflictException("Пользователь не может создать заявку на участие в своём мероприятии.");
        }
        if (!event.getState().equals(EventState.PUBLISHED)) {
            throw new EWMConflictException("Нельзя подавать заявку на неопубликованное событие.");
        }
        if (event.getParticipantLimit() != 0 && event.getParticipantLimit().equals(event.getConfirmedRequests())) {
            throw new EWMConflictException("На данное событие отсутствуют свободные места.");
        }
        Request request = new Request();
        request.setEvent(event);
        request.setRequester(user);
        request.setCreated(LocalDateTime.now());
        if (event.getParticipantLimit() == 0 || !event.getRequestModeration() && event.getParticipantLimit() > event.getConfirmedRequests()) {
            request.setStatus(RequestStatus.CONFIRMED);
            event.setConfirmedRequests(event.getConfirmedRequests() + 1);
            eventRepository.save(event);
        } else if (!event.getRequestModeration() && event.getParticipantLimit().equals(event.getConfirmedRequests())) {
            request.setStatus(RequestStatus.REJECTED);
            log.info("Превышения лимита. Заявка на участие сохранена со статусом CANCELED.");
        } else {
            request.setStatus(RequestStatus.PENDING);
        }
        Request newRequest = requestRepository.save(request);
        log.info("Заявка на участие сохранена со статусом {}.", request.getStatus());
        return RequestMapper.toRequestDto(newRequest);
    }

    @Override
    @Transactional
    public RequestDto cancelRequest(final Long userId,
                                    final Long requestId) {
        log.info("Отмена заявки на участие id {}.", requestId);
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new EWMNotFoundException("Пользователь id " + userId + " не найден."));
        Request request = requestRepository.findById(requestId)
                .orElseThrow(() -> new EWMNotFoundException("Заявка id " + requestId + " не найдена."));
        if (!request.getRequester().equals(user)) {
            throw new EWMConflictException("Ошибка доступа. Пользователь, подавший заявку, может отменить ее.");
        }
        request.setStatus(RequestStatus.CANCELED);
        Request canceledRequest = requestRepository.save(request);
        log.info("Заявка на участие id {} отменена.", requestId);
        Event event = request.getEvent();
        if (event.getRequestModeration()) {
            event.setConfirmedRequests(event.getConfirmedRequests() - 1);
            eventRepository.save(event);
            log.info("У события id {} появилось свободное место.", event.getId());
        }
        return RequestMapper.toRequestDto(canceledRequest);
    }

    @Override
    @Transactional(readOnly = true)
    public List<RequestDto> getRequests(final Long userId) {
        log.info("Получение всех заявок пользователя id {}.", userId);
        List<Request> requests = requestRepository.findByRequesterId(userId);
        if (requests.isEmpty()) {
            log.info("У пользователя id {} нет заявок на участии в событиях.", userId);
            return Collections.emptyList();
        }
        log.info("Получен список всех заявок пользователя id {}.", userId);
        return requests.stream()
                .map(RequestMapper::toRequestDto)
                .collect(Collectors.toList());
    }

}