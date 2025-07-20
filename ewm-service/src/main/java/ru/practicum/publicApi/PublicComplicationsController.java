package ru.practicum.publicApi;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.constraints.Min;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;
import ru.practicum.StatsClient;
import ru.practicum.admin.dto.CompilationDto;
import ru.practicum.publicApi.service.PublicCompilationService;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/compilations")
public class PublicComplicationsController {

    private final StatsClient statsClient;
    private final PublicCompilationService publicCompilationService;

    @GetMapping
    public List<CompilationDto> getCompilations(@RequestParam(required = false) Boolean pinned, @RequestParam(defaultValue = "0") @Min(0) Integer from,
                                                @RequestParam(defaultValue = "10") @Min(1) Integer size, HttpServletRequest request) {

        statsClient.createHit(request);
        return publicCompilationService.getCompilations(pinned, from, size);

    }

    @GetMapping("/{comId}")
    public CompilationDto getById(@PathVariable("comId") Long id, HttpServletRequest request) {
        statsClient.createHit(request);

        return publicCompilationService.getById(id);
    }

}
