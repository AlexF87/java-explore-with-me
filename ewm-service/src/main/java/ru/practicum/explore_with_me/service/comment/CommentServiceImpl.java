package ru.practicum.explore_with_me.service.comment;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.practicum.explore_with_me.dto.comment.NewCommentDto;
import ru.practicum.explore_with_me.dto.comment.ResponseCommentDto;
import ru.practicum.explore_with_me.dto.comment.UpdateCommentDto;
import ru.practicum.explore_with_me.dto.event.EventState;
import ru.practicum.explore_with_me.handler.exception.ForbiddenExceptionCust;
import ru.practicum.explore_with_me.handler.exception.NotFoundException;
import ru.practicum.explore_with_me.mapper.CommentMapper;
import ru.practicum.explore_with_me.model.Comment;
import ru.practicum.explore_with_me.model.Event;
import ru.practicum.explore_with_me.model.User;
import ru.practicum.explore_with_me.repository.CommentRepository;
import ru.practicum.explore_with_me.service.event.EventService;
import ru.practicum.explore_with_me.service.user.UserService;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class CommentServiceImpl implements CommentService {
    private final CommentRepository commentRepository;
    private final UserService userService;
    private final EventService eventService;
    private final CommentMapper commentMapper;

    @Override
    @Transactional
    public ResponseCommentDto addComment(Long userId, Long eventId, NewCommentDto newCommentDto) {
        Event event = eventService.checkEvent(eventId);
        if (event.getState() != EventState.PUBLISHED) {
            throw new ForbiddenExceptionCust("you can leave a comment only on published events");
        }
        User user = userService.checkUser(userId);
        Comment comment = commentMapper.toComment(newCommentDto, user, event);
        return CommentMapper.toResponseCommentDto(commentRepository.save(comment));
    }

    @Override
    @Transactional
    public ResponseCommentDto updateComment(Long commentId, Long userId, UpdateCommentDto updateCommentDto) {
        User user = userService.checkUser(userId);
        if (!updateCommentDto.getUserId().equals(userId)) {
            throw new ForbiddenExceptionCust("The user is not the owner");
        }
        Comment comment = commentRepository.findById(commentId).orElseThrow(() -> new
                NotFoundException(String.format("Comment with id=%d was not found", commentId)));
        if (updateCommentDto.getText() != null && !updateCommentDto.getText().isBlank()) {
            comment.setText(updateCommentDto.getText());
        }
        return CommentMapper.toResponseCommentDto(comment);
    }

    @Override
    public List<ResponseCommentDto> getAllCommentEvent(Long eventId) {
        eventService.checkEvent(eventId);
        List<Comment> comments = commentRepository.findAllByEventId(eventId);
        List<ResponseCommentDto> result = new ArrayList<>();
        result = comments.stream().map(CommentMapper::toResponseCommentDto).collect(Collectors.toList());
        return result;
    }

    @Override
    public ResponseCommentDto getComment(Long commentId, Long userId) {
        userService.checkUser(userId);
        Comment comment = commentRepository.findByIdAndAndUserId(commentId, userId);
        if (comment == null) {
            throw new NotFoundException(String.format("Comment with id=%d was not found", commentId));
        }
        return CommentMapper.toResponseCommentDto(comment);
    }

    @Override
    @Transactional
    public void deleteComment(Long commentId, Long userId) {
        Comment comment = commentRepository.findById(commentId).orElseThrow(() ->
                new NotFoundException(String.format("Comment with id=%d was not found", commentId)));
        if (comment.getUser().getId().equals(userId)) {
            commentRepository.delete(comment);
        } else {
            throw new ForbiddenExceptionCust("The user is not the owner");
        }
    }

    @Override
    @Transactional
    public void deleteCommentAdmin(Long commentId) {
        commentRepository.findById(commentId).orElseThrow(() ->
                new NotFoundException(String.format("Comment with id=%d was not found", commentId)));
        commentRepository.deleteById(commentId);
    }
}
