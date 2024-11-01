package ru.practicum.main.service.request.mapper;

import ru.practicum.main.service.request.dto.RequestDto;
import ru.practicum.main.service.request.model.Request;

public class RequestMapper {

    public static RequestDto toRequestDto(final Request request) {
        final RequestDto requestDto = new RequestDto();
        requestDto.setCreated(request.getCreated());
        requestDto.setEvent(request.getEvent().getId());
        requestDto.setId(request.getId());
        requestDto.setRequester(request.getRequester().getId());
        requestDto.setStatus(request.getStatus());
        return requestDto;
    }

}