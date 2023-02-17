package ru.practicum.explore_with_me.dto.comment;

import lombok.*;

import java.time.LocalDateTime;

@Getter
@Setter
@ToString
@NoArgsConstructor
@AllArgsConstructor
public class ResponseCommentDto {
    private Long id;
    private String text;
    private LocalDateTime created;
    private Long eventId;
    private Long userId;
}
