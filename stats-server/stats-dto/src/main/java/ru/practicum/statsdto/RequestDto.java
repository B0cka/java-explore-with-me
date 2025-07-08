package ru.practicum.statsdto;

import lombok.Data;
import lombok.Getter;

@Data
@Getter
public class RequestDto {
    private String app;
    private String uri;
    private String ip;
    private String timestamp;

    public String getApp() {
        return app;
    }

}
