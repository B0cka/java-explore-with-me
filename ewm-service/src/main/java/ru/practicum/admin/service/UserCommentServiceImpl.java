package ru.practicum.admin.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import ru.practicum.admin.dto.CommentDto;
import ru.practicum.admin.dto.CommentRequestDto;
import ru.practicum.admin.dto.EventStatus;
import ru.practicum.admin.exseptions.ConflictException;
import ru.practicum.admin.exseptions.NotFoundException;
import ru.practicum.admin.mapper.CommentMapper;
import ru.practicum.admin.model.Comment;
import ru.practicum.admin.model.CommentStatus;
import ru.practicum.admin.model.Event;
import ru.practicum.admin.model.User;
import ru.practicum.admin.repository.CommentRepository;
import ru.practicum.admin.repository.EventRepository;
import ru.practicum.admin.repository.UserRepository;

import java.time.LocalDateTime;

@Service
@RequiredArgsConstructor
@Slf4j
public class UserCommentServiceImpl implements UserCommentService {

    private final UserRepository userRepository;
    private final EventRepository eventRepository;
    private final CommentRepository commentRepository;

    @Override
    public CommentDto createComment(Long userId, Long eventId, CommentRequestDto commentRequestDto){
        log.info("Создание комментария с userId={}, eventId={}, test={}", userId, eventId, commentRequestDto.getText());

        Event event = checkEvent(eventId);

        if (!event.getEventStatus().equals(EventStatus.PUBLISHED)) {
            throw new ConflictException("Нельзя комментировать неопубликованное событие.");
        }

        Comment comment = Comment.builder()
                .event(event)
                .author(checkUser(userId))
                .text(commentRequestDto.getText())
                .createdOn(LocalDateTime.now())
                .status(CommentStatus.PENDING)
                .build();

        commentRepository.save(comment);
        return CommentMapper.toDto(comment);

    }

    @Override
    public CommentDto updateComment(Long userId, Long commentId, Long eventId, CommentRequestDto commentRequestDto) {
        log.info("Обновление комментария: userId={}, commentId={}, eventId={}, text={}",
                userId, commentId, eventId, commentRequestDto.getText());

        Event event = checkEvent(eventId);

        if (!event.getEventStatus().equals(EventStatus.PUBLISHED)) {
            throw new ConflictException("Нельзя комментировать неопубликованное событие.");
        }

        Comment comment = commentRepository.findById(commentId)
                .orElseThrow(() -> new NotFoundException("Комментарий не найден"));

        if (!comment.getAuthor().getId().equals(userId)) {
            throw new ConflictException("Пользователь не является автором комментария");
        }

        comment.setText(commentRequestDto.getText());
        comment.setStatus(CommentStatus.PENDING);
        comment.setCreatedOn(LocalDateTime.now());

        commentRepository.save(comment);

        return CommentMapper.toDto(comment);
    }

    @Override
    public void deleteComment(Long userId, Long commentId, Long eventId){
        log.info("Удаление комментария: userId={}, commentId={}, eventId={}, text={}",
                userId, commentId, eventId );

        checkEvent(eventId);
        checkUser(userId);
        Comment comment = commentRepository.findById(commentId)
                .orElseThrow(() -> new NotFoundException("Комментарий не найден"));

        if (!comment.getAuthor().getId().equals(userId)) {
            throw new ConflictException("Пользователь не является автором комментария");
        }
        if (!comment.getEvent().getId().equals(eventId)) {
            throw new ConflictException("Комментарий не относится к данному событию");
        }
        commentRepository.delete(comment);
    }

    private User checkUser(Long userId) {
        return userRepository.findById(userId).orElseThrow(
                () -> new NotFoundException("Пользователя с id = " + userId + " не существует"));
    }

    private Event checkEvent(Long eventId) {
        return eventRepository.findById(eventId).orElseThrow(
                () -> new NotFoundException("Евента с id = " + eventId + " не существует"));
    }

}
