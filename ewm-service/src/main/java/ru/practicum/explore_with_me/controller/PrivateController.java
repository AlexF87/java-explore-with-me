package ru.practicum.explore_with_me.controller;

import org.springframework.web.bind.annotation.*;
import ru.practicum.explore_with_me.dto.event.EventDtoResponse;
import ru.practicum.explore_with_me.dto.event.EventShortDto;
import ru.practicum.explore_with_me.dto.event.NewEventDto;
import ru.practicum.explore_with_me.dto.event.UpdateEventUserRequest;
import ru.practicum.explore_with_me.dto.request.EventRequestStatusUpdateRequest;
import ru.practicum.explore_with_me.dto.request.EventRequestStatusUpdateResult;
import ru.practicum.explore_with_me.dto.request.ParticipationRequestDto;

import javax.validation.constraints.Positive;
import javax.validation.constraints.PositiveOrZero;
import java.util.List;

@RestController
@RequestMapping
public class PrivateController {

    @GetMapping("/users/{userId}/events")
    public List<EventShortDto> getEvents(@PathVariable("userId") Integer userId,
                                         @PositiveOrZero @RequestParam(defaultValue = "0") Integer from,
                                         @Positive @RequestParam(defaultValue = "10") Integer size) {
        return null;
    }

    @PostMapping("/users/{userId}/events")
    public EventDtoResponse addEvent(@PathVariable("userId") Integer userId,
                                     @RequestBody NewEventDto newEventDto) {
        return null;
    }

    @GetMapping("/users/{userId}/events/{eventId}")
    public EventDtoResponse getEvent(@PathVariable("userId") Long userId,
                                     @PathVariable("eventId") Long eventId) {
        return null;
    }

    @PatchMapping("/users/{userId}/events/{eventId}")
    public EventDtoResponse updateEvent(@PathVariable("userId") Long userId,
                                        @PathVariable("eventId") Long eventId,
                                        @RequestBody UpdateEventUserRequest updateEventUserRequest) {
        return null;
    }

    //Получение информации о запросах на участие в событии текущего пользователя
    @GetMapping("/users/{userId}/events/{eventId}/requests")
    public List<ParticipationRequestDto> getRequests(@PathVariable("userId") Long userId,
                                                     @PathVariable("eventId") Long eventId) {
        return null;
    }

    //Изменение статуса (подтверждена, отменена) заявок на участие в событии текущего пользователя
    @PatchMapping("/users/{userId}/events/{eventId}/requests")
    public EventRequestStatusUpdateResult updateRequestState(@PathVariable("userId") Long userId,
                                                             @PathVariable("eventId") Long eventId,
                                                             @RequestBody EventRequestStatusUpdateRequest
                                                                     eventRequestStatusUpdateRequest) {
        return null;
    }

    //Запросы на участие
    //Получение информации о заявках текущего пользователя  на участие в чужих событиях
    @GetMapping("/users/{userId}/requests")
    public List<ParticipationRequestDto> getRequestUser(@PathVariable("userId") Long userId) {
        return null;
    }

    //Добавление запроса от текущего пользователя на участие в событии
    @PostMapping("/users/{userId}/requests")
    public ParticipationRequestDto addRequest(@PathVariable("userId") Long userId,
                                              @RequestParam("eventId") Long eventId) {
        return null;
    }

    //Отмена своего зароса на участии в событии
    @PatchMapping("/users/{userId}/requests/{requestId}/cancel")
    public ParticipationRequestDto cancelRequest(@PathVariable("userId") Long userId,
                                                 @PathVariable("requestId") Long requestId) {
        return null;
    }
}
