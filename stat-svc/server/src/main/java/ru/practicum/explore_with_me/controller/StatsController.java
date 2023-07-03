package ru.practicum.explore_with_me.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;
import ru.practicum.explore_with_me.EndpointHitDto;
import ru.practicum.explore_with_me.ViewStats;
import ru.practicum.explore_with_me.service.StatsService;

import javax.validation.Valid;
import java.time.LocalDateTime;
import java.util.List;

@RestController
@Slf4j
@RequiredArgsConstructor
public class StatsController {

    private final StatsService statsService;

    @PostMapping("/hit")
    @ResponseStatus(HttpStatus.CREATED)
    public void saveHit(@Valid @RequestBody EndpointHitDto hit) {
        log.info("Post with {}", hit);
        statsService.saveHit(hit);
    }

    @GetMapping("/stats")
    public List<ViewStats> getStats(@RequestParam @DateTimeFormat(pattern = EndpointHitDto.DATE_TIME_PATTERN) LocalDateTime start,
                                       @RequestParam @DateTimeFormat(pattern = EndpointHitDto.DATE_TIME_PATTERN) LocalDateTime end,
                                       @RequestParam (required = false) List<String> uris,
                                       @RequestParam (defaultValue = "false") boolean unique) {
        log.info("Get request with start {} end {} unique {} uris {}", start, end, unique, uris);
        return statsService.getStats(start, end, uris, unique);
    }
}
