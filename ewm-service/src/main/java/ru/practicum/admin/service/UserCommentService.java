package ru.practicum.admin.service;

import org.springframework.stereotype.Service;
import ru.practicum.admin.dto.CommentDto;
import ru.practicum.admin.dto.CommentRequestDto;


@Service
public interface UserCommentService {

    CommentDto createComment(Long userId, Long eventId, CommentRequestDto commentRequestDto);

    CommentDto updateComment(Long userId, Long commentId, Long eventId, CommentRequestDto commentRequestDto);

    void deleteComment(Long userId, Long commentId, Long eventId);
}
