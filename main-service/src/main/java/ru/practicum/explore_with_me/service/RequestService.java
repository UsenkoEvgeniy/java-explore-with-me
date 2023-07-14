package ru.practicum.explore_with_me.service;

import ru.practicum.explore_with_me.request.EventRequestStatusUpdateRequest;
import ru.practicum.explore_with_me.request.EventRequestStatusUpdateResult;
import ru.practicum.explore_with_me.request.ParticipationRequestDto;

import java.util.List;

public interface RequestService {
    List<ParticipationRequestDto> getRequestByOwner(Long userId, Long eventId);

    EventRequestStatusUpdateResult updateRequestStatusByEventOwner(Long userId, Long eventId, EventRequestStatusUpdateRequest request);

    List<ParticipationRequestDto> getUserRequests(Long userId);

    ParticipationRequestDto createRequest(Long userId, Long eventId);

    ParticipationRequestDto cancelRequest(Long userId, Long requestId);
}
