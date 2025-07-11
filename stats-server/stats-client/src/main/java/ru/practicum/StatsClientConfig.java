package ru.practicum;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.client.RestTemplate;

@Configuration
public class StatsClientConfig {

    @Bean
    public StatsClient statsClient(RestTemplate restTemplate,
                                   @Value("${stats.server.url:http://localhost:9090}") String statsServerUrl) {
        return new StatsClient(restTemplate, statsServerUrl);
    }
}