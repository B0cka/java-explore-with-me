package ru.practicum.admin.controllers;

import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;
import ru.practicum.StatsClient;
import ru.practicum.admin.dto.EventFullDto;
import ru.practicum.admin.dto.UpdateEventAdminRequest;
import ru.practicum.admin.service.AdminEventService;

import java.util.List;

@Slf4j
@RestController
@RequestMapping("/admin/events")
@RequiredArgsConstructor
public class AdminEventController {

    private final StatsClient statsClient;
    private final AdminEventService adminEventService;

    @PatchMapping("/{id}")
    public EventFullDto updateEventAndState(@PathVariable Long id, @RequestBody UpdateEventAdminRequest updateEventAdminRequest, HttpServletRequest request) {
        log.info("dto={}", updateEventAdminRequest);
        statsClient.createHit(request);
        return adminEventService.updateEventFromAdmin(id, updateEventAdminRequest);

    }

    @GetMapping
    public List<EventFullDto> getEvents(HttpServletRequest request,
                                        @RequestParam(required = false) List<Long> users,
                                        @RequestParam(required = false) List<String> states,
                                        @RequestParam(required = false) List<Long> categories,
                                        @RequestParam(required = false) String rangeStart,
                                        @RequestParam(required = false) String rangeEnd,
                                        @RequestParam(defaultValue = "0") Integer from,
                                        @RequestParam(defaultValue = "10") Integer size) {

        statsClient.createHit(request);
        return adminEventService.getEvents(users, states, categories, rangeEnd, rangeStart, from, size);
    }

}
