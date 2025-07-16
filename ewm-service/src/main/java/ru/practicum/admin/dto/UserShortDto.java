package ru.practicum.admin.dto;

import lombok.*;
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class UserShortDto {

    private Long id;
    private String name;
}