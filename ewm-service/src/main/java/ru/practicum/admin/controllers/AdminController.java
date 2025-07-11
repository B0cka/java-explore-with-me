package ru.practicum.admin.controllers;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.Positive;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import ru.practicum.StatsClient;
import ru.practicum.admin.dto.NewCategoryDto;
import ru.practicum.admin.dto.UserShortDto;
import ru.practicum.admin.dto.ResponseDto;
import ru.practicum.admin.service.AdminService;
import ru.practicum.users.dto.CategoryDto;

import java.util.List;

@RestController
@RequestMapping("/admin")
@RequiredArgsConstructor
@Validated
public class AdminController {

    private final AdminService adminService;
    private final StatsClient statsClient;

    @PostMapping("/users")
    @ResponseStatus(HttpStatus.CREATED)
    public ResponseDto createUser(@Valid @RequestBody UserShortDto userShortDto, HttpServletRequest request) {

        statsClient.createHit(request);
        return adminService.createUser(userShortDto);
    }

    @GetMapping("/users")
    public List<ResponseDto> getUsers(  @RequestParam(required = false) List<Long> ids,
                                        @RequestParam(defaultValue = "0") @Min(0) Long from,
                                        @RequestParam(defaultValue = "10") @Positive Long size,
                                        HttpServletRequest request)
    {

        statsClient.createHit(request);
        return adminService.getUsers(ids, from, size);
    }

    @DeleteMapping("/users/{userId}")
    public String deleteUser(@PathVariable("userId") Long id, HttpServletRequest request){
        statsClient.createHit(request);
        return adminService.deleteUser(id);
    }

    @PostMapping("/categories")
    public CategoryDto createCategory(@RequestBody @Valid NewCategoryDto newCategoryDto, HttpServletRequest request){
        statsClient.createHit(request);
        return adminService.createCategory(newCategoryDto);
    }

    @DeleteMapping("/categories/{catId}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deleteCategory(@PathVariable("catId") Long catId) {
        adminService.deleteCategory(catId);
    }

    @PatchMapping("/{catId}")
    public CategoryDto updateCategory(
            @PathVariable Long catId,
            @RequestBody @Valid NewCategoryDto updateCategoryDto) {
        return adminService.updateCategory(catId, updateCategoryDto);
    }
}
