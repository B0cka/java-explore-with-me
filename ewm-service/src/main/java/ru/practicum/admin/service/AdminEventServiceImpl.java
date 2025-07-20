package ru.practicum.admin.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import ru.practicum.StatsClient;
import ru.practicum.admin.dto.*;
import ru.practicum.admin.exseptions.BadRequestException;
import ru.practicum.admin.exseptions.ConflictException;
import ru.practicum.admin.exseptions.NotFoundException;
import ru.practicum.admin.mapper.EventMapper;
import ru.practicum.admin.mapper.LocationMapper;
import ru.practicum.admin.model.*;
import ru.practicum.admin.repository.CategoryRepository;
import ru.practicum.admin.repository.EventRepository;
import ru.practicum.admin.repository.RequestRepository;
import ru.practicum.statsdto.StatsDto;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Slf4j
public class AdminEventServiceImpl implements AdminEventService {

    private final EventRepository eventRepository;
    private final StatsClient statsClient;
    private final CategoryRepository categoryRepository;
    private final RequestRepository requestRepository;

    @Override
    public EventFullDto updateEventFromAdmin(Long eventId, UpdateEventAdminRequest updateEvent) {
        Event oldEvent = checkEvent(eventId);
        if (oldEvent.getEventStatus().equals(EventStatus.PUBLISHED) || oldEvent.getEventStatus().equals(EventStatus.CANCELED)) {
            throw new ConflictException("Можно изменить только неподтвержденное событие");
        }
        boolean hasChanges = false;
        Event eventForUpdate = universalUpdate(oldEvent, updateEvent);
        if (eventForUpdate == null) {
            eventForUpdate = oldEvent;
        } else {
            hasChanges = true;
        }
        LocalDateTime gotEventDate = updateEvent.getEventDate();
        if (gotEventDate != null) {
            if (gotEventDate.isBefore(LocalDateTime.now().plusHours(1))) {
                throw new BadRequestException("Дата начала события должна быть не ранее чем за час от даты публикации.");
            }
            eventForUpdate.setEventDate(gotEventDate);
            hasChanges = true;
        }

        EventUserState gotAction = updateEvent.getStateAction();
        if (gotAction != null) {
            if (EventUserState.PUBLISH_EVENT.equals(gotAction)) {
                if (!oldEvent.getEventStatus().equals(EventStatus.PENDING)) {
                    throw new ConflictException("Публиковать можно только события в статусе ожидания.");
                }
                eventForUpdate.setEventStatus(EventStatus.PUBLISHED);
                eventForUpdate.setPublishedOn(LocalDateTime.now());
                hasChanges = true;
            } else if (EventUserState.REJECT_EVENT.equals(gotAction)) {
                eventForUpdate.setEventStatus(EventStatus.CANCELED);
                hasChanges = true;
            }
        }

        Event eventAfterUpdate = null;
        if (hasChanges) {
            eventAfterUpdate = eventRepository.save(eventForUpdate);
        }
        return eventAfterUpdate != null ? EventMapper.toEventFullDto(eventAfterUpdate) : null;
    }

    @Override
    public List<EventFullDto> getEvents(SearchEventParamsAdmin searchEventParamsAdmin) {
        PageRequest pageable = PageRequest.of(
                searchEventParamsAdmin.getFrom() / searchEventParamsAdmin.getSize(),
                searchEventParamsAdmin.getSize()
        );

        Specification<Event> specification = Specification.where(null);

        if (searchEventParamsAdmin.getUsers() != null && !searchEventParamsAdmin.getUsers().isEmpty()) {
            specification = specification.and((root, query, cb) ->
                    root.get("initiator").get("id").in(searchEventParamsAdmin.getUsers()));
        }
        if (searchEventParamsAdmin.getStates() != null && !searchEventParamsAdmin.getStates().isEmpty()) {
            specification = specification.and((root, query, cb) ->
                    root.get("eventStatus").as(String.class).in(searchEventParamsAdmin.getStates()));
        }
        if (searchEventParamsAdmin.getCategories() != null && !searchEventParamsAdmin.getCategories().isEmpty()) {
            specification = specification.and((root, query, cb) ->
                    root.get("category").get("id").in(searchEventParamsAdmin.getCategories()));
        }
        if (searchEventParamsAdmin.getRangeStart() != null) {
            specification = specification.and((root, query, cb) ->
                    cb.greaterThanOrEqualTo(root.get("eventDate"), searchEventParamsAdmin.getRangeStart()));
        }
        if (searchEventParamsAdmin.getRangeEnd() != null) {
            specification = specification.and((root, query, cb) ->
                    cb.lessThanOrEqualTo(root.get("eventDate"), searchEventParamsAdmin.getRangeEnd()));
        }

        Page<Event> events = eventRepository.findAll(specification, pageable);
        List<Event> eventList = events.getContent();
        if (eventList == null || eventList.isEmpty()) {
            return List.of();
        }

        Map<Long, List<Request>> confirmedRequestsMap = getConfirmedRequestsCount(eventList);

        List<String> uris = eventList.stream()
                .map(event -> "/events/" + event.getId())
                .collect(Collectors.toList());

        LocalDateTime start = eventList.stream()
                .map(Event::getCreatedDate)
                .filter(Objects::nonNull)
                .min(LocalDateTime::compareTo)
                .orElse(LocalDateTime.now().minusYears(10));

        LocalDateTime end = LocalDateTime.now();

        List<StatsDto> viewStats = statsClient.getStats(start, end, uris, true);

        Map<Long, Long> viewsMap = viewStats.stream()
                .collect(Collectors.toMap(
                        s -> Long.parseLong(s.getUri().split("/")[2]),
                        StatsDto::getHits
                ));

        return eventList.stream()
                .map(event -> EventMapper.toEventFullDtoForAdmin(
                        event,
                        confirmedRequestsMap.getOrDefault(event.getId(), List.of()).size(),
                        viewsMap.getOrDefault(event.getId(), 0L)
                ))
                .collect(Collectors.toList());
    }


    private Event universalUpdate(Event oldEvent, UpdateEventRequest updateEvent) {
        boolean hasChanges = false;
        String gotAnnotation = updateEvent.getAnnotation();
        if (gotAnnotation != null && !gotAnnotation.isBlank()) {
            oldEvent.setAnnotation(gotAnnotation);
            hasChanges = true;
        }
        Long gotCategory = updateEvent.getCategory();
        if (gotCategory != null) {
            Category category = categoryRepository.findById(gotCategory)
                    .orElseThrow(() -> new NotFoundException("ТАкой категории не существует"));

            oldEvent.setCategory(category);
            hasChanges = true;
        }
        String gotDescription = updateEvent.getDescription();
        if (gotDescription != null && !gotDescription.isBlank()) {
            oldEvent.setDescription(gotDescription);
            hasChanges = true;
        }
        if (updateEvent.getLocation() != null) {
            Location location = LocationMapper.toLocation(updateEvent.getLocation());
            oldEvent.setLocation(location);
            hasChanges = true;
        }
        Integer gotParticipantLimit = updateEvent.getParticipantLimit();
        if (gotParticipantLimit != null) {
            oldEvent.setParticipantLimit(gotParticipantLimit);
            hasChanges = true;
        }
        if (updateEvent.getPaid() != null) {
            oldEvent.setPaid(updateEvent.getPaid());
            hasChanges = true;
        }
        Boolean requestModeration = updateEvent.getRequestModeration();
        if (requestModeration != null) {
            oldEvent.setRequestModeration(requestModeration);
            hasChanges = true;
        }
        String gotTitle = updateEvent.getTitle();
        if (gotTitle != null && !gotTitle.isBlank()) {
            oldEvent.setTitle(gotTitle);
            hasChanges = true;
        }
        if (!hasChanges) {

            oldEvent = null;
        }
        return oldEvent;
    }

    private Map<Long, List<Request>> getConfirmedRequestsCount(List<Event> events) {
        List<Request> requests = requestRepository.findAllByEventIdInAndStatus(events
                .stream().map(Event::getId).collect(Collectors.toList()), RequestStatus.CONFIRMED);
        return requests.stream().collect(Collectors.groupingBy(r -> r.getEvent().getId()));
    }

    private Event checkEvent(Long eventId) {
        return eventRepository.findById(eventId).orElseThrow(
                () -> new NotFoundException("Евента с id = " + eventId + " не существует"));
    }
}
