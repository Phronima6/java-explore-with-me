package ru.practicum.main.service.event.mapper;

import ru.practicum.main.service.category.dto.CategoryResponseDto;
import ru.practicum.main.service.category.model.Category;
import ru.practicum.main.service.event.dto.EventRequestDto;
import ru.practicum.main.service.event.dto.EventResponseLongDto;
import ru.practicum.main.service.event.dto.EventResponseShortDto;
import ru.practicum.main.service.event.model.Event;
import ru.practicum.main.service.event.model.EventState;
import ru.practicum.main.service.event.model.Location;
import ru.practicum.main.service.user.dto.UserShortDto;
import ru.practicum.main.service.user.model.User;
import java.time.LocalDateTime;

public class EventMapper {

    public static Event toEvent(final EventRequestDto eventRequestDto,
                                final User initiator,
                                final Category category) {
        final Event event = new Event();
        event.setAnnotation(eventRequestDto.getAnnotation());
        event.setCategory(category);
        event.setConfirmedRequests(0);
        event.setCreatedOn(LocalDateTime.now());
        event.setDescription(eventRequestDto.getDescription());
        event.setEventDate(eventRequestDto.getEventDate());
        event.setInitiator(initiator);
        event.setLat(eventRequestDto.getLocation().lat());
        event.setLon(eventRequestDto.getLocation().lon());
        event.setPaid(eventRequestDto.getPaid());
        event.setParticipantLimit(eventRequestDto.getParticipantLimit());
        event.setRequestModeration(eventRequestDto.getRequestModeration());
        event.setState(EventState.PENDING);
        event.setTitle(eventRequestDto.getTitle());
        return event;
    }

    public static EventResponseShortDto toEventResponseShortDto(final Event event) {
        final EventResponseShortDto eventResponseShortDto = new EventResponseShortDto();
        eventResponseShortDto.setAnnotation(event.getAnnotation());
        eventResponseShortDto.setCategory(new CategoryResponseDto(event.getCategory().getId(), event.getCategory().getName()));
        eventResponseShortDto.setConfirmedRequests(event.getConfirmedRequests());
        eventResponseShortDto.setEventDate(event.getEventDate());
        eventResponseShortDto.setId(event.getId());
        eventResponseShortDto.setInitiator(new UserShortDto(event.getInitiator().getId(), event.getInitiator().getName()));
        eventResponseShortDto.setPaid(event.getPaid());
        eventResponseShortDto.setTitle(event.getTitle());
        eventResponseShortDto.setViews(0);
        return eventResponseShortDto;
    }

    public static EventResponseLongDto toEventResponseLongDto(final Event event) {
        final EventResponseLongDto eventResponseLongDto = new EventResponseLongDto();
        eventResponseLongDto.setAnnotation(event.getAnnotation());
        eventResponseLongDto.setCategory(new CategoryResponseDto(event.getCategory().getId(), event.getCategory().getName()));
        eventResponseLongDto.setConfirmedRequests(event.getConfirmedRequests());
        eventResponseLongDto.setCreatedOn(event.getCreatedOn());
        eventResponseLongDto.setDescription(event.getDescription());
        eventResponseLongDto.setEventDate(event.getEventDate());
        eventResponseLongDto.setId(event.getId());
        eventResponseLongDto.setInitiator(new UserShortDto(event.getInitiator().getId(), event.getInitiator().getName()));
        eventResponseLongDto.setLocation(new Location(event.getLat(), event.getLon()));
        eventResponseLongDto.setPaid(event.getPaid());
        eventResponseLongDto.setParticipantLimit(event.getParticipantLimit());
        eventResponseLongDto.setPublishedOn(event.getPublishedOn());
        eventResponseLongDto.setRequestModeration(event.getRequestModeration());
        eventResponseLongDto.setState(event.getState());
        eventResponseLongDto.setTitle(event.getTitle());
        eventResponseLongDto.setViews(0);
        return eventResponseLongDto;
    }

}