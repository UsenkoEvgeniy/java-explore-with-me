package ru.practicum.explore_with_me.model.mapper;

import lombok.experimental.UtilityClass;
import ru.practicum.explore_with_me.event.EventFullDto;
import ru.practicum.explore_with_me.event.EventShortDto;
import ru.practicum.explore_with_me.event.NewEventDto;
import ru.practicum.explore_with_me.model.Category;
import ru.practicum.explore_with_me.model.Event;

@UtilityClass
public class EventMapper {

    public Event newEventToEvent(NewEventDto dto) {
        Event event = new Event();
        event.setAnnotation(dto.getAnnotation());
        Category category = new Category();
        category.setId(dto.getCategory());
        event.setCategory(category);
        event.setDescription(dto.getDescription());
        event.setEventDate(dto.getEventDate());
        event.setLocation(LocationMapper.toLocation(dto.getLocation()));
        event.setPaid(dto.getPaid());
        event.setParticipantLimit(dto.getParticipantLimit());
        event.setRequestModeration(dto.getRequestModeration());
        event.setTitle(dto.getTitle());
        return event;
    }

    public EventFullDto toEventFullDto(Event event) {
        EventFullDto dto = new EventFullDto();
        dto.setId(event.getId());
        dto.setAnnotation(event.getAnnotation());
        dto.setCategory(CategoryMapper.toCategoryDto(event.getCategory()));
        dto.setConfirmedRequests(event.getConfirmedRequests());
        dto.setCreatedOn(event.getCreatedOn());
        dto.setDescription(event.getDescription());
        dto.setEventDate(event.getEventDate());
        dto.setInitiator(UserMapper.toUserShortDto(event.getInitiator()));
        dto.setLocation(LocationMapper.toLocationDto(event.getLocation()));
        dto.setPaid(event.getPaid());
        dto.setParticipantLimit(event.getParticipantLimit());
        dto.setPublishedOn(event.getPublishedOn());
        dto.setRequestModeration(event.getRequestModeration());
        dto.setState(event.getState().toString());
        dto.setTitle(event.getTitle());
        dto.setViews(event.getViews());
        return dto;
    }

    public EventShortDto toEventShortDto(Event event) {
        EventShortDto dto = new EventShortDto();
        dto.setId(event.getId());
        dto.setAnnotation(event.getAnnotation());
        dto.setCategory(CategoryMapper.toCategoryDto(event.getCategory()));
        dto.setConfirmedRequests(event.getConfirmedRequests());
        dto.setEventDate(event.getEventDate());
        dto.setInitiator(UserMapper.toUserShortDto(event.getInitiator()));
        dto.setPaid(event.getPaid());
        dto.setTitle(event.getTitle());
        dto.setViews(event.getViews());
        return dto;
    }
}
