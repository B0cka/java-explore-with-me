package ru.practicum.admin.service;

import jakarta.servlet.http.HttpServletRequest;
import ru.practicum.admin.dto.*;
import ru.practicum.admin.model.SearchEventParams;

import java.util.List;

public interface EventService {
    EventFullDto addNewEvent(Long userId, NewEventDto input);

    EventFullDto updateEventByUserIdAndEventId(Long userId, Long eventId, UpdateEventUserRequest inputUpdate);

    List<EventFullDto> getEventsByUserId(Long userId, Integer from, Integer size);

    EventFullDto getEventById(Long userId, Long eventId);

    List<ParticipationRequestDto> getAllParticipationRequestsFromEventByOwner(Long eventId, Long userId);

    EventRequestStatusUpdateResult updateStatusRequest(Long userId, Long eventId, EventRequestStatusUpdateRequest inputUpdate);

    List<EventShortDto> getAllEventFromPublic(SearchEventParams searchEventParams, HttpServletRequest request);

    EventFullDto getEventByIdPublic(Long eventId, HttpServletRequest request);

}