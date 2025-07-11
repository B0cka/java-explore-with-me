package ru.practicum.admin.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Data
public class UserShortDto {

    @Email
    @NotBlank
    private String email;

    @NotBlank
    private String name;

}
