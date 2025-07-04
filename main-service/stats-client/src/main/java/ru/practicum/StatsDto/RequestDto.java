package ru.practicum.StatsDto;

import lombok.*;

@NoArgsConstructor
@AllArgsConstructor
@RequiredArgsConstructor
@Data
@Builder
public class RequestDto {

    private Long id;

    private String app;

    private String uri;

    private Long ip;

    private String timestamp;
}
