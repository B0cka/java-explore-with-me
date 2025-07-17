package ru.practicum.admin.service;


import ru.practicum.admin.dto.EventFullDto;
import ru.practicum.admin.dto.UpdateEventAdminRequest;

import java.util.List;

public interface AdminEventService {

    EventFullDto updateEventFromAdmin(Long id, UpdateEventAdminRequest updateEventAdminRequest);

    List<EventFullDto> getEvents(List<Long> usersId,
                          List<String> states,
                          List<Long> categories,
                          String rangeEnd,
                          String rangeStart,
                          Integer from,
                          Integer size);
}
