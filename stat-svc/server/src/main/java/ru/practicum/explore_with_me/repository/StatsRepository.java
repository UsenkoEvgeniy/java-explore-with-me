package ru.practicum.explore_with_me.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import ru.practicum.explore_with_me.stats.ViewStats;
import ru.practicum.explore_with_me.model.EndpointHit;

import java.time.LocalDateTime;
import java.util.Collection;
import java.util.List;

public interface StatsRepository extends JpaRepository<EndpointHit, Integer> {
    @Query("select new ru.practicum.explore_with_me.stats.ViewStats(e.app, e.uri, count(e.ip) as hits) " +
            "from EndpointHit e " +
            "where ((coalesce(:uris) is null) or (e.uri in :uris)) " +
            "and e.timestamp between :start and :end " +
            "group by e.app, e.uri " +
            "order by hits desc")
    List<ViewStats> getViewStats(@Param("uris") Collection<String> uris,
                                 @Param("start") LocalDateTime start,
                                 @Param("end") LocalDateTime end);

    @Query("select new ru.practicum.explore_with_me.stats.ViewStats(e.app, e.uri, count(distinct e.ip) as hits) " +
            "from EndpointHit e " +
            "where ((coalesce(:uris) is null) or (e.uri in :uris)) " +
            "and e.timestamp between :start and :end " +
            "group by e.app, e.uri " +
            "order by hits desc")
    List<ViewStats> getViewStatsUnique(@Param("uris") Collection<String> uris,
                                 @Param("start") LocalDateTime start,
                                 @Param("end") LocalDateTime end);

}