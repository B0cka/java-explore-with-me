package ru.practicum.admin.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.practicum.admin.dto.CompilationDto;
import ru.practicum.admin.dto.NewCompilationDto;
import ru.practicum.admin.exseptions.BadRequestException;
import ru.practicum.admin.exseptions.NotFoundException;
import ru.practicum.admin.mapper.CompilationMapper;
import ru.practicum.admin.model.Compilation;
import ru.practicum.admin.model.Event;
import ru.practicum.admin.repository.CompilationRepository;
import ru.practicum.admin.repository.EventRepository;

import java.util.HashSet;
import java.util.Set;

@Service
@RequiredArgsConstructor
@Slf4j
public class CompilationServiceImpl implements CompilationService {
    private final CompilationRepository compilationRepository;
    private final EventRepository eventRepository;

    @Override
    @Transactional
    public CompilationDto createCompilation(NewCompilationDto newCompilationDto) {
        log.info("Создание сборки со значениями dto={}", newCompilationDto);
        Set<Event> events = new HashSet<>();
        if (newCompilationDto.getEvents() != null && !newCompilationDto.getEvents().isEmpty()) {
            events = new HashSet<>(eventRepository.findAllById(newCompilationDto.getEvents()));
        }
        if (newCompilationDto.getTitle().isBlank()) {
            throw new BadRequestException("Title не может состоять из пробелов");
        }
        Compilation compilation = CompilationMapper.toCompilation(newCompilationDto);
        compilation.setEvents(events);

        Compilation savedCompilation = compilationRepository.save(compilation);
        return CompilationMapper.toCompilationDto(savedCompilation);
    }

    @Override
    @Transactional
    public void deleteCompilation(Long compId) {
        log.info("Удаление сборки по id={}", compId);
        if (!compilationRepository.existsById(compId)) {
            throw new NotFoundException("Подборка с id=" + compId + " не найдена");
        }
        compilationRepository.deleteById(compId);
    }

    @Override
    @Transactional
    public CompilationDto updateCompilation(Long compId, NewCompilationDto updateCompilationDto) {
        log.info("Обновление сборки по id={} со значениями dto={}", compId, updateCompilationDto);
        Compilation compilation = compilationRepository.findById(compId)
                .orElseThrow(() -> new NotFoundException("Подборка с id=" + compId + " не найдена"));

        if (updateCompilationDto.getEvents() != null) {
            Set<Event> events = new HashSet<>(eventRepository.findAllById(updateCompilationDto.getEvents()));
            compilation.setEvents(events);
        }
        if (updateCompilationDto.getTitle() != null) {
            compilation.setTitle(updateCompilationDto.getTitle());
        }
        if (updateCompilationDto.getPinned() != null) {
            compilation.setPinned(updateCompilationDto.getPinned());
        }

        Compilation updatedCompilation = compilationRepository.save(compilation);
        return CompilationMapper.toCompilationDto(updatedCompilation);
    }
}