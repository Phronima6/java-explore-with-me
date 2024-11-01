package ru.practicum.stats.server.controller;

import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import ru.practicum.stats.dto.StatDtoInput;
import ru.practicum.stats.dto.StatDtoOutput;
import ru.practicum.stats.server.service.StatService;
import java.time.LocalDateTime;
import java.util.Collection;

@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
@RestController
@RequiredArgsConstructor
@Validated
public class StatController {

    StatService statService;
    static final String DATE_FORMAT = "yyyy-MM-dd HH:mm:ss";

    @GetMapping("/stats")
    public Collection<StatDtoOutput> getStatEvent(
            @RequestParam @DateTimeFormat(pattern = DATE_FORMAT) final LocalDateTime start,
            @RequestParam @DateTimeFormat(pattern = DATE_FORMAT) final LocalDateTime end,
            @RequestParam(defaultValue = "") final Collection<String> uris,
            @RequestParam(value = "unique", defaultValue = "false") final Boolean isUniqueVisits) {
        return statService.getStatEvent(start, end, uris, isUniqueVisits);
    }

    @PostMapping("/hit")
    @ResponseStatus(HttpStatus.CREATED)
    public StatDtoOutput createStatEvent(@RequestBody final StatDtoInput statDtoInput) {
        return statService.createStatEvent(statDtoInput);
    }

}