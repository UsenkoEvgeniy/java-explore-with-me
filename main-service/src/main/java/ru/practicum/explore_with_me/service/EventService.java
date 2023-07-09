package ru.practicum.explore_with_me.service;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.practicum.explore_with_me.StatsClient;
import ru.practicum.explore_with_me.event.EventFullDto;
import ru.practicum.explore_with_me.event.EventShortDto;
import ru.practicum.explore_with_me.event.NewEventDto;
import ru.practicum.explore_with_me.event.UpdateEventAdminRequest;
import ru.practicum.explore_with_me.event.UpdateEventUserRequest;
import ru.practicum.explore_with_me.model.Category;
import ru.practicum.explore_with_me.model.Event;
import ru.practicum.explore_with_me.model.State;
import ru.practicum.explore_with_me.model.User;
import ru.practicum.explore_with_me.model.exceptions.ForbiddenOperationException;
import ru.practicum.explore_with_me.model.exceptions.NotFoundException;
import ru.practicum.explore_with_me.model.mapper.EventMapper;
import ru.practicum.explore_with_me.model.mapper.LocationMapper;
import ru.practicum.explore_with_me.repository.EventRepository;
import ru.practicum.explore_with_me.stats.ViewStats;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.Predicate;
import javax.validation.ValidationException;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import static ru.practicum.explore_with_me.model.State.PUBLISHED;

@Service
@Transactional
@RequiredArgsConstructor
public class EventService {
    private final EventRepository eventRepository;
    private final StatsClient statsClient;

    @Transactional(readOnly = true)
    public List<EventShortDto> getAllUserEvents(Long userId, Integer from, Integer size) {
        return eventRepository.findByInitiator_Id(userId, PageRequest.of(from / size, size)).stream()
                .map(EventMapper::toEventShortDto).collect(Collectors.toList());

    }

    public EventFullDto create(NewEventDto eventDto, Long userId) {
        Event event = EventMapper.newEventToEvent(eventDto);
        event.setCreatedOn(LocalDateTime.now());
        User initiator = new User();
        initiator.setId(userId);
        event.setInitiator(initiator);
        event.setState(State.PENDING);
        event.setConfirmedRequests(0);
        return EventMapper.toEventFullDto(eventRepository.save(event));
    }

    @Transactional(readOnly = true)
    public EventFullDto getFullEventByOwner(Long userId, Long eventId) {
        return EventMapper.toEventFullDto(eventRepository.findByIdAndInitiator_Id(eventId, userId)
                .orElseThrow(() -> new NotFoundException("Event with id=" + eventId + " was not found")));
    }

    public EventFullDto updateEventByOwner(UpdateEventUserRequest updateRequest, Long userId, Long eventId) {
        Event event = eventRepository.findByIdAndInitiator_Id(eventId, userId)
                .orElseThrow(() -> new NotFoundException("Event with id=" + eventId + " was not found"));
        if (event.getState().equals(PUBLISHED)) {
            throw new ForbiddenOperationException("Only pending or canceled events can be changed");
        }
        if (updateRequest.getAnnotation() != null) {
            event.setAnnotation(updateRequest.getAnnotation());
        }
        if (updateRequest.getCategory() != null) {
            Category category = new Category();
            category.setId(updateRequest.getCategory());
            event.setCategory(category);
        }
        if (updateRequest.getDescription() != null) {
            event.setDescription(updateRequest.getDescription());
        }
        if (updateRequest.getEventDate() != null) {
            event.setEventDate(updateRequest.getEventDate());
        }
        if (updateRequest.getLocation() != null) {
            event.setLocation(LocationMapper.toLocation(updateRequest.getLocation()));
        }
        if (updateRequest.getPaid() != null) {
            event.setPaid(updateRequest.getPaid());
        }
        if (updateRequest.getParticipantLimit() != null) {
            event.setParticipantLimit(updateRequest.getParticipantLimit());
        }
        if (updateRequest.getRequestModeration() != null) {
            event.setRequestModeration(updateRequest.getRequestModeration());
        }
        if (updateRequest.getTitle() != null) {
            event.setTitle(updateRequest.getTitle());
        }
        if (updateRequest.getStateAction() != null) {
            if (updateRequest.getStateAction().equals("SEND_TO_REVIEW")) {
                event.setState(State.PENDING);
            } else if (updateRequest.getStateAction().equals("CANCEL_REVIEW")) {
                event.setState(State.CANCELED);
            }
        }
        return EventMapper.toEventFullDto(eventRepository.save(event));
    }

    @Transactional(readOnly = true)
    public List<EventFullDto> getEventsByAdminParams(List<Long> users, List<State> states, List<Long> categories, LocalDateTime rangeStart, LocalDateTime rangeEnd, Integer from, Integer size) {
        Specification<Event> specification = ((root, query, criteriaBuilder) -> {
            List<Predicate> predicates = new ArrayList<>();
            if (users != null) {
                CriteriaBuilder.In<Long> in = criteriaBuilder.in(root.get("initiator"));
                users.forEach(in::value);
                predicates.add(in);
            }
            if (states != null) {
                CriteriaBuilder.In<State> in = criteriaBuilder.in(root.get("state"));
                states.forEach(in::value);
                predicates.add(in);
            }
            if (categories != null) {
                CriteriaBuilder.In<Long> in = criteriaBuilder.in(root.get("category"));
                categories.forEach(in::value);
                predicates.add(in);
            }
            if (rangeStart != null) {
                predicates.add(criteriaBuilder.greaterThan(root.get("eventDate"), rangeStart));
            }
            if (rangeEnd != null) {
                predicates.add(criteriaBuilder.lessThan(root.get("eventDate"), rangeEnd));
            }
            return criteriaBuilder.and(predicates.toArray(Predicate[]::new));
        });
        return eventRepository.findAll(specification, PageRequest.of(from / size, size))
                .map(EventMapper::toEventFullDto).toList();
    }

    public EventFullDto updateEventByAdmin(Long eventId, UpdateEventAdminRequest updateRequest) {
        Event event = eventRepository.findById(eventId)
                .orElseThrow(() -> new NotFoundException("Event with id=" + eventId + " was not found"));
        if (updateRequest.getStateAction() != null && !event.getState().equals(State.PENDING)) {
            throw new ForbiddenOperationException("Only pending events status can be changed, current state: " + event.getState());
        }
        if (updateRequest.getAnnotation() != null) {
            event.setAnnotation(updateRequest.getAnnotation());
        }
        if (updateRequest.getCategory() != null) {
            Category category = new Category();
            category.setId(updateRequest.getCategory());
            event.setCategory(category);
        }
        if (updateRequest.getDescription() != null) {
            event.setDescription(updateRequest.getDescription());
        }
        if (updateRequest.getEventDate() != null) {
            event.setEventDate(updateRequest.getEventDate());
        }
        if (updateRequest.getLocation() != null) {
            event.setLocation(LocationMapper.toLocation(updateRequest.getLocation()));
        }
        if (updateRequest.getPaid() != null) {
            event.setPaid(updateRequest.getPaid());
        }
        if (updateRequest.getParticipantLimit() != null) {
            event.setParticipantLimit(updateRequest.getParticipantLimit());
        }
        if (updateRequest.getRequestModeration() != null) {
            event.setRequestModeration(updateRequest.getRequestModeration());
        }
        if (updateRequest.getTitle() != null) {
            event.setTitle(updateRequest.getTitle());
        }
        if (updateRequest.getStateAction() != null) {
            if (updateRequest.getStateAction().equals("PUBLISH_EVENT")) {
                event.setState(PUBLISHED);
                event.setPublishedOn(LocalDateTime.now());
            } else if (updateRequest.getStateAction().equals("REJECT_EVENT")) {
                event.setState(State.REJECTED);
            }
        }
        return EventMapper.toEventFullDto(eventRepository.save(event));
    }

    @Transactional(readOnly = true)
    public List<EventShortDto> getAllEventsFiltered(String text, List<Long> categories, Boolean paid,
                                                    LocalDateTime rangeStart, LocalDateTime rangeEnd,
                                                    Boolean onlyAvailable, String sort, Integer from, Integer size) {
        LocalDateTime rangeStartChecked = rangeStart == null ? LocalDateTime.now() : rangeStart;
        Specification<Event> specification = ((root, query, criteriaBuilder) -> {
            List<Predicate> predicates = new ArrayList<>();
            predicates.add(criteriaBuilder.equal(root.get("state"), PUBLISHED));

            if (rangeEnd != null && rangeEnd.isBefore(rangeStartChecked)) {
                throw new ValidationException("Start must be before end");
            }
            if (text != null) {
                predicates.add(criteriaBuilder.or(criteriaBuilder.like(criteriaBuilder.lower(root.get("annotation")), "%" + text.toLowerCase() + "%"),
                        criteriaBuilder.like(criteriaBuilder.lower(root.get("description")), "%" + text.toLowerCase() + "%")));
            }
            if (categories != null) {
                CriteriaBuilder.In<Long> in = criteriaBuilder.in(root.get("category"));
                categories.forEach(in::value);
                predicates.add(in);
            }
            if (paid != null) {
                predicates.add(criteriaBuilder.equal(root.get("paid"), paid));
            }
            predicates.add(criteriaBuilder.greaterThan(root.get("eventDate"), rangeStartChecked));
            if (rangeEnd != null) {
                predicates.add(criteriaBuilder.lessThan(root.get("eventDate"), rangeEnd));
            }
            if (onlyAvailable) {
                predicates.add(criteriaBuilder.greaterThan(root.get("confirmedRequests"), root.get("participantLimit")));
            }
            return criteriaBuilder.and(predicates.toArray(Predicate[]::new));
        });
        String sortParam;
        if ("EVENT_DATE".equals(sort)) {
            sortParam = "eventDate";
        } else if ("VIEWS".equals(sort)) {
            sortParam = "views";
        } else {
            sortParam = "id";
        }
        List<Event> result = eventRepository.findAll(specification, PageRequest.of(from / size, size, Sort.by(sortParam))).toList();
        List<String> statReqUri = result.stream().map(event -> "/events/" + event.getId()).collect(Collectors.toList());
        setViews(result, statReqUri);
        return result.stream().map(EventMapper::toEventShortDto).collect(Collectors.toList());
    }

    private void setViews(List<Event> result, List<String> statReqUri) {
        LocalDateTime now = LocalDateTime.now();
        List<ViewStats> rawStats = statsClient.getStats(now.minusYears(25), now, statReqUri, true);
        Map<Long, Integer> stats = rawStats.stream()
                .collect(Collectors.toMap(vs -> Long.parseLong(vs.getUri().replace("/events/", "")), ViewStats::getHits));
        result.forEach(event -> event.setViews(stats.get(event.getId())));
    }

    @Transactional(readOnly = true)
    public EventFullDto getFullPublicEvent(Long eventId) {
        Event event = eventRepository.findByIdAndState(eventId, PUBLISHED)
                .orElseThrow(() -> new NotFoundException("Event with id=" + eventId + " was not found"));
        List<Event> events = List.of(event);
        setViews(events, List.of("/events/" + eventId));
        return EventMapper.toEventFullDto(event);
    }
}
