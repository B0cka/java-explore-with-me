package ru.practicum.users.controller;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.constraints.Min;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import ru.practicum.StatsClient;
import ru.practicum.admin.dto.ParticipationRequestDto;
import ru.practicum.admin.service.RequestService;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/users/{userId}/requests")
public class UserRequestController {
    private final RequestService requestService;
    private final StatsClient statsClient;

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public ParticipationRequestDto addRequest(
            @PathVariable @Min(1) Long userId,
            @RequestParam @Min(1) Long eventId, HttpServletRequest request) {

        statsClient.createHit(request);
        return requestService.createRequest(userId, eventId);
    }

    @GetMapping
    public List<ParticipationRequestDto> getUserRequests(
            @PathVariable @Min(1) Long userId, HttpServletRequest request) {

        statsClient.createHit(request);
        return requestService.getUserRequests(userId);
    }

    @PatchMapping("/{requestId}/cancel")
    public ParticipationRequestDto cancelRequest(
            @PathVariable @Min(1) Long userId,
            @PathVariable @Min(1) Long requestId, HttpServletRequest request) {

        statsClient.createHit(request);
        return requestService.cancelRequest(userId, requestId);
    }
}