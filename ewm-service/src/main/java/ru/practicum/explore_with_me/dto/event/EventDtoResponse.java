package ru.practicum.explore_with_me.dto.event;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import ru.practicum.explore_with_me.dto.category.CategoryDtoResponse;
import ru.practicum.explore_with_me.dto.user.UserShortDto;

import java.time.LocalDateTime;
@Getter
@Setter
@Builder
public class EventDtoResponse {

    private String annotation;
    private CategoryDtoResponse category;
    private Long confirmedRequests;
    //Дата и время создания события
    private LocalDateTime createdOn;
    private String description;
    //Дата и время на которые намечено событие
    private String eventDate;
    private Long id;
    private UserShortDto initiator;
    private Location location;
    //Нужно ли оплачивать участие
    private boolean paid;
    //Лимит участников
    private int participantLimit;
    //Дата и время публикации события
    private LocalDateTime publishedOn;
    //Нужна ли пре-модерация заявок на участие
    private boolean requestModeration;
    //Список состояний жизненного цикла события
    private EventState state;
    private String title;
    //Количество просмотров события
    private long views;
}
