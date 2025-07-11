package ru.practicum.statsdto;

import lombok.*;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class StatsDto {

    private String app;

    private String uri;

    private Long hits;

    public void setUri(String uri) {
        this.uri = uri;
    }

    public void setApp(String app) {
        this.app = app;
    }

    public void setHits(Long hits) {
        this.hits = hits;
    }
}
