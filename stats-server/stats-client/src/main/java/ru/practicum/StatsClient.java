
package ru.practicum;

import jakarta.servlet.http.HttpServletRequest;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;
import ru.practicum.statsdto.RequestDto;
import ru.practicum.statsdto.StatsDto;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;

@Component
@Slf4j
public class StatsClient {

    private final RestTemplate restTemplate;
    private final String statsServerUrl;

    public StatsClient(RestTemplate restTemplate, String statsServerUrl) {
        this.restTemplate = restTemplate;
        this.statsServerUrl = statsServerUrl;
    }

    public String createHit(HttpServletRequest request) {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");

        RequestDto requestDto = new RequestDto();
        requestDto.setApp("ewm-main-service");
        requestDto.setIp(request.getRemoteAddr());
        requestDto.setUri(request.getRequestURI());
        requestDto.setTimestamp(LocalDateTime.now().format(formatter));

        try {
            return restTemplate.postForObject(statsServerUrl + "/hit", requestDto, String.class);
        } catch (Exception e) {
            log.info("Stats service unavailable: {}", e.getMessage());
            return null;
        }
    }


    public List<StatsDto> getStats(LocalDateTime start, LocalDateTime end, List<String> uris, boolean unique) {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");

        String startStr = start.format(formatter);
        String endStr = end.format(formatter);

        StringBuilder statsServerUrls = new StringBuilder(statsServerUrl);
        statsServerUrls.append("/stats?start=" + startStr)
                .append("&end=" + endStr)
                .append("&unique=" + unique);

        if (uris != null && !uris.isEmpty()) {
            for (String q : uris) {
                statsServerUrls.append("&uris=" + q);
            }
        }

        StatsDto[] response = restTemplate.getForObject(statsServerUrls.toString(), StatsDto[].class);
        return List.of(response);
    }

}


