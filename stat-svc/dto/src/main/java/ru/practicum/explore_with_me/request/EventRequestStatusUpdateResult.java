package ru.practicum.explore_with_me.request;

import lombok.Data;

import java.util.List;

@Data
public class EventRequestStatusUpdateResult {
    private List<ParticipationRequestDto> confirmedRequests;
    private List<ParticipationRequestDto> rejectedRequests;
}
