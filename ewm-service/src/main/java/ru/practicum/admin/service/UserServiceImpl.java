package ru.practicum.admin.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import ru.practicum.admin.dto.NewUserRequest;
import ru.practicum.admin.dto.UserDto;
import ru.practicum.admin.exseptions.ConflictException;
import ru.practicum.admin.exseptions.NotFoundException;
import ru.practicum.admin.mapper.UserMapper;
import ru.practicum.admin.model.User;
import ru.practicum.admin.repository.UserRepository;

import java.util.List;
import java.util.stream.Collectors;

@Service
@Slf4j
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;

    @Override
    public UserDto createUser(NewUserRequest newUserRequest) {
        String name = newUserRequest.getName();
        String email = newUserRequest.getEmail();
        log.info("Создание пользователя с email={}, name={}", email, name);

        if (userRepository.findByEmail(email).isPresent()) {
            throw new ConflictException("Данная почта уже занята");
        }

        User user = userRepository.save(UserMapper.toUser(newUserRequest));
        return UserMapper.toUserDto(user);
    }


    @Override
    public List<UserDto> getUsers(List<Long> ids, Integer from, Integer size) {
        log.info("Запрос с ids={}, from={}, size={}", ids, from, size);

        PageRequest page = PageRequest.of(from / size, size);

        return (ids != null) ? userRepository.findByIdIn(ids, page)
                .stream().map(UserMapper::toUserDto).collect(Collectors.toList()) : userRepository.findAll(page)
                .stream().map(UserMapper::toUserDto).collect(Collectors.toList());
    }

    @Override
    public void deleteUser(Long id) {
        log.info("Удаление пользователя с id={}", id);
        if (!userRepository.existsById(id)) {
            throw new NotFoundException("Пользователь с id= " + id + " не найден");
        }
        userRepository.deleteById(id);
    }

}
