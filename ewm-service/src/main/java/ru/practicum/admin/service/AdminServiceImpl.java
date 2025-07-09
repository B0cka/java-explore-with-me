package ru.practicum.admin.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import ru.practicum.admin.dto.RequestDto;
import ru.practicum.admin.dto.ResponseDto;
import ru.practicum.admin.exseptions.NotFound;
import ru.practicum.admin.model.User;
import ru.practicum.admin.repository.AdminRepository;

import java.util.ArrayList;
import java.util.List;

@Service
@Slf4j
@RequiredArgsConstructor
public class AdminServiceImpl implements AdminService {

    private final AdminRepository adminRepository;

    @Override
    public ResponseDto createUser(RequestDto requestDto) {
        String name = requestDto.getName();
        String email = requestDto.getEmail();

        log.info("Создание пользователя с email={}, name={}", email, name);

        User user = User.builder()
                .email(email)
                .name(name)
                .build();

        adminRepository.save(user);
        return ResponseDto.builder()
                .email(user.getEmail())
                .name(user.getName())
                .build();
    }

    @Override
    public List<ResponseDto> getUsers(List<Long> ids, Long from, Long size) {
        log.info("Запрос с ids={}, from={}, size={}", ids, from, size);

        List<User> foundUsers = adminRepository.findAllById(ids);
        if (foundUsers.size() != ids.size()) {
            throw new NotFound("Некоторые пользователи не найдены");
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
}
