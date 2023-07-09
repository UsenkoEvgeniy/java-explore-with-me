package ru.practicum.explore_with_me.model.mapper;

import ru.practicum.explore_with_me.model.Request;
import ru.practicum.explore_with_me.request.ParticipationRequestDto;

public class RequestMapper {
    private RequestMapper() {
    }

    public static ParticipationRequestDto toParticipationRequestDto(Request request) {
        ParticipationRequestDto dto = new ParticipationRequestDto();
        dto.setId(request.getId());
        dto.setCreated(request.getCreated());
        dto.setEvent(request.getEvent().getId());
        dto.setRequester(request.getRequester().getId());
        dto.setStatus(request.getStatus().toString());
        return dto;
    }
}
