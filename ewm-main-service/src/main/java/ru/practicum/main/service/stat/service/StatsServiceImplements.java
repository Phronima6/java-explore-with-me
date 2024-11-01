package ru.practicum.main.service.stat.service;

import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import ru.practicum.main.service.stat.client.StatsClient;
import ru.practicum.stats.dto.StatDtoInput;
import ru.practicum.stats.dto.StatDtoOutput;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.*;

@Service
@RequiredArgsConstructor
@Slf4j
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class StatsServiceImplements implements StatsService {

    StatsClient statsClient;
    DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
    static final String APP_NAME = "ewm-main-service";

    @Override
    public void createStat(final String uri,
                           final String ip) {
        log.info("Сохранение информации в сервисе статистики");
        final StatDtoInput statDtoInput = new StatDtoInput();
        statDtoInput.setApp(APP_NAME);
        statDtoInput.setIp(ip);
        statDtoInput.setUri(uri);
        statDtoInput.setTimestamp(LocalDateTime.now());
        statsClient.createStat(statDtoInput);
        log.info("Информация сохранена");
    }

    @Override
    public Collection<StatDtoOutput> getStat(final Collection<Long> eventsId,
                                             final Boolean isUniqueVisits) {
        log.info("Получение статистики для events id {}", eventsId);
        final String start = LocalDateTime.now().minusYears(20).format(formatter);
        final String end = LocalDateTime.now().plusYears(20).format(formatter);
        final String[] uris = eventsId.stream()
                .map(id -> String.format("/events/%d", id))
                .toArray(String[]::new);
        return statsClient.getStat(start, end, uris, isUniqueVisits);
    }

    @Override
    public Map<Long, Long> getView(final Collection<Long> eventsId,
                                   final Boolean isUniqueVisits) {
        log.info("Получение просмотров с сервиса статистики для events id {}", eventsId);
        final Collection<StatDtoOutput> stats = getStat(eventsId, isUniqueVisits);
        final Map<Long, Long> views = new HashMap<>();
        for (StatDtoOutput stat : stats) {
            final Long id = Long.valueOf(stat.getUri().replace("/events/", ""));
            final Long view = stat.getHits();
            views.put(id, view);
        }
        return views;
    }

}