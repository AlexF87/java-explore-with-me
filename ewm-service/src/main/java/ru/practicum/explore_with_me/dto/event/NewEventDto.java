package ru.practicum.explore_with_me.dto.event;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.time.LocalDateTime;

public class NewEventDto {
    @NotBlank
    private String annotation;
    @NotNull
    Long category;
    @NotBlank
    @Size(min = 20, max = 7000)
    String description;
    @NotNull
    LocalDateTime eventDate;

    @NotNull
    Location location;

    Boolean paid;

    Integer participantLimit;

    Boolean requestModeration;

    @NotBlank
    @Size(min = 3, max = 120)
    String title;

}
