package ru.practicum.admin.controllers;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ru.practicum.StatsClient;
import ru.practicum.admin.dto.EventFullDto;
import ru.practicum.admin.dto.UpdateEventAdminRequest;
import ru.practicum.admin.model.SearchEventParamsAdmin;
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
    public EventFullDto updateEventAndState(@PathVariable Long id, @RequestBody @Valid UpdateEventAdminRequest updateEventAdminRequest, HttpServletRequest request) {
        log.info("dto={}", updateEventAdminRequest);
        statsClient.createHit(request);
        return adminEventService.updateEventFromAdmin(id, updateEventAdminRequest);

    }

    @GetMapping
    public ResponseEntity<List<EventFullDto>> getEvents(HttpServletRequest request,
                                                        @Valid SearchEventParamsAdmin params) {

        statsClient.createHit(request);
        List<EventFullDto> list =adminEventService.getEvents(params);
        return ResponseEntity.ok(list != null ? list : List.of());
    }

}
