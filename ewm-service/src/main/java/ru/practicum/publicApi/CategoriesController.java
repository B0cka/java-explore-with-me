package ru.practicum.publicApi;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.constraints.Min;
import lombok.RequiredArgsConstructor;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import ru.practicum.StatsClient;
import ru.practicum.publicApi.service.PublicCategoriesService;
import ru.practicum.users.dto.CategoryDto;

import java.util.List;

@RestController
@RequestMapping("/categories")
@RequiredArgsConstructor
@Validated
public class CategoriesController {

    private final StatsClient statsClient;
    private final PublicCategoriesService publicCategoriesService;
    @GetMapping
    public List<CategoryDto> getAllCategories(@RequestParam(defaultValue = "0") @Min(0) Integer from, @RequestParam(defaultValue = "10") @Min(0) Integer size, HttpServletRequest request){

        statsClient.createHit(request);
        return publicCategoriesService.getAllCategories(from, size);
    }

    @GetMapping("/{catId}")
    public CategoryDto getCategoryById(@PathVariable("catId") Long id, HttpServletRequest request){

        statsClient.createHit(request);
        return  publicCategoriesService.getCategoryById(id);

    }
}
