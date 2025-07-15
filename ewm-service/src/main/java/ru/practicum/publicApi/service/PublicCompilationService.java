package ru.practicum.publicApi.service;

import ru.practicum.admin.dto.CompilationDto;

import java.util.List;

public interface PublicCompilationService {

    List<CompilationDto> getCompilations(Boolean pinned, Integer from, Integer size);
    CompilationDto getById(Long id);

}

