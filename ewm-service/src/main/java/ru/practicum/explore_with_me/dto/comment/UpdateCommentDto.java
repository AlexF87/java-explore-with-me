package ru.practicum.explore_with_me.dto.comment;

import lombok.*;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.time.LocalDateTime;

@Getter
@Setter
@ToString
@NoArgsConstructor
@AllArgsConstructor
public class UpdateCommentDto {
    @NotBlank
    @Size(max = 1000)
    private String text;
    private LocalDateTime created;
    @NotNull
    private Long eventId;
    @NotNull
    private Long userId;
}
