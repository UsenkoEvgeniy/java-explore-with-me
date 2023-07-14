package ru.practicum.explore_with_me.controller.all;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import ru.practicum.explore_with_me.StatsClient;
import ru.practicum.explore_with_me.event.EventFullDto;
import ru.practicum.explore_with_me.event.EventShortDto;
import ru.practicum.explore_with_me.service.EventService;
import ru.practicum.explore_with_me.stats.EndpointHitDto;

import javax.servlet.http.HttpServletRequest;
import javax.validation.constraints.Positive;
import javax.validation.constraints.PositiveOrZero;
import java.time.LocalDateTime;
import java.util.List;

import static ru.practicum.explore_with_me.DateConstant.DATE_TIME_PATTERN;

@Validated
@RestController
@Slf4j
@RequestMapping("/events")
@RequiredArgsConstructor
public class EventController {
    private final EventService eventService;
    private final StatsClient statsClient;

    @GetMapping
    public List<EventShortDto> getEvents(@RequestParam(required = false) String text,
                                         @RequestParam(required = false) List<Long> categories,
                                         @RequestParam(required = false) Boolean paid,
                                         @RequestParam(required = false) @DateTimeFormat(pattern = DATE_TIME_PATTERN) LocalDateTime rangeStart,
                                         @RequestParam(required = false) @DateTimeFormat(pattern = DATE_TIME_PATTERN) LocalDateTime rangeEnd,
                                         @RequestParam(defaultValue = "false") Boolean onlyAvailable,
                                         @RequestParam(required = false) String sort,
                                         @RequestParam(defaultValue = "0") @PositiveOrZero Integer from,
                                         @RequestParam(defaultValue = "10") @Positive Integer size,
                                         HttpServletRequest request) {
        log.info("Get request with params text={} categories={} paid={} rangeStart={} rangeEnd={} " +
                        "onlyAvailable={} sort={} from={} size={}", text, categories, paid, rangeStart, rangeEnd,
                onlyAvailable, sort, from, size);
        List<EventShortDto> result = eventService.getAllEventsFiltered(text, categories, paid, rangeStart, rangeEnd,
                onlyAvailable, sort, from, size);
        EndpointHitDto hit = new EndpointHitDto(request.getRequestURI(), request.getRemoteAddr(), LocalDateTime.now());
        log.info("Saving hit {}", hit);
        statsClient.saveHit(hit);
        return result;
    }

    @GetMapping("/{eventId}")
    public EventFullDto getPublicFullEvent(@PathVariable Long eventId,
                                           HttpServletRequest request) {
        log.info("Get request for eventId {}", eventId);
        EventFullDto result = eventService.getFullPublicEvent(eventId);
        EndpointHitDto hit = new EndpointHitDto(request.getRequestURI(), request.getRemoteAddr(), LocalDateTime.now());
        log.info("Saving hit {}", hit);
        statsClient.saveHit(hit);
        return result;
    }
}
