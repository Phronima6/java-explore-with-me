package ru.practicum.stats.server.service;

import ru.practicum.stats.dto.StatDtoInput;
import ru.practicum.stats.dto.StatDtoOutput;
import java.time.LocalDateTime;
import java.util.Collection;

public interface StatService {

    // Сохранение информации о запросе
    StatDtoOutput createStatEvent(final StatDtoInput statDtoInput);

    // Получение статистики по посещениям
    Collection<StatDtoOutput> getStatEvent(final LocalDateTime start,
                                           final LocalDateTime end,
                                           final Collection<String> uris,
                                           final Boolean isUniqueVisits);

}