package ru.practicum.admin.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import ru.practicum.admin.dto.CommentFullDto;
import ru.practicum.admin.dto.UpdateCommentRequest;
import ru.practicum.admin.exseptions.ConflictException;
import ru.practicum.admin.exseptions.NotFoundException;
import ru.practicum.admin.mapper.CommentMapper;
import ru.practicum.admin.model.*;
import ru.practicum.admin.repository.CommentRepository;
import ru.practicum.admin.repository.EventRepository;

import java.time.LocalDateTime;
import java.util.List;

@Service
@Slf4j
@RequiredArgsConstructor
public class AdminCommentServiceImpl implements AdminCommentService{
    private final CommentRepository commentRepository;
    private final EventRepository eventRepository;

    @Override
    public CommentFullDto updateStatus(Long commentId, Long eventId, UpdateCommentRequest updateCommentRequest) {
        log.info("Обновление статуса комментария с id={} на status={}", commentId, updateCommentRequest.getStatus());

        Comment comment = checkComment(commentId);

        if (comment.getStatus() == CommentStatus.APPROVED || comment.getStatus() == CommentStatus.REJECTED) {
            throw new ConflictException("Комментарий уже модерирован и не может быть изменён.");
        }

        comment.setStatus(updateCommentRequest.getStatus());

        if (updateCommentRequest.getStatus() == CommentStatus.APPROVED) {
            comment.setPublishedOn(LocalDateTime.now());
        }

        if(updateCommentRequest.getStatus() == CommentStatus.REJECTED){
            commentRepository.delete(comment);
            return null;
        }

        commentRepository.save(comment);
        return CommentMapper.toFullDto(comment);
    }

    @Override
    public List<CommentFullDto> getComments(SearchCommentParamsAdmin params) {
        PageRequest pageable = PageRequest.of(
                params.getFrom() / params.getSize(),
                params.getSize()
        );

        Specification<Comment> spec = Specification.where(null);

        if (params.getAuthorId() != null) {
            spec = spec.and((root, query, cb) ->
                    cb.equal(root.get("author").get("id"), params.getAuthorId()));
        }

        if (params.getEventId() != null) {
            spec = spec.and((root, query, cb) ->
                    cb.equal(root.get("event").get("id"), params.getEventId()));
        }

        if (params.getStatus() != null) {
            spec = spec.and((root, query, cb) ->
                    cb.equal(root.get("status"), params.getStatus()));
        }

        if (params.getRangeStart() != null) {
            spec = spec.and((root, query, cb) ->
                    cb.greaterThanOrEqualTo(root.get("createdOn"), params.getRangeStart()));
        }

        if (params.getRangeEnd() != null) {
            spec = spec.and((root, query, cb) ->
                    cb.lessThanOrEqualTo(root.get("createdOn"), params.getRangeEnd()));
        }

        Page<Comment> result = commentRepository.findAll(spec, pageable);

        return result.stream()
                .map(CommentMapper::toFullDto)
                .toList();
    }



    private Comment checkComment(Long commentId) {
        return commentRepository.findById(commentId).orElseThrow(
                () -> new NotFoundException("Комментария с id = " + commentId + " не существует"));
    }

    private Event checkEvent(Long eventId) {
        return eventRepository.findById(eventId).orElseThrow(
                () -> new NotFoundException("Евента с id = " + eventId + " не существует"));
    }

}
