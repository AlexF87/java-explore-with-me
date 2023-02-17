package ru.practicum.explore_with_me.service.comment;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.practicum.explore_with_me.dto.comment.NewCommentDto;
import ru.practicum.explore_with_me.dto.comment.ResponseCommentDto;
import ru.practicum.explore_with_me.dto.comment.UpdateCommentDto;
import ru.practicum.explore_with_me.mapper.CommentMapper;
import ru.practicum.explore_with_me.model.Comment;
import ru.practicum.explore_with_me.model.Event;
import ru.practicum.explore_with_me.model.User;
import ru.practicum.explore_with_me.repository.CommentRepository;
import ru.practicum.explore_with_me.service.event.EventService;
import ru.practicum.explore_with_me.service.user.UserService;

import java.util.List;

@Service
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
        User user = userService.checkUser(userId);
        Comment comment = commentMapper.toComment(newCommentDto, user, event);
        return CommentMapper.toResponseCommentDto(comment);
    }

    @Override
    @Transactional
    public ResponseCommentDto updateComment(Long userId, Long eventId, UpdateCommentDto updateCommentDto) {
        Event event = eventService.checkEvent(eventId);
        User user = userService.checkUser(userId);
        if ()
        return null;
    }

    @Override
    public List<ResponseCommentDto> getAllCommentEvent(Long eventId) {
        return null;
    }

    @Override
    public ResponseCommentDto getComment(Long commentId, Long userId) {
        return null;
    }

    @Override
    public void deleteComment(Long commentId, Long userId) {

    }
}
