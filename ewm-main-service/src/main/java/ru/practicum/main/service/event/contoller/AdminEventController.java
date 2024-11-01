package ru.practicum.main.service.event.contoller;

import jakarta.validation.Valid;
import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.PositiveOrZero;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import ru.practicum.main.service.event.dto.EventResponseLongDto;
import ru.practicum.main.service.event.dto.EventUpdateDto;
import ru.practicum.main.service.event.service.EventService;
import java.util.List;

@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
@RequiredArgsConstructor
@RequestMapping("/admin/events")
@RestController
@Validated
public class AdminEventController {

    EventService eventService;
    static final String PATH_EVENT_ID = "event-id";

    @PatchMapping("/{" + PATH_EVENT_ID + "}")
    public EventResponseLongDto approveRequestsByAdministrator(@PathVariable(PATH_EVENT_ID) @Positive final Long eventId,
                                                               @RequestBody @Valid final EventUpdateDto eventUpdateDto) {
        return eventService.approveRequestsByAdministrator(eventId, eventUpdateDto);
    }

    @GetMapping
    public List<EventResponseLongDto> getEventsByAdministrator(@RequestParam(required = false) final List<Long> users,
                                                               @RequestParam(required = false) final List<String> states,
                                                               @RequestParam(required = false) final List<Long> categories,
                                                               @RequestParam(required = false) final String stringStart,
                                                               @RequestParam(required = false) final String stringEnd,
                                                               @RequestParam(defaultValue = "0") @PositiveOrZero final int from,
                                                               @RequestParam(defaultValue = "10") @Positive final int size) {
        return eventService.getEventsByAdministrator(users, states, categories, stringStart, stringEnd, from, size);
    }

}