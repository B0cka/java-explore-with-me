package ru.practicum.admin.dto;

import lombok.Data;
import ru.practicum.admin.model.CommentStatus;

@Data
public class UpdateCommentRequest {

    private CommentStatus status;

}
