package ru.practicum.stats.server.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import ru.practicum.stats.server.model.Stat;
import java.time.LocalDateTime;
import java.util.Collection;
import ru.practicum.stats.dto.StatDtoOutput;

public interface StatRepository extends JpaRepository<Stat, Long> {

    @Query("""
            SELECT new ru.practicum.stats.dto.StatDtoOutput(s.app, COUNT(s.ip) AS hits, s.uri)
            FROM Stat s
            WHERE s.timestamp BETWEEN :start AND :end
            GROUP BY s.app, s.uri
            ORDER BY COUNT(s.ip) DESC
            """)
    Collection<StatDtoOutput> findAllCount(@Param("start") final LocalDateTime start,
                                           @Param("end") final LocalDateTime end);

    @Query("""
            SELECT new ru.practicum.stats.dto.StatDtoOutput(s.app, COUNT(s.ip) AS hits, s.uri)
            FROM Stat s
            WHERE s.timestamp BETWEEN :start AND :end AND s.uri IN :uris
            GROUP BY s.app, s.uri
            ORDER BY COUNT(s.ip) DESC
            """)
    Collection<StatDtoOutput> findAllNoUniqueUris(@Param("start") final LocalDateTime start,
                                                  @Param("end") final LocalDateTime end,
                                                  @Param("uris") final Collection<String> uris);

    @Query("""
            SELECT new ru.practicum.stats.dto.StatDtoOutput(s.app, COUNT(DISTINCT s.ip) AS hits, s.uri)
            FROM Stat s
            WHERE s.timestamp BETWEEN :start AND :end
            GROUP BY s.app, s.uri
            ORDER BY COUNT(DISTINCT s.ip) DESC
            """)
    Collection<StatDtoOutput> findAllUniqueIp(@Param("start") final LocalDateTime start,
                                              @Param("end") final LocalDateTime end);

    @Query("""
            SELECT new ru.practicum.stats.dto.StatDtoOutput(s.app, COUNT(DISTINCT s.ip) AS hits, s.uri)
            FROM Stat s
            WHERE s.timestamp BETWEEN :start AND :end AND s.uri IN :uris
            GROUP BY s.app, s.uri
            ORDER BY COUNT(DISTINCT s.ip) DESC
            """)
    Collection<StatDtoOutput> findAllUniqueUris(@Param("start") final LocalDateTime start,
                                                @Param("end") final LocalDateTime end,
                                                @Param("uris") final Collection<String> uris);

}