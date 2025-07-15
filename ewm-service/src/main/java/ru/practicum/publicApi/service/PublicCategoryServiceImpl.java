package ru.practicum.publicApi.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import ru.practicum.admin.exseptions.NotFoundException;
import ru.practicum.admin.mapper.CategoryMapper;
import ru.practicum.admin.model.Category;
import ru.practicum.admin.repository.CategoryRepository;
import ru.practicum.users.dto.CategoryDto;
import org.springframework.data.domain.Pageable;
import java.util.ArrayList;
import java.util.List;

@Service
@Slf4j
@RequiredArgsConstructor
public class PublicCategoryServiceImpl implements PublicCategoriesService{
private final CategoryRepository categoryRepository;

    @Override
    public List<CategoryDto> getAllCategories(Integer from, Integer size) {
        log.info("Получение категорий по параметрам: from={}, size={}", from, size);

        Pageable pageable = (Pageable) PageRequest.of(from / size, size);

        Page<Category> allEvents = categoryRepository.findAll(pageable);
        List<CategoryDto> returnList = new ArrayList<>();

        for(Category c : allEvents){
            returnList.add(CategoryMapper.toCategoryDto(c));
        }

        return returnList;
    }

    @Override
    public CategoryDto getCategoryById(Long id){
        log.info("Получение категорий по id={}", id);

        Category category = categoryRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("Категории с id: " + id +" не существует"));

        return CategoryMapper.toCategoryDto(category);
    }
}
