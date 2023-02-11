package ru.practicum.explore_with_me.service.event;

import ru.practicum.explore_with_me.dto.event.EventDtoResponse;
import ru.practicum.explore_with_me.dto.event.EventState;
import ru.practicum.explore_with_me.dto.event.UpdateEventAdminRequest;

import java.time.LocalDateTime;
import java.util.List;

public interface EventService {
    List<EventDtoResponse> getEventsAdmin(List<Long> usersId, List<EventState> states, List<Long> categoriesId,
                                      LocalDateTime
            rangeStart, LocalDateTime rangeEnd, Integer from, Integer size);
    EventDtoResponse updateEventAdmin(Long id, UpdateEventAdminRequest updateEventAdminRequest);
}
