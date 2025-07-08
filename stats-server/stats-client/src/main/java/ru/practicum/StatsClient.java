
package ru.practicum;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;
import ru.practicum.statsdto.RequestDto;
import ru.practicum.statsdto.StatsDto;

import java.util.List;

@Component
@RequiredArgsConstructor
public class StatsClient {

    private final RestTemplate restTemplate;

    @Value("${stats.server.url}")
    String statsServerUrl;

    public String createHit(RequestDto requestDto) {

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


