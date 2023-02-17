package ru.practicum.explore_with_me.dto.event;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.*;
import ru.practicum.explore_with_me.handler.exception.ForbiddenExceptionCust;

import javax.validation.constraints.*;
import java.time.LocalDateTime;

@ToString
@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
public class NewEventDto {
    @NotBlank
    @Size(max = 2000)
    private String annotation;
    @NotNull
    private Long category;
    @NotBlank
    @Size(min = 20, max = 7000)
    private String description;
    @NotNull
    @Future(groups = ForbiddenExceptionCust.class)
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime eventDate;
    @NotNull
    private Location location;
    @NotNull
    private Boolean paid;
    @PositiveOrZero
    private int participantLimit;
    @NotNull
    private Boolean requestModeration;
    @NotBlank
    @Size(min = 3, max = 120)
    private String title;

}
