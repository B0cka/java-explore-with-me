package ru.practicum.admin.service;

import ru.practicum.admin.dto.RequestDto;
import ru.practicum.admin.dto.ResponseDto;

import java.util.List;

public interface AdminService {

    ResponseDto createUser(RequestDto requestDto);

    List<ResponseDto> getUsers(List<Long> ids, Long from, Long size);
}
