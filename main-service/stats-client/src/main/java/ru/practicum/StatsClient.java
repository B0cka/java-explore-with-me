package ru.practicum;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;
import ru.practicum.statsdto.RequestDto;
import ru.practicum.statsdto.StatsDto;

import java.util.List;

@Component
@RequiredArgsConstructor

public class StatsClient {

    RestTemplate restTemplate;

    public String createHit(RequestDto requestDto){
        String statsServerUrl = "http://localhost:9090/hit";

        return restTemplate.postForObject(statsServerUrl, requestDto, String.class);
    }

    public List<StatsDto> getStats(String start, String end, List<String> uris,boolean unique) {
        StringBuilder statsServerUrl = new StringBuilder("http://localhost:9090/stats");
        statsServerUrl.append("?start="+start).append("&end="+end).append("&unique="+unique);
        if(uris.size() != 0){
            for(String q : uris){
                statsServerUrl.append("&uris="+q);
            }
        }
        String finalURI = statsServerUrl.toString();
        StatsDto[] response = restTemplate.getForObject(finalURI, StatsDto[].class);
        return List.of(response);
    }
}
