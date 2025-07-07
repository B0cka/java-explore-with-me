package ru.practicum.service;

import jakarta.servlet.http.HttpServletRequest;
import org.springframework.stereotype.Service;
import ru.practicum.statsdto.RequestDto;
import ru.practicum.statsdto.StatsDto;

import java.time.LocalDateTime;
import java.util.List;

@Service
public interface StatsService {

    String createHit(RequestDto requestDto, HttpServletRequest request);

    List<StatsDto> getStats(LocalDateTime start, LocalDateTime end, List<String> uris, boolean unique);

}
