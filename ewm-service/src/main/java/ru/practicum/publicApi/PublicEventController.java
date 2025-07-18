package ru.practicum.publicApi;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import jakarta.validation.constraints.Min;
import lombok.RequiredArgsConstructor;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import ru.practicum.admin.dto.EventFullDto;
import ru.practicum.admin.dto.EventShortDto;
import ru.practicum.admin.model.SearchEventParams;
import ru.practicum.admin.service.EventService;


import java.util.List;

@RestController
@RequiredArgsConstructor
@Validated
@RequestMapping(path = "/events")
public class PublicEventController {
    private final EventService eventService;

    @GetMapping
    public List<EventShortDto> getAllEvents(@Valid @ModelAttribute SearchEventParams searchEventParams,
                                            HttpServletRequest request) {
        return eventService.getAllEventFromPublic(searchEventParams, request);
    }

    @GetMapping("/{eventId}")
    public EventFullDto getEventById(@PathVariable(value = "eventId") @Min(1) Long eventId,
                                     HttpServletRequest request) {
        return eventService.getEventByIdPublic(eventId, request);
    }
}
