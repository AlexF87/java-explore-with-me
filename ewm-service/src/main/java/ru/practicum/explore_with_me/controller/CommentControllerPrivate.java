package ru.practicum.explore_with_me.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import ru.practicum.explore_with_me.dto.comment.NewCommentDto;
import ru.practicum.explore_with_me.dto.comment.ResponseCommentDto;
import ru.practicum.explore_with_me.dto.comment.UpdateCommentDto;
import ru.practicum.explore_with_me.service.comment.CommentService;

import javax.validation.Valid;
import java.util.List;

@Slf4j
@RestController
@RequestMapping("/comment")
@Validated
@RequiredArgsConstructor
public class CommentControllerPrivate {

    private final CommentService commentService;

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public ResponseCommentDto addComment(@RequestParam Long userId,
                                         @RequestParam Long eventId,
                                         @RequestBody @Valid NewCommentDto newCommentDto) {
        log.info("POST /comment userId {}, eventId {], {} ", userId, eventId, newCommentDto);
        return commentService.addComment(userId, eventId, newCommentDto);
    }

    @PatchMapping("/user/{userId}/event/{eventId}")
    @ResponseStatus(HttpStatus.OK)
    public ResponseCommentDto updateComment(@PathVariable Long userId,
                                            @PathVariable Long eventId,
                                            @RequestBody @Valid UpdateCommentDto updateCommentDto) {
        log.info("Patch/user/{userId}/event/{eventId} userId {} eventId {} ", userId, eventId);
        return commentService.updateComment(userId, eventId, updateCommentDto);
    }

    @GetMapping("/event/{eventId}")
    @ResponseStatus(HttpStatus.OK)
    public List<ResponseCommentDto> getAllCommentEvent(@PathVariable Long eventId) {
        log.info("GET /comment/event/{eventId} ");
        return commentService.getAllCommentEvent(eventId);
    }

    @GetMapping("/{commentId}/user/{userId}")
    @ResponseStatus(HttpStatus.OK)
    public ResponseCommentDto getComment(@PathVariable Long commentId,
                                         @PathVariable Long userId) {
        log.info("GET /comment/{commentId}/user/{userId} commentId {} userId {}", commentId, userId);
        return commentService.getComment(commentId,userId);
    }

    @DeleteMapping("/{commentId}/user/{userId}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deleteComment(@PathVariable Long commentId,
                              @PathVariable Long userId) {
        log.info("DELETE /comment/{commentId}/user/{userId} commentId {} userId {}", commentId, userId);
        commentService.deleteComment(commentId, userId);
    }
}
