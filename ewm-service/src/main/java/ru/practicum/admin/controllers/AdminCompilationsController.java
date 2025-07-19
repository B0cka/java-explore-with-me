package ru.practicum.admin.controllers;


import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import ru.practicum.StatsClient;
import ru.practicum.admin.dto.CompilationDto;
import ru.practicum.admin.dto.NewCompilationDto;
import ru.practicum.admin.service.CompilationService;



@RestController
@RequiredArgsConstructor
@RequestMapping("/admin/compilations")
public class AdminCompilationsController {
    private final CompilationService compilationService;
    private final StatsClient statsClient;

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public CompilationDto createCompilation(@RequestBody @Valid NewCompilationDto newCompilationDto, HttpServletRequest request) {
        statsClient.createHit(request);
        return compilationService.createCompilation(newCompilationDto);
    }

    @DeleteMapping("/{compId}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deleteCompilation(@PathVariable Long compId,  HttpServletRequest request) {
        statsClient.createHit(request);
        compilationService.deleteCompilation(compId);
    }

    @PatchMapping("/{compId}")
    public CompilationDto updateCompilation(@PathVariable Long compId,
                                            @RequestBody @Valid NewCompilationDto updateCompilationDto,  HttpServletRequest request) {
        statsClient.createHit(request);
        return compilationService.updateCompilation(compId, updateCompilationDto);
    }
}