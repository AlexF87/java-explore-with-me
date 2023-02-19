package ru.practicum.explore_with_me.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import ru.practicum.explore_with_me.dto.event.EventDtoResponse;
import ru.practicum.explore_with_me.dto.event.EventShortDto;
import ru.practicum.explore_with_me.dto.event.NewEventDto;
import ru.practicum.explore_with_me.dto.event.UpdateEventUserRequest;
import ru.practicum.explore_with_me.dto.request.EventRequestStatusUpdateRequest;
import ru.practicum.explore_with_me.dto.request.EventRequestStatusUpdateResult;
import ru.practicum.explore_with_me.dto.request.ParticipationRequestDto;
import ru.practicum.explore_with_me.handler.exception.BadRequestException;
import ru.practicum.explore_with_me.handler.exception.ForbiddenExceptionCust;
import ru.practicum.explore_with_me.service.event.EventService;

import javax.validation.Valid;
import javax.validation.constraints.Positive;
import javax.validation.constraints.PositiveOrZero;
import java.util.List;

@Slf4j
@RestController
@Validated
@RequestMapping("/users/{userId}/events")
@RequiredArgsConstructor
public class EventsControllerPrivate {
    private final EventService eventService;

    @GetMapping
    @ResponseStatus(HttpStatus.OK)
    public List<EventShortDto> getEvents(@PathVariable Long userId,
                                         @PositiveOrZero @RequestParam(defaultValue = "0") Integer from,
                                         @Positive @RequestParam(defaultValue = "10") Integer size) {
        log.info("GET /users/{userId}/events userId {}, from {}, size {}", userId, from, size);
        return eventService.getEventsForPrivate(userId, from, size);
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public EventDtoResponse addEvent(@PathVariable Long userId,
                                     @Validated @RequestBody NewEventDto newEventDto, BindingResult errors) {
        checkExceptions(errors);
        log.info("POST /users/userId/events userId={}, {}", userId, newEventDto);
        return eventService.addEvent(userId, newEventDto);
    }

    @GetMapping("/{eventId}")
    @ResponseStatus(HttpStatus.OK)
    public EventDtoResponse getEvent(@PathVariable Long userId,
                                     @PathVariable Long eventId) {
        log.info("GET /users/{userId}/events/{eventId} {}, {}", userId, eventId);

        return eventService.findUserEvent(userId, eventId);
    }

    @PatchMapping("/{eventId}")
    @ResponseStatus(HttpStatus.OK)
    public EventDtoResponse updateEvent(@PathVariable Long userId,
                                        @PathVariable Long eventId,
                                        @Validated @RequestBody UpdateEventUserRequest updateEventUserRequest,
                                        BindingResult errors) {
        checkExceptions(errors);
        log.info("Patch /users/{userId}/events/{eventId} {}, {}, {}", userId, eventId, updateEventUserRequest);
        return eventService.updateEventUser(userId, eventId, updateEventUserRequest);
    }

    @GetMapping("/{eventId}/requests")
    @ResponseStatus(HttpStatus.OK)
    public List<ParticipationRequestDto> getRequests(@PathVariable Long userId,
                                                     @PathVariable Long eventId) {
        log.info("GET /users/{userId}/events//{eventId}/requests userId {}, eventId {}", userId, eventId);
        return eventService.getRequestsEvent(userId, eventId);
    }

    @PatchMapping("/{eventId}/requests")
    @ResponseStatus(HttpStatus.OK)
    public EventRequestStatusUpdateResult updateRequestsStatus(@Positive @PathVariable Long userId,
                                                               @Positive @PathVariable Long eventId,
                                                               @Valid @RequestBody EventRequestStatusUpdateRequest
                                                                       eventRequestStatusUpdateRequest) {

        log.info("PATCH /users/{userId}/events//{eventId}/requests userId {}, eventId {}, {}", userId, eventId,
                eventRequestStatusUpdateRequest);

        return eventService.updateRequestStatus(userId, eventId, eventRequestStatusUpdateRequest);
    }

    private void checkExceptions(BindingResult errors) {
        if (errors.hasErrors()) {
            for (Object object : errors.getAllErrors()) {
                if (object instanceof FieldError) {
                    FieldError fieldError = (FieldError) object;
                    if (fieldError.getCodes()[fieldError.getCodes().length - 1].equals("Future")) {
                        throw new ForbiddenExceptionCust(fieldError.getDefaultMessage());
                    } else {
                        throw new BadRequestException(fieldError.getDefaultMessage());
                    }
                }
            }
        }
    }
}

