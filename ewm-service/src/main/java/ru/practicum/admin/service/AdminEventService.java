package ru.practicum.admin.service;


import ru.practicum.admin.dto.EventFullDto;
import ru.practicum.admin.dto.UpdateEventAdminRequest;
import ru.practicum.admin.model.SearchEventParamsAdmin;

import java.util.List;

public interface AdminEventService {

    EventFullDto updateEventFromAdmin(Long id, UpdateEventAdminRequest updateEventAdminRequest);

    List<EventFullDto> getEvents(SearchEventParamsAdmin params);
}
