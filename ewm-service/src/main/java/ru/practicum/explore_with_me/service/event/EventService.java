package ru.practicum.explore_with_me.service.event;

import ru.practicum.explore_with_me.dto.event.*;
import ru.practicum.explore_with_me.dto.request.EventRequestStatusUpdateRequest;
import ru.practicum.explore_with_me.dto.request.EventRequestStatusUpdateResult;
import ru.practicum.explore_with_me.dto.request.ParticipationRequestDto;

import java.time.LocalDateTime;
import java.util.List;

public interface EventService {
    List<EventDtoResponse> getEventsAdmin(List<Long> usersId, List<EventState> states, List<Long> categoriesId,
                                          LocalDateTime
                                                  rangeStart, LocalDateTime rangeEnd, Integer from, Integer size);

    EventDtoResponse updateEventAdmin(Long id, UpdateEventAdminRequest updateEventAdminRequest);

    List<EventShortDto> findAllEvents(String text, List<Long> categories, Boolean paid, LocalDateTime rangeStart,
                                      LocalDateTime rangeEnd, Boolean onlyAvailable, String sort, Integer from,
                                      Integer size);

    EventDtoResponse getEvent(Long id);

    List<EventShortDto> getEventsForPrivate(Long userId, Integer from, Integer size);

    EventDtoResponse addEvent(Long userId, NewEventDto newEventDto);

    EventDtoResponse findUserEvent(Long userId, Long eventId);

    EventDtoResponse updateEventUser(Long userId, Long eventId, UpdateEventUserRequest updateEventUserRequest);

    List<ParticipationRequestDto> getRequestsEvent(Long userId, Long eventId);

    EventRequestStatusUpdateResult updateRequestStatus(Long userId, Long eventId,
                                                       EventRequestStatusUpdateRequest eventRequestStatusUpdateRequest);
}
