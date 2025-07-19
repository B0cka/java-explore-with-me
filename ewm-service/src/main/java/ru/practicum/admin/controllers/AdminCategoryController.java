package ru.practicum.admin.controllers;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import jakarta.validation.constraints.Min;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import ru.practicum.StatsClient;
import ru.practicum.admin.dto.NewCategoryDto;
import ru.practicum.admin.service.CategoryService;
import ru.practicum.users.dto.CategoryDto;

@Validated
@RestController
@RequestMapping(path = "/admin/categories")
@RequiredArgsConstructor
public class AdminCategoryController {
    private final CategoryService categoryService;
    private final StatsClient statsClient;


    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public CategoryDto createCategory(@RequestBody @Valid NewCategoryDto newCategoryDto, HttpServletRequest request) {

        statsClient.createHit(request);
        return categoryService.createCategory(newCategoryDto);
    }

    @DeleteMapping("/{catId}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deleteCategory(@PathVariable Long catId, HttpServletRequest request) {
        statsClient.createHit(request);
        categoryService.deleteCategoryById(catId);
    }

    @PatchMapping("/{catId}")
    public CategoryDto updateCategory(@PathVariable(value = "catId") @Min(1) Long catId,
                                      @RequestBody @Valid CategoryDto categoryDto, HttpServletRequest request) {
        statsClient.createHit(request);
        return categoryService.updateCategory(catId, categoryDto);
    }
}