package ru.practicum.admin.service;

import ru.practicum.admin.dto.NewCategoryDto;
import ru.practicum.admin.dto.UserShortDto;
import ru.practicum.admin.dto.ResponseDto;
import ru.practicum.users.dto.CategoryDto;

import java.util.List;

public interface AdminService {

    ResponseDto createUser(UserShortDto requestDto);

    List<ResponseDto> getUsers(List<Long> ids, Long from, Long size);

    String deleteUser(Long id);

    CategoryDto createCategory(NewCategoryDto newCategoryDto);

    void deleteCategory(Long catId);

    CategoryDto updateCategory(Long catId, NewCategoryDto updateCategoryDto);
}
