package ru.practicum.StatsController;

import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import ru.practicum.StatsDto.RequestDto;
import ru.practicum.StatsService.StatsService;

@RestController
@RequiredArgsConstructor
public class Controller {

    private final StatsService statsService;

    @PostMapping("/hit")
    public String createHit(@RequestBody RequestDto requestDto,
                            HttpServletRequest request) {
        return statsService.createHit(requestDto, request);
    }
}
