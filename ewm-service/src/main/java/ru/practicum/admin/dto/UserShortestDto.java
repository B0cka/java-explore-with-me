package ru.practicum.admin.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Data
public class UserShortestDto {

    @NotBlank
    private Long id;

    @Email
    @NotBlank
    private String name;

}
