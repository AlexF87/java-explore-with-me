package ru.practicum.explore_with_me.dto.comment;

import lombok.*;
import ru.practicum.explore_with_me.handler.exception.ForbiddenExceptionCust;

import javax.validation.constraints.Future;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.time.LocalDateTime;

@Getter
@Setter
@ToString
@NoArgsConstructor
@AllArgsConstructor
public class NewCommentDto {
    @NotBlank
    @Size(max = 1000)
    private String text;
    @Future(groups = ForbiddenExceptionCust.class)
    private LocalDateTime created;
}
