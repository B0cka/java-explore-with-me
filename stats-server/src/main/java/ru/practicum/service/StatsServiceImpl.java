package ru.practicum.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import ru.practicum.exseptions.BadRequestException;
import org.springframework.stereotype.Service;
import ru.practicum.model.Hit;
import ru.practicum.repository.StatsRepository;
import ru.practicum.statsdto.RequestDto;
import ru.practicum.statsdto.StatsDto;

import java.time.format.DateTimeFormatter;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;


@Service
@Slf4j
@RequiredArgsConstructor
public class StatsServiceImpl implements StatsService {

    private final StatsRepository statsRepository;


    @Override
    public void createHit(RequestDto requestDto) {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
        String ip = requestDto.getIp();
        String uri = requestDto.getUri();

        log.info("Получен hit: app={}, uri={}, ip={}",
                requestDto.getApp(), uri, ip);
        Hit hit = Hit.builder()
                .uri(uri)
                .app(requestDto.getApp())
                .ip(ip)
                .timeStamp(LocalDateTime.parse(requestDto.getTimestamp(), formatter))
                .build();
        try {
            statsRepository.save(hit);
        } catch (Exception e) {
            log.error("Ошибка при отправке данных на stats-server: {}", e.getMessage());
        }
    }

    @Override
    public List<StatsDto> getStats(String start, String end, List<String> uris, boolean unique) {
        log.info("Получен запрос статистики: start={}, end={}, uris={}, unique={}", start, end, uris, unique);

        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
        LocalDateTime startTime = LocalDateTime.parse(start, formatter);
        LocalDateTime endTime = LocalDateTime.parse(end, formatter);

        if (endTime.isBefore(startTime)) {
            throw new BadRequestException("Конец должен быть позже начала!");
        }

        List<Object[]> rawData = unique
                ? statsRepository.findUniqueStats(startTime, endTime, uris)
                : statsRepository.findStats(startTime, endTime, uris);
        if (uris == null || uris.isEmpty()) {
            rawData = unique
                    ? statsRepository.findUniqueStatsWithoutUris(startTime, endTime)
                    : statsRepository.findStatsWithoutUris(startTime, endTime);
        }
        List<StatsDto> stats = new ArrayList<>();
        for (Object[] row : rawData) {
            String app = (String) row[0];
            String uri = (String) row[1];
            Long hits = (Long) row[2];

            StatsDto dto = new StatsDto();
            dto.setApp(app);
            dto.setUri(uri);
            dto.setHits(hits);
            stats.add(dto);
        }

        return stats;


    }


}