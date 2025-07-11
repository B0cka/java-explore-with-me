package ru.practicum.users.dto;

import lombok.*;
import ru.practicum.admin.dto.LocationDto;
import ru.practicum.admin.dto.UserShortestDto;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class EventShortDto {
    private String annotation;
    private CategoryDto category;
    private Long confirmedRequests;
    private String createdOn;
    private String description;
    private String eventDate;
    private UserShortestDto initiator;
    private LocationDto location;
    private Boolean paid;
    private Integer participantLimit;
    private Boolean requestModeration;
    private String title;
    private String state;
}