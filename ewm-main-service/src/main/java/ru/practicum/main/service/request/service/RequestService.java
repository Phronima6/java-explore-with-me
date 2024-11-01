package ru.practicum.main.service.request.service;

import ru.practicum.main.service.request.dto.RequestDto;
import java.util.List;

public interface RequestService {

    RequestDto cancelRequest(final Long userId,
                             final Long requestId);

    RequestDto createRequest(final Long userId,
                             final Long eventId);

    List<RequestDto> getRequests(final Long userId);

}