package ru.practicum.admin.service;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import ru.practicum.admin.dto.*;
import ru.practicum.admin.exseptions.BadRequestException;
import ru.practicum.admin.exseptions.ConflictException;
import ru.practicum.admin.exseptions.NotFoundException;
import ru.practicum.admin.mapper.EventMapper;
import ru.practicum.admin.mapper.LocationMapper;
import ru.practicum.admin.model.Category;
import ru.practicum.admin.model.Event;
import ru.practicum.admin.model.Location;
import ru.practicum.admin.repository.CategoryRepository;
import ru.practicum.admin.repository.EventRepository;
import ru.practicum.admin.repository.LocationRepository;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Slf4j
public class AdminEventServiceImpl implements AdminEventService {

    private final EventRepository eventRepository;
    private final LocationRepository locationRepository;
    private final CategoryRepository categoryRepository;

    @Override
    public EventFullDto updateEventFromAdmin(Long eventId, UpdateEventAdminRequest updateEvent) {
        Event oldEvent = checkEvent(eventId);
        if (oldEvent.getEventStatus().equals(EventStatus.PUBLISHED) || oldEvent.getEventStatus().equals(EventStatus.CANCELED)) {
            throw new BadRequestException("Можно изменить только неподтвержденное событие");
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
                throw new ConflictException("Дата начала события должна быть не ранее чем за час от даты публикации.");
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
    @Transactional
    public List<EventFullDto> getEvents(List<Long> users, List<String> states, List<Long> categories, String rangeEnd, String rangeStart, Integer from, Integer size) {
        DateTimeFormatter dateTimeFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
        log.info("Получение евентов по параметрам: usersId={}, states={}, categories={}, rangeEnd={}, rangeStart={}, from={}, size={}", users, states, categories, rangeEnd, rangeStart,
                from,
                size);

        LocalDateTime start = rangeStart != null ?
                LocalDateTime.parse(rangeStart, dateTimeFormatter) :
                LocalDateTime.now();
        LocalDateTime end = rangeEnd != null ?
                LocalDateTime.parse(rangeEnd, dateTimeFormatter) :
                null;

        Specification<Event> spec = Specification.where(null);

        if (users != null && !users.isEmpty()) {
            spec = spec.and((root, query, cb) -> root.get("initiator").get("id").in(users));
        }
        if (states != null && !states.isEmpty()) {
            spec = spec.and((root, query, cb) -> root.get("eventStatus").as(String.class).in(states));
        }
        if (categories != null && !categories.isEmpty()) {
            spec = spec.and((root, query, cb) -> root.get("category").get("id").in(categories));
        }
        if (rangeStart != null) {
            spec = spec.and((root, query, cb) -> cb.greaterThanOrEqualTo(root.get("eventDate"), start));
        }
        if (rangeEnd != null) {
            spec = spec.and((root, query, cb) -> cb.lessThanOrEqualTo(root.get("eventDate"), end));
        }

        Pageable pageable = PageRequest.of(from / size, size, Sort.by("eventDate").descending());

        List<Event> events = eventRepository.findAll(spec, pageable).getContent();

        return events.stream()
                .map(EventMapper::toEventFullDto)
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

    private Event checkEvent(Long eventId) {
        return eventRepository.findById(eventId).orElseThrow(
                () -> new NotFoundException("Евента с id = " + eventId + " не существует"));
    }
}
