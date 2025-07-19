package ru.practicum.publicApi.service;

import ru.practicum.users.dto.CategoryDto;

import java.util.List;

public interface PublicCategoriesService {

    List<CategoryDto> getAllCategories(Integer from, Integer size);

    CategoryDto getCategoryById(Long id);

}
