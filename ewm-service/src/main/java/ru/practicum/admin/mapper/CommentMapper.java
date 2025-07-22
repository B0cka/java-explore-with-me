package ru.practicum.admin.mapper;

import lombok.experimental.UtilityClass;
import ru.practicum.admin.dto.CommentDto;
import ru.practicum.admin.dto.CommentFullDto;
import ru.practicum.admin.model.Comment;

@UtilityClass
public class CommentMapper {

    public CommentDto toDto(Comment comment){

        return CommentDto.builder()
                .id(comment.getId())
                .name(comment.getAuthor().getName())
                .text(comment.getText())
                .createOn(comment.getCreatedOn())
                .build();

    }

    public CommentFullDto toFullDto(Comment comment){

        return CommentFullDto.builder()
                .id(comment.getId())
                .name(comment.getAuthor().getName())
                .text(comment.getText())
                .createOn(comment.getCreatedOn())
                .publishedOn(comment.getPublishedOn())
                .build();

    }
}
