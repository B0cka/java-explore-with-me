package ru.practicum.StatsService;

import jakarta.servlet.http.HttpServletRequest;
import ru.practicum.statsdto.RequestDto;
import ru.practicum.statsdto.StatsDto;

import java.util.List;

public interface StatsService {

    String createHit(RequestDto requestDto, HttpServletRequest request);

    List<StatsDto> getStats(String start, String end, List<String> uris, boolean unique);

}
