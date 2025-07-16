package ru.practicum.publicApi.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import ru.practicum.admin.dto.CompilationDto;
import ru.practicum.admin.exseptions.NotFoundException;
import ru.practicum.admin.mapper.CompilationMapper;
import ru.practicum.admin.model.Compilation;
import ru.practicum.admin.repository.CompilationRepository;

import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
@Slf4j
public class PublicCompilationServiceImpl implements PublicCompilationService {
    private final CompilationRepository compilationRepository;

    @Override
    public List<CompilationDto> getCompilations(Boolean pinned, Integer from, Integer size) {
        log.info("Запрос на получение сборки с параметрами: pinned={}, from={}, size={}", pinned, from, size);

        Pageable pageable = (Pageable) PageRequest.of(from / size, size);
        List<Compilation> compilations = compilationRepository.findAllByPinned(pinned, pageable);
        List<CompilationDto> returned = new ArrayList<>();
        for (Compilation c : compilations) {
            returned.add(CompilationMapper.toCompilationDto(c));
        }

        return returned;

    }

    @Override
    public CompilationDto getById(Long id) {
        log.info("Запрос на получение сборки с параметрами: id={}", id);
        Compilation compilation = compilationRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("Сборки по id: " + id + " не существует"));

        return CompilationMapper.toCompilationDto(compilation);

    }

}
