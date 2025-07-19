package ru.practicum.admin.service;

import ru.practicum.admin.dto.NewUserRequest;
import ru.practicum.admin.dto.UserDto;

import java.util.List;

public interface UserService {

    UserDto createUser(NewUserRequest newUserRequest);

    List<UserDto> getUsers(List<Long> ids, Integer from, Integer size);

    void deleteUser(Long id);

}
