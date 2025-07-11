package ru.practicum.admin.mapper;

import org.springframework.stereotype.Component;
import ru.practicum.admin.model.Category;
import ru.practicum.users.dto.CategoryDto;

@Component
public class CategoryMapper {

    public Category toEntity(CategoryDto categoryDto){

        return Category.builder()
                .name(categoryDto.getName())
                .id(categoryDto.getId())
                .build();

    }

    public CategoryDto toDto(Category category){

        return CategoryDto.builder()
                .name(category.getName())
                .id(category.getId())
                .build();

    }

}
