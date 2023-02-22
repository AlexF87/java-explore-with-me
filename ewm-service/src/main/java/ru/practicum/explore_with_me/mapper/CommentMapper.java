package ru.practicum.explore_with_me.mapper;

import org.springframework.stereotype.Component;
import ru.practicum.explore_with_me.dto.comment.NewCommentDto;
import ru.practicum.explore_with_me.dto.comment.ResponseCommentDto;
import ru.practicum.explore_with_me.model.Comment;
import ru.practicum.explore_with_me.model.Event;
import ru.practicum.explore_with_me.model.User;

import java.time.LocalDateTime;

@Component
public class CommentMapper {

    public Comment toComment(NewCommentDto newCommentDto, User user, Event event) {
        Comment comment = new Comment();
        comment.setCreated(LocalDateTime.now());
        comment.setText(newCommentDto.getText());
        comment.setUser(user);
        comment.setEvent(event);
        return comment;
    }

    public static ResponseCommentDto toResponseCommentDto(Comment comment) {
        ResponseCommentDto responseCommentDto = new ResponseCommentDto();
        responseCommentDto.setCreated(comment.getCreated());
        responseCommentDto.setText(comment.getText());
        responseCommentDto.setUserId(comment.getUser().getId());
        responseCommentDto.setEventId(comment.getEvent().getId());
        responseCommentDto.setId(comment.getId());
        responseCommentDto.setEditedOn(comment.getEditedOn() == null ? null : comment.getEditedOn());
        responseCommentDto.setEdited(comment.isEdited());
        return responseCommentDto;
    }
}
