package ru.practicum.users.controller;

import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;
import ru.practicum.StatsClient;
import ru.practicum.admin.dto.CommentDto;
import ru.practicum.admin.dto.CommentRequestDto;
import ru.practicum.admin.service.UserCommentService;

@RestController
@RequiredArgsConstructor
@RequestMapping("/users")
public class UserCommentController {
    private final StatsClient statsClient;
    private final UserCommentService userCommentService;

    @PostMapping("/{userId}/comment/{eventId}")
    public CommentDto createComment(@PathVariable("userId") Long userId,
                                    @PathVariable("eventId") Long eventId,
                                    @RequestBody CommentRequestDto requestDto,
                                    HttpServletRequest request) {

        statsClient.createHit(request);
        return userCommentService.createComment(userId, eventId, requestDto);

    }

    @PatchMapping("/{userId}/comment/{eventId}/{commentId}")
    public CommentDto updateComment(@PathVariable("userId") Long userId,
                                    @PathVariable("commentId") Long commentId,
                                    @PathVariable("eventId") Long eventId,
                                    @RequestBody CommentRequestDto requestDto,
                                    HttpServletRequest request) {

        statsClient.createHit(request);
        return userCommentService.updateComment(userId, commentId, eventId, requestDto);

    }

    @DeleteMapping("/{userId}/comment/{eventId}/{commentId}")
    public void deleteComment(@PathVariable("userId") Long userId,
                              @PathVariable("commentId") Long commentId,
                              @PathVariable("eventId") Long eventId,
                              HttpServletRequest request) {

        statsClient.createHit(request);
        userCommentService.deleteComment(userId, commentId, eventId);
    }

}
