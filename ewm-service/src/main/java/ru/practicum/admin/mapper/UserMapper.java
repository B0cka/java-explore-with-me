package ru.practicum.admin.mapper;

import lombok.experimental.UtilityClass;
import ru.practicum.admin.dto.NewUserRequest;
import ru.practicum.admin.dto.UserDto;
import ru.practicum.admin.dto.UserShortDto;
import ru.practicum.admin.model.User;

@UtilityClass
public class UserMapper {

    public User toUser(NewUserRequest newUserRequest) {
        return User.builder()
                .name(newUserRequest.getName())
                .email(newUserRequest.getEmail())
                .build();
    }

    public UserDto toUserDto(User user) {
        return UserDto.builder()
                .id(user.getId())
                .name(user.getName())
                .email(user.getEmail())
                .build();
    }

    public UserShortDto toUserShortDto(User user) {
        return UserShortDto.builder()
                .id(user.getId())
                .name(user.getName())
                .build();
    }
}