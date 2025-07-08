package ru.practicum.service;

import org.springframework.stereotype.Service;
import ru.practicum.statsdto.RequestDto;
import ru.practicum.statsdto.StatsDto;

import java.util.List;

@Service
public interface StatsService {

    String createHit(RequestDto requestDto);

    List<StatsDto> getStats(String start, String end, List<String> uris, boolean unique);

}
