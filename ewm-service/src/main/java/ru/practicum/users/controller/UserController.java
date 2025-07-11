package ru.practicum.users.controller;

import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;
import ru.practicum.StatsClient;
import ru.practicum.users.model.Event;
import ru.practicum.users.dto.EventShortDto;
import ru.practicum.users.service.UserService;

@RestController
@RequiredArgsConstructor
@RequestMapping("/users")
public class UserController {

    private final UserService userService;
    private final StatsClient statsClient;

    @PostMapping("/{userId}/events")
    public Event createEvent(@RequestBody EventShortDto eventShortDto, HttpServletRequest request, @PathVariable("userId") Long id){

        statsClient.createHit(request);
        return userService.createEvent(eventShortDto, id);
    }

}
