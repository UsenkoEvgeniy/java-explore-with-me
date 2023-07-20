package ru.practicum.explore_with_me.controller.user;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;
import ru.practicum.explore_with_me.event.EventFullDto;
import ru.practicum.explore_with_me.event.EventShortDto;
import ru.practicum.explore_with_me.event.NewEventDto;
import ru.practicum.explore_with_me.event.UpdateEventUserRequest;
import ru.practicum.explore_with_me.service.EventService;

import javax.validation.Valid;
import javax.validation.constraints.Positive;
import javax.validation.constraints.PositiveOrZero;
import java.util.List;

@Validated
@RestController
@Slf4j
@RequestMapping("/users/{userId}/events")
@RequiredArgsConstructor
public class UserEventsController {
    private final EventService eventService;

    @GetMapping
    public List<EventShortDto> getAllEvents(@PathVariable Long userId,
                                            @RequestParam(defaultValue = "0") @PositiveOrZero Integer from,
                                            @RequestParam(defaultValue = "10") @Positive Integer size) {
        log.info("Get request for all events by userId {} from {} size {}", userId, from, size);
        return eventService.getAllUserEvents(userId, from, size);
    }

    @ResponseStatus(HttpStatus.CREATED)
    @PostMapping
    public EventFullDto createEvent(@PathVariable Long userId,
                                    @Valid @RequestBody NewEventDto eventDto) {
        log.info("Post request for event {} from userId {}", eventDto, userId);
        return eventService.create(eventDto, userId);
    }

    @GetMapping("/{eventId}")
    public EventFullDto getFullEventByOwner(@PathVariable Long userId,
                                            @PathVariable Long eventId) {
        log.info("Get request for full eventId {} by userId {}", eventId, userId);
        return eventService.getFullEventByOwner(userId, eventId);
    }

    @PatchMapping("/{eventId}")
    public EventFullDto updateEventByOwner(@Valid @RequestBody UpdateEventUserRequest updateRequest,
                                           @PathVariable Long userId,
                                           @PathVariable Long eventId) {
        log.info("Patch request {} from userId {} for eventId {}", updateRequest, userId, eventId);
        return eventService.updateEventByOwner(updateRequest, userId, eventId);
    }

    @ResponseStatus(HttpStatus.CREATED)
    @PostMapping("/{eventId}/like")
    public void addLike(@PathVariable Long userId,
                        @PathVariable Long eventId,
                        @RequestParam Boolean like) {
        log.info("Post request for like {} from userId {} for eventId {}", like, userId, eventId);
        eventService.addLike(userId, eventId, like);
    }

    @ResponseStatus(HttpStatus.NO_CONTENT)
    @DeleteMapping("/{eventId}/like")
    public void deleteLike(@PathVariable Long userId,
                           @PathVariable Long eventId) {
        log.info("Delete request for like from user {} for event {}", userId, eventId);
        eventService.deleteLike(userId, eventId);
    }
}
