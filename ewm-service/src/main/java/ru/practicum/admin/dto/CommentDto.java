package ru.practicum.admin.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class CommentDto { // это то, что я буду возвращать пользователю(при обычном создании, сразу такой комментарий на модерации,
    // потом через админ панель я сделаю published, но данной информации не будет в event, там будет другой dto с доп строчкой publishedOn)

    private Long id;

    private String name;

    private String text;

    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime createOn;

}
