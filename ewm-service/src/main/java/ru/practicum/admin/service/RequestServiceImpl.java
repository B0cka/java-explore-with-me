package ru.practicum.admin.service;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import ru.practicum.admin.dto.EventStatus;
import ru.practicum.admin.dto.ParticipationRequestDto;
import ru.practicum.admin.exseptions.ConflictException;
import ru.practicum.admin.exseptions.NotFoundException;
import ru.practicum.admin.mapper.RequestMapper;
import ru.practicum.admin.model.Event;
import ru.practicum.admin.model.Request;
import ru.practicum.admin.model.RequestStatus;
import ru.practicum.admin.model.User;
import ru.practicum.admin.repository.EventRepository;
import ru.practicum.admin.repository.RequestRepository;
import ru.practicum.admin.repository.UserRepository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Service
@Slf4j
@RequiredArgsConstructor
public class RequestServiceImpl implements RequestService{

    private final EventRepository eventRepository;
    private final UserRepository userRepository;
    private final RequestRepository requestRepository;

    @Override
    public ParticipationRequestDto createRequest(Long userId, Long eventId) {
        log.info("Создание запроса с userId={} и eventId={}", userId, eventId);

        Event event = checkEvent(eventId);

        if (event.getInitiator().getId().equals(userId)) {
            throw new ConflictException("Нельзя участвовать в своем событии");
        }

        if (requestRepository.existsByRequesterIdAndEventId(userId, eventId)) {
            throw new ConflictException("Нельзя создать повторный запрос");
        }

        if (!event.getEventStatus().equals(EventStatus.PUBLISHED)) {
            throw new ConflictException("Событие не опубликовано");
        }

        if (event.getParticipantLimit() > 0) {
            long confirmedCount = requestRepository.countByEventIdAndStatus(eventId, RequestStatus.CONFIRMED);
            if (confirmedCount >= event.getParticipantLimit()) {
                throw new ConflictException("Достигнут лимит участников");
            }
        }

        Request request = Request.builder()
                .requester(checkUser(userId))
                .event(event)
                .created(LocalDateTime.now())
                .status(event.isRequestModeration() ? RequestStatus.PENDING : RequestStatus.CONFIRMED)
                .build();


        requestRepository.save(request);
        return RequestMapper.toDto(request);
    }

    @Override
    public List<ParticipationRequestDto> getUserRequests(Long userId) {
        log.info("Получение запросов пользователя: userId={}", userId);
        checkUser(userId);

        return requestRepository.findAllByRequesterId(userId).stream()
                .map(RequestMapper::toDto)
                .collect(Collectors.toList());
    }

    @Override
    @Transactional
    public ParticipationRequestDto cancelRequest(Long userId, Long requestId) {
        log.info("Отмена запроса: userId={}, requestId={}", userId, requestId);

        Request request = requestRepository.findByIdAndRequesterId(requestId, userId)
                .orElseThrow(() -> new NotFoundException("Запрос не найден"));

        if (request.getStatus().equals(RequestStatus.CANCELED)) {
            return RequestMapper.toDto(request);
        }

        request.setStatus(RequestStatus.CANCELED);
        Request canceledRequest = requestRepository.save(request);
        return RequestMapper.toDto(canceledRequest);
    }


    private User checkUser(Long userId) {
        return userRepository.findById(userId).orElseThrow(
                () -> new NotFoundException("Пользователя с id = " + userId + " не существует"));
    }

    private Event checkEvent(Long eventId){
        return eventRepository.findById(eventId).orElseThrow(
                () -> new NotFoundException("Евента с id = " + eventId + " не существует"));

    }
}
