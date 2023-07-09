package ru.practicum.explore_with_me.service;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.practicum.explore_with_me.stats.EndpointHitDto;
import ru.practicum.explore_with_me.stats.ViewStats;
import ru.practicum.explore_with_me.mapper.EndpointHitMapper;
import ru.practicum.explore_with_me.repository.StatsRepository;

import javax.validation.ValidationException;
import java.time.LocalDateTime;
import java.util.List;

@Service
@Transactional
@Slf4j
public class StatsService {
    private final StatsRepository statsRepository;

    public StatsService(StatsRepository statsRepository) {
        this.statsRepository = statsRepository;
    }

    public void saveHit(EndpointHitDto hitDto) {
        statsRepository.save(EndpointHitMapper.mapToEndpointHit(hitDto));
    }

    @Transactional(readOnly = true)
    public List<ViewStats> getStats(LocalDateTime start, LocalDateTime end, List<String> uris, Boolean unique) {
        if (start.isAfter(end)) {
            throw new ValidationException("Start must be before end");
        }
        log.debug("Inside service Unique is {}", unique);
        if (unique) {
            return statsRepository.getViewStatsUnique(uris, start, end);
        }
        return statsRepository.getViewStats(uris, start, end);
    }
}
