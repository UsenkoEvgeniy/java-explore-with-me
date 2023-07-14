package ru.practicum.explore_with_me.service;

import ru.practicum.explore_with_me.event.EventFullDto;
import ru.practicum.explore_with_me.event.EventShortDto;
import ru.practicum.explore_with_me.event.NewEventDto;
import ru.practicum.explore_with_me.event.UpdateEventAdminRequest;
import ru.practicum.explore_with_me.event.UpdateEventUserRequest;
import ru.practicum.explore_with_me.model.State;

import java.time.LocalDateTime;
import java.util.List;

public interface EventService {
    List<EventShortDto> getAllUserEvents(Long userId, Integer from, Integer size);

    EventFullDto create(NewEventDto eventDto, Long userId);

    EventFullDto getFullEventByOwner(Long userId, Long eventId);

    EventFullDto updateEventByOwner(UpdateEventUserRequest updateRequest, Long userId, Long eventId);

    List<EventFullDto> getEventsByAdminParams(List<Long> users, List<State> states, List<Long> categories, LocalDateTime rangeStart, LocalDateTime rangeEnd, Integer from, Integer size);

    EventFullDto updateEventByAdmin(Long eventId, UpdateEventAdminRequest updateRequest);

    List<EventShortDto> getAllEventsFiltered(String text, List<Long> categories, Boolean paid,
                                             LocalDateTime rangeStart, LocalDateTime rangeEnd,
                                             Boolean onlyAvailable, String sort, Integer from, Integer size);

    EventFullDto getFullPublicEvent(Long eventId);
}
