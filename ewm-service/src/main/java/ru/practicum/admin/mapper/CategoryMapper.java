package ru.practicum.admin.mapper;

import lombok.experimental.UtilityClass;
import ru.practicum.admin.dto.NewCategoryDto;
import ru.practicum.admin.model.Category;
import ru.practicum.users.dto.CategoryDto;

@UtilityClass
public class CategoryMapper {
    public Category toCategory(CategoryDto categoryDto) {
        return Category.builder()
                .name(categoryDto.getName())
                .build();
    }

    public Category toNewCategoryDto(NewCategoryDto newCategoryDto) {
        return Category.builder()
                .name(newCategoryDto.getName())
                .build();
    }

    public CategoryDto toCategoryDto(Category category) {
        return CategoryDto.builder()
                .id(category.getId())
                .name(category.getName())
                .build();
    }
}