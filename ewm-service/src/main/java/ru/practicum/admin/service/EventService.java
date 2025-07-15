package ru.practicum.admin.service;

import ru.practicum.admin.dto.*;

import java.util.List;

public interface EventService {
    EventFullDto addNewEvent(Long userId, NewEventDto input);

    EventFullDto updateEventByUserIdAndEventId(Long userId, Long eventId, UpdateEventUserRequest inputUpdate);

    List<EventFullDto> getEventsByUserId(Long userId, Integer from, Integer size);

    EventFullDto getEventById(Long userId, Long eventId);

    List<ParticipationRequestDto> getAllParticipationRequestsFromEventByOwner(Long eventId, Long userId);

    EventRequestStatusUpdateResult updateStatusRequest(Long userId, Long eventId, EventRequestStatusUpdateRequest inputUpdate);
}