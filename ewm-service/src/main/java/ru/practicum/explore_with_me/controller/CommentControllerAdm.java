package ru.practicum.explore_with_me.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import ru.practicum.explore_with_me.service.comment.CommentService;

@Slf4j
@RestController
@RequestMapping("/admin/comment")
@RequiredArgsConstructor
public class CommentControllerAdm {
    private final CommentService commentService;

    @DeleteMapping("/{commentId}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deleteComment(@PathVariable Long commentId) {
        log.info("DELETE /admin/comment/{commentId} commentId {}", commentId);
        commentService.deleteCommentAdmin(commentId);
    }
}
