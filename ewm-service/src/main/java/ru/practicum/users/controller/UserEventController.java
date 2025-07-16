package ru.practicum.users.controller;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import jakarta.validation.constraints.Min;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import ru.practicum.StatsClient;
import ru.practicum.admin.dto.*;
import ru.practicum.admin.service.EventService;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/users")
public class UserEventController {
    private final EventService eventService;
    private final StatsClient statsClient;

    @PostMapping("/{userId}/events")
    @ResponseStatus(HttpStatus.CREATED)
    public EventFullDto addEvent(@PathVariable(value = "userId") @Min(1) Long userId,
                                 @RequestBody @Valid NewEventDto input, HttpServletRequest request) {
        statsClient.createHit(request);
        return eventService.addNewEvent(userId, input);
    }

    @PatchMapping("/{userId}/events/{eventId}")
    public EventFullDto updateEventByOwner(@PathVariable(value = "userId") @Min(0) Long userId,
                                           @PathVariable(value = "eventId") @Min(0) Long eventId,
                                           @RequestBody @Valid UpdateEventUserRequest inputUpdate, HttpServletRequest request) {
        statsClient.createHit(request);
        return eventService.updateEventByUserIdAndEventId(userId, eventId, inputUpdate);
    }

    @GetMapping("/{userId}/events")
    public List<EventFullDto> getUserEvents(@PathVariable @Min(1) Long userId,
                                            @RequestParam(defaultValue = "0") @Min(0) Integer from,
                                            @RequestParam(defaultValue = "10") @Min(1) Integer size, HttpServletRequest request) {
        statsClient.createHit(request);
        return eventService.getEventsByUserId(userId, from, size);
    }

    @GetMapping("/{userId}/events/{eventId}")
    public EventFullDto getUserEventsBuId(@PathVariable @Min(1) Long userId, @PathVariable @Min(1) Long eventId, HttpServletRequest request) {
        statsClient.createHit(request);
        return eventService.getEventById(userId, eventId);
    }

    @GetMapping("/{userId}/events/{eventId}/requests")
    public List<ParticipationRequestDto> getRequestsById(@PathVariable(value = "eventId") Long eventId, @PathVariable(value = "userId") Long userId, HttpServletRequest request) {
        statsClient.createHit(request);
        return eventService.getAllParticipationRequestsFromEventByOwner(userId, eventId);
    }

    @PatchMapping("/{userId}/events/{eventId}/requests")
    public EventRequestStatusUpdateResult updateStatusRequestFromOwner(@PathVariable(value = "userId") @Min(1) Long userId,
                                                                       @PathVariable(value = "eventId") @Min(1) Long eventId,
                                                                       @RequestBody EventRequestStatusUpdateRequest inputUpdate) {
        return eventService.updateStatusRequest(userId, eventId, inputUpdate);
    }

}
