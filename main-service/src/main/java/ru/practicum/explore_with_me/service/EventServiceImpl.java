package ru.practicum.explore_with_me.service;

import lombok.RequiredArgsConstructor;
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
import ru.practicum.explore_with_me.repository.UserRepository;
import ru.practicum.explore_with_me.stats.ViewStats;
import ru.practicum.explore_with_me.utils.CustomPage;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.Predicate;
import javax.validation.ValidationException;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import static ru.practicum.explore_with_me.model.State.PUBLISHED;

@Service
@Transactional
@RequiredArgsConstructor
public class EventServiceImpl implements EventService {
    private final EventRepository eventRepository;
    private final UserRepository userRepository;
    private final StatsClient statsClient;

    @Override
    @Transactional(readOnly = true)
    public List<EventShortDto> getAllUserEvents(Long userId, Integer from, Integer size) {
        CustomPage pageable = new CustomPage(from, size);
        return eventRepository.findByInitiator_Id(userId, pageable).stream()
                .map(EventMapper::toEventShortDto).collect(Collectors.toList());

    }

    @Override
    public EventFullDto create(NewEventDto eventDto, Long userId) {
        Event event = EventMapper.newEventToEvent(eventDto);
        event.setCreatedOn(LocalDateTime.now());
        User initiator = userRepository.findById(userId)
                .orElseThrow(() -> new NotFoundException("User", userId));
        event.setInitiator(initiator);
        event.setState(State.PENDING);
        event.setConfirmedRequests(0);
        return EventMapper.toEventFullDto(eventRepository.save(event));
    }

    @Override
    @Transactional(readOnly = true)
    public EventFullDto getFullEventByOwner(Long userId, Long eventId) {
        return EventMapper.toEventFullDto(eventRepository.findByIdAndInitiator_Id(eventId, userId)
                .orElseThrow(() -> new NotFoundException("Event", eventId)));
    }

    @Override
    public EventFullDto updateEventByOwner(UpdateEventUserRequest updateRequest, Long userId, Long eventId) {
        Event event = eventRepository.findByIdAndInitiator_Id(eventId, userId)
                .orElseThrow(() -> new NotFoundException("Event", eventId));
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

    @Override
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
        CustomPage pageable = new CustomPage(from, size);
        return eventRepository.findAll(specification, pageable)
                .map(EventMapper::toEventFullDto).toList();
    }

    @Override
    public EventFullDto updateEventByAdmin(Long eventId, UpdateEventAdminRequest updateRequest) {
        Event event = eventRepository.findById(eventId)
                .orElseThrow(() -> new NotFoundException("Event", eventId));
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

    @Override
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
        if (sort != null) {
            switch (sort) {
                case "EVENT_DATE":
                    sortParam = "eventDate";
                    break;
                case "VIEWS":
                    sortParam = "views";
                    break;
                default:
                    sortParam = "id";
            }
        } else {
            sortParam = "id";
        }
        CustomPage pageable = new CustomPage(from, size);
        List<Event> result = eventRepository.findAll(specification, pageable.withSort(Sort.by(sortParam))).toList();
        return result.stream().map(EventMapper::toEventShortDto).collect(Collectors.toList());
    }

    @Override
    public EventFullDto getFullPublicEvent(Long eventId) {
        Event event = eventRepository.findByIdAndState(eventId, PUBLISHED)
                .orElseThrow(() -> new NotFoundException("Event", eventId));
        LocalDateTime now = LocalDateTime.now();
        List<ViewStats> rawStats = statsClient.getStats(now.minusYears(25), now, List.of("/events/" + eventId), true);
        if (rawStats.isEmpty()) {
            event.setViews(1);
        } else {
            event.setViews(rawStats.get(0).getHits());
        }
        return EventMapper.toEventFullDto(event);
    }
}
