package ru.practicum.admin.mapper;

import lombok.experimental.UtilityClass;
import ru.practicum.admin.dto.LocationDto;
import ru.practicum.admin.model.Location;

@UtilityClass
public class LocationMapper {

    public Location toLocation(LocationDto locationDto) {
        return Location.builder()
                .lat(locationDto.getLat())
                .lon(locationDto.getLon())
                .build();
    }

    public LocationDto toLocationDto(Location location) {
        return LocationDto.builder()
                .lat(location.getLat())
                .lon(location.getLon())
                .build();
    }

}
