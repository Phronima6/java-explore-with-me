package ru.practicum.main.service.event.service;

import jakarta.servlet.http.HttpServletRequest;
import ru.practicum.main.service.event.dto.*;
import ru.practicum.main.service.event.model.EventSort;
import ru.practicum.main.service.request.dto.RequestDto;
import ru.practicum.main.service.request.dto.RequestUpdateDto;
import java.util.List;
import java.util.Map;

public interface EventService {

    EventResponseLongDto createEventByPrivate(final Long userId,
                                              final EventRequestDto eventRequestDto);

    EventResponseLongDto approveRequestsByAdministrator(final Long eventId,
                                                        final EventUpdateDto eventUpdateDto);

    Map<String, List<RequestDto>> approveRequestsByPrivate(final Long userId,
                                                           final Long eventId,
                                                           final RequestUpdateDto requestUpdateDto);

    List<EventResponseLongDto> getEventsByAdministrator(final List<Long> usersId,
                                                        final List<String> states,
                                                        final List<Long> categoriesId,
                                                        final String rangeStart,
                                                        final String rangeEnd,
                                                        final int from,
                                                        final int size);

    EventResponseLongDto getEventByPrivate(final Long userId,
                                           final Long eventId);

    List<EventResponseShortDto> getEventsByPrivate(final Long userId,
                                                   final int from,
                                                   final int size);

    EventResponseLongDto getEventByPublic(final Long eventId,
                                          final HttpServletRequest request);

    List<EventResponseShortDto> getEventsByPublic(final String text,
                                                  final List<Long> categories,
                                                  final Boolean paid,
                                                  final String rangeStart,
                                                  final String rangeEnd,
                                                  final boolean onlyAvailable,
                                                  final EventSort sort,
                                                  final int from,
                                                  final int size,
                                                  final HttpServletRequest request);

    List<RequestDto> getRequestsEventByPrivate(final Long userId,
                                               final Long eventId);

    EventResponseLongDto updateEventByPrivate(final Long userId,
                                              final Long eventId,
                                              final EventUpdateUserDto eventUpdateUserDto);

}