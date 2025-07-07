package ru.practicum.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
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
    public String createHit(RequestDto requestDto) {
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
            return "Информация сохранена";
        } catch (Exception e) {
            log.error("Ошибка при отправке данных на stats-server: {}", e.getMessage());
            return "Ошибка при сохранении";
        }
    }

    @Override
    public List<StatsDto> getStats(String start, String end, List<String> uris, boolean unique) {
        log.info("Получен запрос статистики: start={}, end={}, uris={}, unique={}", start, end, uris, unique);

        try {
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
            LocalDateTime startTime = LocalDateTime.parse(start, formatter);
            LocalDateTime endTime = LocalDateTime.parse(end, formatter);

            List<Object[]> rawData = unique
                    ? statsRepository.findUniqueStats(startTime, endTime, uris)
                    : statsRepository.findStats(startTime, endTime, uris);
            if(uris == null){
                rawData = unique
                        ? statsRepository.findUniqueStatsWithoutUris(startTime, endTime)
                        : statsRepository.findStatsWithoutUris(startTime, endTime);
            }
            List<StatsDto> stats = new ArrayList<>();
            for (Object[] row : rawData) {
                String app = (String) row[0];
                String uri = (String) row[1];
                Long hits = (Long) row[2];

                StatsDto dto = StatsDto.builder()
                        .app(app)
                        .uri(uri)
                        .hits(hits)
                        .build();
                stats.add(dto);
            }

            return stats;

        } catch (Exception e) {
            log.error("Ошибка при обработке статистики: {}", e.getMessage());
            throw new RuntimeException("Не удалось обработать запрос статистики", e);
        }
    }


}