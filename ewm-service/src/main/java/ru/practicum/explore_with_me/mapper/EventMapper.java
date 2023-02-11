package ru.practicum.explore_with_me.mapper;

import org.springframework.stereotype.Component;
import ru.practicum.explore_with_me.dto.category.CategoryDtoResponse;
import ru.practicum.explore_with_me.dto.event.EventDtoResponse;
import ru.practicum.explore_with_me.dto.event.EventShortDto;
import ru.practicum.explore_with_me.dto.user.UserShortDto;
import ru.practicum.explore_with_me.model.Event;

@Component
public class EventMapper {
    public static EventDtoResponse toEventResponse(Event event) {
        EventDtoResponse eventDtoResponse = EventDtoResponse.builder()
                .id(event.getId())
                .annotation(event.getAnnotation())
                .categoryDtoResponse(new CategoryDtoResponse(event.getCategory().getId(),
                        event.getCategory().getName()))
                .confirmedRequests(event.getConfirmedRequests())
                .createdOn(event.getCreatedOn())
                .description(event.getDescription())
                .eventDate(event.getEventDate())
                .initiator(new UserShortDto(event.getInitiator().getId(), event.getInitiator().getName()))
                .location(event.getLocation())
                .paid(event.getPaid())
                .participantLimit(event.getParticipantLimit())
                .publishedOn(event.getPublishedOn())
                .requestModeration(event.getRequestModeration())
                .state(event.getState())
                .title(event.getTitle())
                .views(event.getViews())
                .build();
        return eventDtoResponse;
    }

    public static EventShortDto toEvenShortDto(Event event) {
        EventShortDto eventShortDto = EventShortDto.builder()
                .annotation(event.getAnnotation())
                .category(new CategoryDtoResponse(event.getCategory().getId(), event.getCategory().getName()))
                .confirmedRequests(event.getConfirmedRequests())
                .eventDate(event.getEventDate().toString())
                .id(event.getId())
                .initiator(new UserShortDto(event.getInitiator().getId(), event.getInitiator().getName()))
                .paid(event.getPaid())
                .title(event.getTitle())
                .views(event.getViews())
                .build();
        return eventShortDto;
    }
}
