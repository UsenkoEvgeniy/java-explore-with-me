package ru.practicum.explore_with_me.model.mapper;

import ru.practicum.explore_with_me.event.LocationDto;
import ru.practicum.explore_with_me.model.Location;

public class LocationMapper {
    private LocationMapper() {
    }

    public static Location toLocation(LocationDto dto) {
        Location location = new Location();
        location.setLat(dto.getLat());
        location.setLon(dto.getLon());
        return location;
    }

    public static LocationDto toLocationDto(Location location) {
        LocationDto dto = new LocationDto();
        dto.setLat(location.getLat());
        dto.setLon(location.getLon());
        return dto;
    }
}
