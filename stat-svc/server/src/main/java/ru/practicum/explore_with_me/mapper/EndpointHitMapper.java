package ru.practicum.explore_with_me.mapper;

import ru.practicum.explore_with_me.EndpointHitDto;
import ru.practicum.explore_with_me.model.EndpointHit;

public class EndpointHitMapper {
    public static EndpointHit mapToEndpointHit(EndpointHitDto dto) {
        return new EndpointHit(null, dto.getApp(), dto.getUri(), dto.getIp(), dto.getTimestamp());
    }
}
