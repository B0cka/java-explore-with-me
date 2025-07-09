package ru.practicum.admin.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Data
public class RequestDto {

    @NotBlank
    private String email;

    @Email
    @NotBlank
    private String name;

}
