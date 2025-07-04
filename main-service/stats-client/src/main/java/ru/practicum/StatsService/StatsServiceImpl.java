package ru.practicum.StatsService;

import jakarta.servlet.http.HttpServletRequest;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import ru.practicum.StatsDto.RequestDto;

@Service
@Slf4j
public class StatsServiceImpl implements StatsService {

    @Override
    public String createHit(RequestDto requestDto, HttpServletRequest request) {
        String ip = request.getRemoteAddr();
        String uri = request.getRequestURI();

        log.info("Получен hit: app={}, uri={}, ip={}",
                requestDto.getApp(), uri, ip);

        // ОТПРАВКА НА SERVER

        return "Hit accepted";
    }
}
