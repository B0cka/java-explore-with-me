
package ru.practicum;

import jakarta.servlet.http.HttpServletRequest;

import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;
import ru.practicum.statsdto.RequestDto;
import ru.practicum.statsdto.StatsDto;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;

@Component
public class StatsClient {

    private final RestTemplate restTemplate;
    private final String statsServerUrl;

    public StatsClient(RestTemplate restTemplate, String statsServerUrl) {
        this.restTemplate = restTemplate;
        this.statsServerUrl = statsServerUrl;
    }

    public String createHit(HttpServletRequest request) {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ss.SSSSSS");
//        RequestDto requestDto = RequestDto.builder()
//                .ip(request.getRemoteAddr())
//                .app("ewm-main-service")
//                .uri(request.getRequestURI())
//                .timestamp(LocalDateTime.now().toString())
//                .build();

        RequestDto requestDto = new RequestDto();
        requestDto.setApp(request.getRemoteAddr());
        requestDto.setIp(request.getRemoteAddr());
        requestDto.setUri(request.getRequestURI());
        requestDto.setTimestamp(LocalDateTime.now().format(formatter));

        return restTemplate.postForObject(statsServerUrl + "/hit", requestDto, String.class);
    }

    public List<StatsDto> getStats(String start, String end, List<String> uris, boolean unique) {
        StringBuilder statsServerUrls = new StringBuilder(statsServerUrl);
        statsServerUrls.append("/stats?start=" + start).append("&end=" + end).append("&unique=" + unique);
        if (uris.size() != 0) {
            for (String q : uris) {
                statsServerUrls.append("&uris=" + q);
            }
        }
        String finalURI = statsServerUrls.toString();
        StatsDto[] response = restTemplate.getForObject(finalURI, StatsDto[].class);
        return List.of(response);
    }
}


