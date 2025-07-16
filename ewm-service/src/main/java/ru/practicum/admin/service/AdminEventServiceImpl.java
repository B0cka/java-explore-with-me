package ru.practicum.admin.service;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import ru.practicum.admin.dto.EventFullDto;
import ru.practicum.admin.dto.EventStatus;
import ru.practicum.admin.dto.UpdateEventAdminRequest;
import ru.practicum.admin.exseptions.ConflictException;
import ru.practicum.admin.exseptions.NotFoundException;
import ru.practicum.admin.mapper.EventMapper;
import ru.practicum.admin.mapper.LocationMapper;
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
    @Transactional
    public EventFullDto updateEventAndState(Long id, UpdateEventAdminRequest updateEventAdminRequest) {
        log.info("Admin: Редактирование события id={}", id);

        Event event = checkEvent(id);

        updateEventFields(event, updateEventAdminRequest);

        if (updateEventAdminRequest.getStateAction() != null) {

            switch (updateEventAdminRequest.getStateAction()) {
                case EventStatus.PUBLISHED:

                    if (!event.getEventStatus().equals(EventStatus.PENDING)) {
                        throw new ConflictException("Событие должно быть в состоянии PENDING для публикации");
                    }

                    event.setEventStatus(EventStatus.PUBLISHED);
                    event.setPublishedOn(LocalDateTime.now());
                    break;

                case EventStatus.CANCELED:
                    if (event.getEventStatus().equals(EventStatus.PUBLISHED)) {
                        throw new ConflictException("Нельзя отклонить уже опубликованное событие");
                    }
                    event.setEventStatus(EventStatus.CANCELED);
                    break;
            }

        }

        Event updatedEvent = eventRepository.save(event);
        return EventMapper.toEventFullDto(updatedEvent);
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

    private void updateEventFields(Event event, UpdateEventAdminRequest update) {
        if (update.getAnnotation() != null && !update.getAnnotation().isBlank()) {
            event.setAnnotation(update.getAnnotation());
        }
        if (update.getCategory() != null) {
            event.setCategory(categoryRepository.findById(update.getCategory())
                    .orElseThrow(() -> new NotFoundException("Категория не найдена")));
        }
        if (update.getDescription() != null && !update.getDescription().isBlank()) {
            event.setDescription(update.getDescription());
        }
        if (update.getEventDate() != null) {
            event.setEventDate(update.getEventDate());
        }
        if (update.getLocation() != null) {
            Location location = locationRepository.save(LocationMapper.toLocation(update.getLocation()));
            event.setLocation(location);
        }
        if (update.getPaid() != null) {
            event.setPaid(update.getPaid());
        }
        if (update.getParticipantLimit() != null) {
            event.setParticipantLimit(update.getParticipantLimit());
        }
        if (update.getRequestModeration() != null) {
            event.setRequestModeration(update.getRequestModeration());
        }
        if (update.getTitle() != null && !update.getTitle().isBlank()) {
            event.setTitle(update.getTitle());
        }
    }

    private Event checkEvent(Long eventId) {
        return eventRepository.findById(eventId).orElseThrow(
                () -> new NotFoundException("Евента с id = " + eventId + " не существует"));
    }
}
