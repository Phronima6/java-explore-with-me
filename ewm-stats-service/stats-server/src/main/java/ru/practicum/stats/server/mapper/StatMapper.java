package ru.practicum.stats.server.mapper;

import org.springframework.stereotype.Component;
import ru.practicum.stats.dto.StatDtoInput;
import ru.practicum.stats.dto.StatDtoOutput;
import ru.practicum.stats.server.model.Stat;

@Component
public class StatMapper {

    public Stat toStat(final StatDtoInput statDtoInput) {
        final Stat stat = new Stat();
        stat.setApp(statDtoInput.getApp());
        stat.setIp(statDtoInput.getIp());
        stat.setTimestamp(statDtoInput.getTimestamp());
        stat.setUri(statDtoInput.getUri());
        return stat;
    }

    public StatDtoOutput toStatDtoOutput(final Stat stat) {
        final StatDtoOutput statDtoOutput = new StatDtoOutput();
        statDtoOutput.setApp(stat.getApp());
        statDtoOutput.setUri(stat.getUri());
        return statDtoOutput;
    }

}