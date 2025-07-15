package ru.practicum.admin.service;

import ru.practicum.admin.dto.CompilationDto;
import ru.practicum.admin.dto.NewCompilationDto;

public interface CompilationService {
    CompilationDto createCompilation(NewCompilationDto newCompilationDto);
    void deleteCompilation(Long compId);
    CompilationDto updateCompilation(Long compId, NewCompilationDto updateCompilationDto);
}