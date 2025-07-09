package ru.practicum.admin.controllers;

import jakarta.validation.Valid;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.Positive;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import ru.practicum.admin.dto.RequestDto;
import ru.practicum.admin.dto.ResponseDto;
import ru.practicum.admin.service.AdminService;

import java.util.List;

@RestController
@RequestMapping("/admin")
@RequiredArgsConstructor
@Validated
public class AdminController {

    private AdminService adminService;

    @PostMapping("/users")
    @ResponseStatus(HttpStatus.CREATED)
    public ResponseDto createUser(@Valid @RequestBody RequestDto requestDto) {
        return adminService.createUser(requestDto);
    }

    @GetMapping("/users")
    public List<ResponseDto> getUsers(@RequestParam(required = false) List<Long> ids,
                               @RequestParam(defaultValue = "0") @Min(0) Long from,
                               @RequestParam(defaultValue = "10") @Positive Long size
    ) {
        return adminService.getUsers(ids, from, size);
    }
}
