package ru.practicum.admin.service;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import ru.practicum.admin.dto.NewCategoryDto;
import ru.practicum.admin.dto.UserShortDto;
import ru.practicum.admin.dto.ResponseDto;
import ru.practicum.admin.exseptions.ConflictException;
import ru.practicum.admin.exseptions.NotFoundException;
import ru.practicum.admin.mapper.CategoryMapper;
import ru.practicum.admin.model.Category;
import ru.practicum.admin.model.User;
import ru.practicum.admin.repository.AdminRepository;
import ru.practicum.admin.repository.CategoryRepository;
import ru.practicum.users.dto.CategoryDto;
import ru.practicum.users.repository.EventRepository;

import java.util.ArrayList;
import java.util.List;

@Service
@Slf4j
@RequiredArgsConstructor
public class AdminServiceImpl implements AdminService {

    private final AdminRepository adminRepository;
    private final CategoryRepository categoryRepository;
    private final CategoryMapper categoryMapper;
    private final EventRepository eventRepository;
    @Override
    public ResponseDto createUser(UserShortDto userShortDto) {
        String name = userShortDto.getName();
        String email = userShortDto.getEmail();

        log.info("Создание пользователя с email={}, name={}", email, name);

        User user = User.builder()
                .email(email)
                .name(name)
                .build();

        adminRepository.save(user);
        return ResponseDto.builder()
                .id(user.getId())
                .email(user.getEmail())
                .name(user.getName())
                .build();
    }

    @Override
    public List<ResponseDto> getUsers(List<Long> ids, Long from, Long size) {
        log.info("Запрос с ids={}, from={}, size={}", ids, from, size);

        List<User> foundUsers = adminRepository.findAllById(ids);
        if (foundUsers.size() != ids.size()) {
            throw new NotFoundException("Некоторые пользователи не найдены");
        }

        List<User> users = adminRepository.getUsersNative(ids, from, size);
        List<ResponseDto> responseDto = new ArrayList<>();

        for (User u : users) {
            responseDto.add(
                    ResponseDto.builder()
                            .id(u.getId())
                            .name(u.getName())
                            .email(u.getEmail())
                            .build()
            );
        }
        return responseDto;
    }

    @Override
    public String deleteUser(Long id){
        log.info("Удаление пользователя с id={}", id);

        if(adminRepository.findById(id).isEmpty()){
            throw new NotFoundException("User with id=" + id + " was not found");
        }

        adminRepository.deleteById(id);
        return "Пользователь удален";
    }

    @Override
    public CategoryDto createCategory(NewCategoryDto newCategoryDto) {
        log.info("Создание категории с name={}", newCategoryDto.getName());

        if (categoryRepository.existsByName(newCategoryDto.getName())) {
            throw new ConflictException("Категория с таким именем уже существует");
        }

        Category category = Category.builder()
                .name(newCategoryDto.getName())
                .build();

        Category saved = categoryRepository.save(category);
        return categoryMapper.toDto(saved);
    }

    @Override
    @Transactional
    public void deleteCategory(Long catId) {

        if (!categoryRepository.existsById(catId)) {
            throw new NotFoundException("Category with id=" + catId + " was not found");
        }

        if (eventRepository.existsByCategoryId(catId)) {
            throw new ConflictException("The category is not empty");
        }

        categoryRepository.deleteById(catId);//МБ НАДО ВОЗВРАЩАТЬ СТРИНГ
    }

    @Override
    @Transactional
    public CategoryDto updateCategory(Long catId, NewCategoryDto updateCategoryDto) {
        Category category = categoryRepository.findById(catId)
                .orElseThrow(() -> new NotFoundException("Category with id=" + catId + " was not found"));

        if (categoryRepository.existsByNameAndIdNot(updateCategoryDto.getName(), catId)) {
            throw new ConflictException("Category with this name already exists");
        }

        category.setName(updateCategoryDto.getName());
        Category updated = categoryRepository.save(category);
        return categoryMapper.toDto(updated);
    }
}
