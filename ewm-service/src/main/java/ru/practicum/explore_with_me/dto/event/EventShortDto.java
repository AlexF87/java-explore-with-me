package ru.practicum.explore_with_me.dto.event;

import ru.practicum.explore_with_me.dto.category.CategoryDtoResponse;
import ru.practicum.explore_with_me.dto.user.UserShortDto;

public class EventShortDto {
    private String annotation;
    private CategoryDtoResponse category;
    private Long confirmedRequests;
    private String eventDate;
    private Long id;
    private UserShortDto initiator;
    private boolean paid;
    private String title;
    private Long views;
}
