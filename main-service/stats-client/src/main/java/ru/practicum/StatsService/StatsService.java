package ru.practicum.StatsService;

import jakarta.servlet.http.HttpServletRequest;
import ru.practicum.StatsDto.RequestDto;

public interface StatsService {
    String createHit(RequestDto requestDto, HttpServletRequest request);
}
