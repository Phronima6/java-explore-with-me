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
    public Collection<StatDtoOutput> findStatEvent(
            @RequestParam @DateTimeFormat(pattern = DATE_FORMAT) final LocalDateTime start,
            @RequestParam @DateTimeFormat(pattern = DATE_FORMAT) final LocalDateTime end,
            @RequestParam(defaultValue = "") final Collection<String> uris,
            @RequestParam(defaultValue = "false") final boolean unique) {
        return statService.findStatEvent(start, end, uris, unique);
    }

    @PostMapping("/hit")
    @ResponseStatus(HttpStatus.CREATED)
    public StatDtoOutput saveStatEvent(@RequestBody final StatDtoInput statDto) {
        return statService.saveStatEvent(statDto);
    }

}