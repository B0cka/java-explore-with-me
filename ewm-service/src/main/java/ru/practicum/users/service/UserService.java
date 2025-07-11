package ru.practicum.users.service;

import ru.practicum.users.model.Event;
import ru.practicum.users.dto.EventShortDto;

public interface UserService {

    Event createEvent(EventShortDto eventShortDto, Long id);

}
