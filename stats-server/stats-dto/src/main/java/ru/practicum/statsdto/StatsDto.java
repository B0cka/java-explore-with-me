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


}
