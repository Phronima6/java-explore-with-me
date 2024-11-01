package ru.practicum.main.service.event.contoller;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.PositiveOrZero;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import ru.practicum.main.service.event.dto.EventResponseLongDto;
import ru.practicum.main.service.event.dto.EventResponseShortDto;
import ru.practicum.main.service.event.model.EventSort;
import ru.practicum.main.service.event.service.EventService;
import java.util.List;

@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
@RequiredArgsConstructor
@RequestMapping("/events")
@RestController
@Validated
public class PublicEventController {

    EventService eventService;
    static final String PATH_EVENT_ID = "event-id";

    @GetMapping
    public List<EventResponseShortDto> getAllPublic(@RequestParam(defaultValue = "") final String text,
                                                    @RequestParam(required = false) final List<Long> categories,
                                                    @RequestParam(required = false) final Boolean paid,
                                                    @RequestParam(required = false) final String rangeStart,
                                                    @RequestParam(required = false) final String rangeEnd,
                                                    @RequestParam(defaultValue = "false") final boolean onlyAvailable,
                                                    @RequestParam(required = false) final EventSort sort,
                                                    @RequestParam(defaultValue = "0") @PositiveOrZero final int from,
                                                    @RequestParam(defaultValue = "10") @Positive final int size,
                                                    final HttpServletRequest request) {
        return eventService.getEventsByPublic(text, categories, paid, rangeStart, rangeEnd, onlyAvailable, sort, from, size, request);
    }

    @GetMapping("/{" + PATH_EVENT_ID + "}")
    public EventResponseLongDto getEventByPublic(@PathVariable(PATH_EVENT_ID) final Long eventId,
                                                 final HttpServletRequest request) {
        return eventService.getEventByPublic(eventId, request);
    }

}