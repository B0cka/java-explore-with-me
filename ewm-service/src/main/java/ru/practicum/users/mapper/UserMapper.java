package ru.practicum.users.mapper;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import ru.practicum.admin.exseptions.NotFoundException;
import ru.practicum.admin.exseptions.ValidationException;
import ru.practicum.admin.model.Category;
import ru.practicum.admin.model.Location;
import ru.practicum.admin.model.User;
import ru.practicum.admin.repository.AdminRepository;
import ru.practicum.admin.repository.CategoryRepository;

import ru.practicum.admin.repository.LocationRepository;
import ru.practicum.users.model.Event;
import ru.practicum.users.dto.EventShortDto;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;

@Component
@Slf4j
public class UserMapper {

    private final DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");

    public Event toEntity(EventShortDto dto, CategoryRepository categoryRepo,
                          AdminRepository userRepo, LocationRepository locationRepo) {

        Event event = new Event();

        event.setAnnotation(dto.getAnnotation());
        event.setDescription(dto.getDescription());
        event.setTitle(dto.getTitle());
        event.setPaid(dto.getPaid());
        event.setParticipantLimit(dto.getParticipantLimit());
        event.setRequestModeration(dto.getRequestModeration());
        event.setState(dto.getState() != null ? dto.getState() : "PENDING");
        log.info("--------------------------------- " + dto.getEventDate() + " ---------------------------------");
        if (dto.getEventDate() != null && !dto.getEventDate().isEmpty()) {
            try {
                event.setEventDate(LocalDateTime.parse(dto.getEventDate(), formatter));
            } catch (DateTimeParseException e) {
                throw new ValidationException("Неверный формат даты. Используйте yyyy-MM-dd HH:mm:ss");
            }
        } else {
            throw new ValidationException("Дата события обязательна");
        }
        event.setCreatedOn(dto.getCreatedOn() != null ? dto.getCreatedOn() : LocalDateTime.now().toString());

        if (dto.getCategory() != null && dto.getCategory().getId() != null) {
            Category category = categoryRepo.findById(dto.getCategory().getId())
                    .orElseThrow(() -> new NotFoundException("Category not found"));
            event.setCategory(category);
        }

        if (dto.getInitiator() != null && dto.getInitiator().getId() != null) {
            User initiator = userRepo.findById(dto.getInitiator().getId())
                    .orElseThrow(() -> new NotFoundException("User not found"));
            event.setInitiator(initiator);
        }

        if (dto.getLocation() != null) {
            Location location = locationRepo.findByLatAndLon(
                    dto.getLocation().getLat(),
                    dto.getLocation().getLon()
            ).orElseGet(() -> locationRepo.save(
                    Location.builder()
                            .lat(dto.getLocation().getLat())
                            .lon(dto.getLocation().getLon())
                            .build()
            ));
            event.setLocation(location);
        }

        if (event.getConfirmedRequests() == null) {
            event.setConfirmedRequests(0L);
        }
        if (event.getViews() == null) {
            event.setViews(0L);
        }

        return event;
    }
}