package ru.practicum.stats.server.service;

import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.practicum.stats.dto.StatDtoInput;
import ru.practicum.stats.dto.StatDtoOutput;
import ru.practicum.stats.server.exception.EWMValidationException;
import ru.practicum.stats.server.mapper.StatMapper;
import ru.practicum.stats.server.model.Stat;
import ru.practicum.stats.server.repository.StatRepository;
import java.time.LocalDateTime;
import java.util.Collection;

@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
@RequiredArgsConstructor
@Service
@Slf4j
public class StatServiceImplements implements StatService {

    StatMapper statMapper;
    StatRepository statRepository;

    @Override
    @Transactional(readOnly = true)
    public Collection<StatDtoOutput> findStatEvent(final LocalDateTime start,
                                                   final LocalDateTime end,
                                                   final Collection<String> uris,
                                                   final boolean isUniqueVisits) {
        log.info("Поиск списка статистики");
        if (start.isAfter(end)) {
            throw new EWMValidationException("Дата начала не может быть позже даты окончания");
        }
        if (uris.isEmpty()) {
            log.info("Поиск списка всех элементов статистики");
            return isUniqueVisits ? statRepository.findAllUniqueIp(start, end) : statRepository.findAllCount(start, end);
        }
        log.info("Поиск списка {} элементов статистики для конкретных uri", isUniqueVisits ? "уникальных " : "не уникальных ");
        return isUniqueVisits ? statRepository.findAllUniqueUris(start, end, uris)
                : statRepository.findAllNoUniqueUris(start, end, uris);
    }

    @Override
    @Transactional
    public StatDtoOutput saveStatEvent(final StatDtoInput statDtoInput) {
        log.info("Сохранение элемента статистики в БД");
        final Stat stat = statRepository.save(statMapper.toStat(statDtoInput));
        log.info("Элемент статистики сохранён в БД");
        return statMapper.toStatDtoOutput(stat);
    }

}