package ru.practicum.admin.dto;

import lombok.*;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class LocationDto {
    private Double lat;
    private Double lon;
}