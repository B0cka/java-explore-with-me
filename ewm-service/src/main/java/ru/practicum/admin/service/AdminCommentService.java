package ru.practicum.admin.service;

import ru.practicum.admin.dto.CommentFullDto;
import ru.practicum.admin.dto.UpdateCommentRequest;
import ru.practicum.admin.model.SearchCommentParamsAdmin;

import java.util.List;

public interface AdminCommentService {

    CommentFullDto updateStatus(Long commentId, Long eventId, UpdateCommentRequest updateCommentRequest);

    List<CommentFullDto> getComments(SearchCommentParamsAdmin params);
}
