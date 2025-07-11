package ru.practicum.users.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import ru.practicum.admin.exseptions.ConflictException;
import ru.practicum.admin.exseptions.NotFoundException;
import ru.practicum.admin.repository.AdminRepository;
import ru.practicum.admin.repository.CategoryRepository;
import ru.practicum.admin.repository.LocationRepository;
import ru.practicum.users.model.Event;
import ru.practicum.users.dto.EventShortDto;
import ru.practicum.users.mapper.UserMapper;
import ru.practicum.users.repository.EventRepository;

import java.time.LocalDateTime;

@Service
@RequiredArgsConstructor
@Slf4j
public class UserServiceImpl implements UserService{

    private final EventRepository eventRepository;
    private final UserMapper userMapper;
    private final AdminRepository adminRepository;
    private final CategoryRepository categoryRepository;
    private final LocationRepository locationRepo;

    @Override
    public Event createEvent(EventShortDto eventShortDto, Long userId) {
        log.info("Создание события для пользователя ID={}", userId);

        if (adminRepository.findById(userId).isEmpty()) {
            throw new NotFoundException("Пользователь с ID=" + userId + " не найден");
        }
        log.info("Received request to create event: {}", eventShortDto);
        Event event = userMapper.toEntity(eventShortDto,categoryRepository, adminRepository, locationRepo);

        if (event.getEventDate().isBefore(LocalDateTime.now())) {
            throw new ConflictException("Дата события должна быть в будущем");
        }

        event.setState("PENDING");

        Event savedEvent = eventRepository.save(event);
        log.info("Событие создано: ID={}", savedEvent.getId());
        return savedEvent;
    }

}
