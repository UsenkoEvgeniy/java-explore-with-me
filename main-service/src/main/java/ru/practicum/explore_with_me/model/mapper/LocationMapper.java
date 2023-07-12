package ru.practicum.explore_with_me.model.mapper;

import lombok.experimental.UtilityClass;
import ru.practicum.explore_with_me.event.LocationDto;
import ru.practicum.explore_with_me.model.Location;

@UtilityClass
public class LocationMapper {

    public Location toLocation(LocationDto dto) {
        Location location = new Location();
        location.setLat(dto.getLat());
        location.setLon(dto.getLon());
        return location;
    }

    public LocationDto toLocationDto(Location location) {
        LocationDto dto = new LocationDto();
        dto.setLat(location.getLat());
        dto.setLon(location.getLon());
        return dto;
    }
}
