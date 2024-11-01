package ru.practicum.main.service.stat.service;

import ru.practicum.stats.dto.StatDtoOutput;
import java.util.Collection;
import java.util.Map;

public interface StatsService {

    void createStat(final String uri,
                    final String ip);

    Collection<StatDtoOutput> getStat(final Collection<Long> eventsId,
                                      final Boolean isUniqueVisits);

    Map<Long, Long> getView(final Collection<Long> eventsId,
                            final Boolean isUniqueVisits);

}