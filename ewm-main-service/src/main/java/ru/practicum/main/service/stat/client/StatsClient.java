package ru.practicum.main.service.stat.client;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import ru.practicum.stats.dto.StatDtoInput;
import ru.practicum.stats.dto.StatDtoOutput;
import java.util.Collection;

@FeignClient(value = "stats-client", url = "http://stats-server:9090")
public interface StatsClient {

    @PostMapping("/hit")
    StatDtoInput createStat(@RequestBody final StatDtoInput statDtoInput);

    @GetMapping("/stats")
    Collection<StatDtoOutput> getStat(@RequestParam final String start,
                                      @RequestParam final String end,
                                      @RequestParam(required = false) final String[] uris,
                                      @RequestParam(value = "unique", defaultValue = "false") final Boolean isUniqueVisits);

}