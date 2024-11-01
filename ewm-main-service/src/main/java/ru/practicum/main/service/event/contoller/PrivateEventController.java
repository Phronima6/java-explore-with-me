package ru.practicum.main.service.event.contoller;

import jakarta.validation.Valid;
import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.PositiveOrZero;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.http.HttpStatus;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import ru.practicum.main.service.event.dto.*;
import ru.practicum.main.service.event.service.EventService;
import ru.practicum.main.service.request.dto.RequestDto;
import ru.practicum.main.service.request.dto.RequestUpdateDto;
import java.util.List;
import java.util.Map;

@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
@RequiredArgsConstructor
@RequestMapping("/users/{user-id}/events")
@RestController
@Validated
public class PrivateEventController {

    EventService eventService;
    static final String PATH_EVENT_ID = "event-id";
    static final String PATH_USER_ID = "user-id";

    @PatchMapping("/{" + PATH_EVENT_ID + "}/requests")
    public Map<String, List<RequestDto>> approveRequestsByPrivate(@PathVariable(PATH_USER_ID) final Long userId,
                                                                  @PathVariable(PATH_EVENT_ID) final Long eventId,
                                                                  @RequestBody @Valid final RequestUpdateDto requestUpdateDto) {
        return eventService.approveRequestsByPrivate(userId, eventId, requestUpdateDto);
    }

    @PostMapping
    @ResponseStatus(code = HttpStatus.CREATED)
    public EventResponseLongDto createEventByPrivate(@PathVariable(PATH_USER_ID) @Positive final Long userId,
                                                     @RequestBody @Valid final EventRequestDto eventRequestDto) {
        return eventService.createEventByPrivate(userId, eventRequestDto);
    }

    @GetMapping("/{" + PATH_EVENT_ID + "}")
    public EventResponseLongDto getEventByPrivate(@PathVariable(PATH_USER_ID) @Positive final Long userId,
                                                  @PathVariable(PATH_EVENT_ID) @Positive final Long eventId) {
        return eventService.getEventByPrivate(userId, eventId);
    }

    @GetMapping
    public List<EventResponseShortDto> getEventsByPrivate(@PathVariable(PATH_USER_ID) @Positive final Long userId,
                                                          @RequestParam(defaultValue = "0") @PositiveOrZero final int from,
                                                          @RequestParam(defaultValue = "10") @Positive final int size) {
        return eventService.getEventsByPrivate(userId, from, size);
    }

    @GetMapping("/{" + PATH_EVENT_ID + "}/requests")
    public List<RequestDto> getRequestsEventByPrivate(@PathVariable(PATH_USER_ID) final Long userId,
                                                      @PathVariable(PATH_EVENT_ID) final Long eventId) {
        return eventService.getRequestsEventByPrivate(userId, eventId);
    }

    @PatchMapping("/{" + PATH_EVENT_ID + "}")
    public EventResponseLongDto updateEventByPrivate(@PathVariable(PATH_USER_ID) @Positive final Long userId,
                                                     @PathVariable(PATH_EVENT_ID) @Positive final Long eventId,
                                                     @RequestBody @Valid final EventUpdateUserDto eventUpdateUserDto) {
        return eventService.updateEventByPrivate(userId, eventId, eventUpdateUserDto);
    }

}