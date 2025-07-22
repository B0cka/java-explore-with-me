package ru.practicum.admin.controllers;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ru.practicum.StatsClient;
import ru.practicum.admin.dto.CommentFullDto;
import ru.practicum.admin.dto.UpdateCommentRequest;
import ru.practicum.admin.model.SearchCommentParamsAdmin;
import ru.practicum.admin.service.AdminCommentService;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping(path = "/admin/comments")
public class AdminCommentController {
    private final StatsClient statsClient;
    private final AdminCommentService adminCommentService;

    @PatchMapping("/{commentId}/{eventId}")
    public CommentFullDto updateStatus(@PathVariable Long commentId, @PathVariable Long eventId, @RequestBody UpdateCommentRequest updateCommentRequest, HttpServletRequest request) {

        statsClient.createHit(request);
        return adminCommentService.updateStatus(commentId, eventId, updateCommentRequest);

    }

    @GetMapping
    public ResponseEntity<List<CommentFullDto>> getComments(HttpServletRequest request,
                                                            @Valid SearchCommentParamsAdmin params) {

        statsClient.createHit(request);
        List<CommentFullDto> list = adminCommentService.getComments(params);
        return ResponseEntity.ok(list != null ? list : List.of());
    }

}
