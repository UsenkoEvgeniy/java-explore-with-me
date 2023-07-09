package ru.practicum.explore_with_me.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
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
import ru.practicum.explore_with_me.request.EventRequestStatusUpdateRequest;
import ru.practicum.explore_with_me.request.EventRequestStatusUpdateResult;
import ru.practicum.explore_with_me.request.ParticipationRequestDto;
import ru.practicum.explore_with_me.service.EventService;
import ru.practicum.explore_with_me.service.RequestService;

import javax.validation.Valid;
import java.util.List;

@RestController
@Slf4j
@RequestMapping("/users")
@RequiredArgsConstructor
public class UserController {
    private final EventService eventService;
    private final RequestService requestService;

    @GetMapping("/{userId}/events")
    public List<EventShortDto> getAllEvents(@PathVariable Long userId,
                                            @RequestParam(defaultValue = "0") Integer from,
                                            @RequestParam(defaultValue = "10") Integer size) {
        log.info("Get request for all events by userId {} from {} size {}", userId, from, size);
        return eventService.getAllUserEvents(userId, from, size);
    }

    @ResponseStatus(HttpStatus.CREATED)
    @PostMapping("/{userId}/events")
    public EventFullDto createEvent(@PathVariable Long userId,
                                    @Valid @RequestBody NewEventDto eventDto) {
        log.info("Post request for event {} from userId {}", eventDto, userId);
        return eventService.create(eventDto, userId);
    }

    @GetMapping("/{userId}/events/{eventId}")
    public EventFullDto getFullEventByOwner(@PathVariable Long userId,
                                            @PathVariable Long eventId) {
        log.info("Get request for full eventId {} by userId {}", eventId, userId);
        return eventService.getFullEventByOwner(userId, eventId);
    }

    @PatchMapping("/{userId}/events/{eventId}")
    public EventFullDto updateEventByOwner(@Valid @RequestBody UpdateEventUserRequest updateRequest,
                                           @PathVariable Long userId,
                                           @PathVariable Long eventId) {
        log.info("Patch request {} from userId {} for eventId {}", updateRequest, userId, eventId);
        return eventService.updateEventByOwner(updateRequest, userId, eventId);
    }

    @GetMapping("/{userId}/events/{eventId}/requests")
    public List<ParticipationRequestDto> getRequestByOwner(@PathVariable Long userId,
                                                           @PathVariable Long eventId) {
        log.info("Get request by ownerId {} eventId {}", userId, eventId);
        return requestService.getRequestByOwner(userId, eventId);
    }

    @PatchMapping("/{userId}/events/{eventId}/requests")
    public EventRequestStatusUpdateResult updateRequestsStatus(@PathVariable Long userId,
                                                               @PathVariable Long eventId,
                                                               @RequestBody EventRequestStatusUpdateRequest request) {
        log.info("Patch request {} for requests status from {} event {}", request, userId, eventId);
        return requestService.updateRequestStatusByEventOwner(userId, eventId, request);
    }

    @GetMapping("/{userId}/requests")
    public List<ParticipationRequestDto> getUserRequests(@PathVariable Long userId) {
        log.info("Get request userId {} requests", userId);
        return requestService.getUserRequests(userId);
    }

    @ResponseStatus(HttpStatus.CREATED)
    @PostMapping("/{userId}/requests")
    public ParticipationRequestDto createRequest(@PathVariable Long userId,
                                                 @RequestParam Long eventId) {
        log.info("Post request from user {} to participate in event {}", userId, eventId);
        return requestService.createRequest(userId, eventId);
    }

    @PatchMapping("/{userId}/requests/{requestId}/cancel")
    public ParticipationRequestDto cancelRequest(@PathVariable Long userId,
                                                 @PathVariable Long requestId) {
        log.info("Patch request to cancel {} by user {}", requestId, userId);
        return requestService.cancelRequest(userId, requestId);
    }
}
