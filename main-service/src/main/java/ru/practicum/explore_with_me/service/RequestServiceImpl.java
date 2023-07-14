package ru.practicum.explore_with_me.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.practicum.explore_with_me.model.Event;
import ru.practicum.explore_with_me.model.Request;
import ru.practicum.explore_with_me.request.RequestState;
import ru.practicum.explore_with_me.model.State;
import ru.practicum.explore_with_me.model.User;
import ru.practicum.explore_with_me.model.exceptions.ForbiddenOperationException;
import ru.practicum.explore_with_me.model.exceptions.NotFoundException;
import ru.practicum.explore_with_me.model.mapper.RequestMapper;
import ru.practicum.explore_with_me.repository.EventRepository;
import ru.practicum.explore_with_me.repository.RequestRepository;
import ru.practicum.explore_with_me.repository.UserRepository;
import ru.practicum.explore_with_me.request.EventRequestStatusUpdateRequest;
import ru.practicum.explore_with_me.request.EventRequestStatusUpdateResult;
import ru.practicum.explore_with_me.request.ParticipationRequestDto;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

import static ru.practicum.explore_with_me.model.State.*;

@Service
@Transactional
@RequiredArgsConstructor
public class RequestServiceImpl implements RequestService {
    private final RequestRepository requestRepository;
    private final EventRepository eventRepository;
    private final UserRepository userRepository;

    @Override
    @Transactional(readOnly = true)
    public List<ParticipationRequestDto> getRequestByOwner(Long userId, Long eventId) {
        List<Request> requests = requestRepository.findByEvent_IdAndEvent_Initiator_Id(eventId, userId);
        return requests.stream().map(RequestMapper::toParticipationRequestDto).collect(Collectors.toList());
    }

    @Override
    public EventRequestStatusUpdateResult updateRequestStatusByEventOwner(Long userId, Long eventId, EventRequestStatusUpdateRequest request) {
        Event event = eventRepository.findByIdAndInitiator_Id(eventId, userId)
                .orElseThrow(() -> new NotFoundException("Event", eventId));
        if (event.getParticipantLimit() > 0 && event.getParticipantLimit() <= event.getConfirmedRequests()) {
            throw new ForbiddenOperationException("The participant limit has been reached");
        }
        List<Request> requests = requestRepository.findByIdInAndEvent_IdAndEvent_Initiator_IdAndStatus(request.getRequestIds(),
                eventId, userId, PENDING);
        EventRequestStatusUpdateResult result = new EventRequestStatusUpdateResult();
        List<Request> confirmed = new ArrayList<>();
        List<Request> rejected = new ArrayList<>();
        if (RequestState.REJECTED.equals(request.getStatus())) {
            requests.forEach(r -> r.setStatus(State.REJECTED));
            rejected = requests;
        }
        if (RequestState.CONFIRMED.equals(request.getStatus())) {
            for (Request req : requests) {
                if (event.getParticipantLimit() > 0 && event.getParticipantLimit() > event.getConfirmedRequests()) {
                    req.setStatus(CONFIRMED);
                    event.setConfirmedRequests(event.getConfirmedRequests() + 1);
                    confirmed.add(req);
                } else {
                    req.setStatus(REJECTED);
                    rejected.add(req);
                }
            }
        }
        eventRepository.save(event);
        requestRepository.saveAll(requests);
        result.setConfirmedRequests(confirmed.stream().map(RequestMapper::toParticipationRequestDto).collect(Collectors.toList()));
        result.setRejectedRequests(rejected.stream().map(RequestMapper::toParticipationRequestDto).collect(Collectors.toList()));
        return result;
    }

    @Override
    @Transactional(readOnly = true)
    public List<ParticipationRequestDto> getUserRequests(Long userId) {
        if (!userRepository.existsById(userId)) {
            throw new NotFoundException("User", userId);
        }
        List<Request> requests = requestRepository.findByRequester_Id(userId);
        return requests.stream().map(RequestMapper::toParticipationRequestDto).collect(Collectors.toList());
    }

    @Override
    public ParticipationRequestDto createRequest(Long userId, Long eventId) {
        Event event = eventRepository.findById(eventId)
                .orElseThrow(() -> new NotFoundException("Event", eventId));
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new NotFoundException("User", userId));
        if (user.getId().equals(event.getInitiator().getId())) {
            throw new ForbiddenOperationException("Initiator can't send request to his event");
        }
        if (!event.getState().equals(PUBLISHED)) {
            throw new ForbiddenOperationException("Can't participate in unpublished event");
        }
        if (event.getParticipantLimit() != 0 && event.getParticipantLimit() <= event.getConfirmedRequests()) {
            throw new ForbiddenOperationException("Participant limit has been reached");
        }
        Request request = new Request();
        request.setCreated(LocalDateTime.now());
        request.setEvent(event);
        request.setRequester(user);
        if (event.getRequestModeration() && event.getParticipantLimit() != 0) {
            request.setStatus(PENDING);
        } else {
            request.setStatus(CONFIRMED);
            event.setConfirmedRequests(event.getConfirmedRequests() + 1);
            eventRepository.save(event);
        }
        return RequestMapper.toParticipationRequestDto(requestRepository.save(request));
    }

    @Override
    public ParticipationRequestDto cancelRequest(Long userId, Long requestId) {
        Request request = requestRepository.findById(requestId)
                .orElseThrow(() -> new NotFoundException("Request", requestId));
        if (!Objects.equals(request.getRequester().getId(), userId)) {
            throw new ForbiddenOperationException("User " + userId + "is not the requester");
        }
        request.setStatus(CANCELED);
        return RequestMapper.toParticipationRequestDto(request);
    }
}
