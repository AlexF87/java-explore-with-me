package ru.practicum.explore_with_me.service.comment;

import ru.practicum.explore_with_me.dto.comment.NewCommentDto;
import ru.practicum.explore_with_me.dto.comment.ResponseCommentDto;
import ru.practicum.explore_with_me.dto.comment.UpdateCommentDto;

import java.util.List;

public interface CommentService {

    ResponseCommentDto addComment(Long userId, Long eventId, NewCommentDto newCommentDto);

    ResponseCommentDto updateComment(Long commentId, Long userId, UpdateCommentDto updateCommentDto);

    List<ResponseCommentDto> getAllCommentEvent(Long eventId);
    ResponseCommentDto getComment(Long commentId, Long userId);
    void deleteComment(Long commentId, Long userId);
    void deleteCommentAdmin(Long commentId);
}
