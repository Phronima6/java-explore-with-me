package ru.practicum.stats.server.service;

import ru.practicum.stats.dto.StatDtoInput;
import ru.practicum.stats.dto.StatDtoOutput;
import java.time.LocalDateTime;
import java.util.Collection;

public interface StatService {

    // Получение статистики по посещениям
    Collection<StatDtoOutput> findStatEvent(final LocalDateTime start,
                                 final LocalDateTime end,
                                 final Collection<String> uris,
                                 final boolean unique);

    // Сохранение информации о запросе
    StatDtoOutput saveStatEvent(final StatDtoInput statDtoInput);

}