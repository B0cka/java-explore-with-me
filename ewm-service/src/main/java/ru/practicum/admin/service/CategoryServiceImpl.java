package ru.practicum.admin.service;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import ru.practicum.admin.dto.NewCategoryDto;
import ru.practicum.admin.exseptions.ConflictException;
import ru.practicum.admin.exseptions.NotFoundException;
import ru.practicum.admin.mapper.CategoryMapper;
import ru.practicum.admin.model.Category;
import ru.practicum.admin.repository.CategoryRepository;
import ru.practicum.admin.repository.EventRepository;
import ru.practicum.users.dto.CategoryDto;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Slf4j
@Service
@RequiredArgsConstructor
public class CategoryServiceImpl implements CategoryService {

    private final CategoryRepository categoryRepository;
    private final EventRepository eventRepository;

    @Override
    public CategoryDto createCategory(NewCategoryDto newCategoryDto) {
        log.info("Создание категории с name={}", newCategoryDto.getName());

        if (categoryRepository.existsByName(newCategoryDto.getName())) {
            throw new ConflictException("Категория с таким именем уже существует");
        }

        Category category = CategoryMapper.toNewCategoryDto(newCategoryDto);
        Category saveCategory = categoryRepository.save(category);
        return CategoryMapper.toCategoryDto(saveCategory);
    }

    @Override
    @Transactional
    public void deleteCategoryById(Long catId) {
        log.info("Удаление категории по id={}", catId);
        if (!categoryRepository.existsById(catId)) {
            throw new NotFoundException("Category with id=" + catId + " was not found");
        }

        if (eventRepository.existsByCategoryId(catId)) {
            throw new ConflictException("The category is not empty");
        }

        categoryRepository.deleteById(catId);
    }

    @Override
    @Transactional
    public CategoryDto updateCategory(Long catId, CategoryDto categoryDto) {
        log.info("Обновление категории по таким параметрам: id={}, dto={}",catId, categoryDto);
        Category category = categoryRepository.findById(catId)
                .orElseThrow(() -> new NotFoundException("Category with id=" + catId + " was not found"));

        if (categoryRepository.existsByNameAndIdNot(categoryDto.getName(), catId)) {
            throw new ConflictException("Category with this name already exists");
        }

        category.setName(categoryDto.getName());
        Category updated = categoryRepository.save(category);
        return CategoryMapper.toCategoryDto(updated);
    }

    @Override
    public List<CategoryDto> getCategories(Integer from, Integer size) {
        log.info("Получение категорий по from={}, size={}", from, size);

        PageRequest pageRequest = PageRequest.of(from / size, size, Sort.by(Sort.Direction.DESC, "id"));
        return categoryRepository.findAll(pageRequest)
                .stream().map(CategoryMapper::toCategoryDto)
                .collect(Collectors.toList());
    }

    @Override
    public CategoryDto getCategoryById(Long catId) {
        log.info("Получение категорий по id={}", catId);
        Optional<Category> category = categoryRepository.findById(catId);

        if (category.isEmpty()) {
            throw new NotFoundException("Category with id=" + catId + " was not found");
        }

        return CategoryMapper.toCategoryDto(category.orElse(null));
    }

}
