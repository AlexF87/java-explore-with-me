package ru.practicum.explore_with_me.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import ru.practicum.explore_with_me.dto.event.EventDtoResponse;
import ru.practicum.explore_with_me.dto.event.EventState;
import ru.practicum.explore_with_me.dto.event.UpdateEventAdminRequest;
import ru.practicum.explore_with_me.handler.exception.BadRequestException;
import ru.practicum.explore_with_me.handler.exception.ForbiddenExceptionCust;
import ru.practicum.explore_with_me.service.event.EventService;

import javax.validation.constraints.Positive;
import javax.validation.constraints.PositiveOrZero;
import java.time.LocalDateTime;
import java.util.List;

@Slf4j
@Validated
@RestController
@RequestMapping("/admin/events")
@RequiredArgsConstructor
public class EventsControllerAdm {
    private final EventService eventService;

    @GetMapping
    @ResponseStatus(HttpStatus.OK)
    public List<EventDtoResponse> getEvents(@RequestParam(required = false) List<Long> users,
                                            @RequestParam(required = false) List<EventState> states,
                                            @RequestParam(required = false) List<Long> categories,
                                            @RequestParam(required = false) LocalDateTime rangeStart,
                                            @RequestParam(required = false) LocalDateTime rangeEnd,
                                            @PositiveOrZero @RequestParam(defaultValue = "0") Integer from,
                                            @Positive @RequestParam(defaultValue = "10") Integer size) {

        log.info("GET /admin/events users={},states={},categories={},\nrangeStart={},rangeEnd={}" +
                ",from={},size={}", users, states, categories, rangeStart, rangeEnd, from, size);

        return eventService.getEventsAdmin(users, states, categories,
                rangeStart, rangeEnd, from, size);
    }

    @PatchMapping("/{eventId}")
    @ResponseStatus(HttpStatus.OK)
    public EventDtoResponse update(@Positive @PathVariable Long eventId,
                                   @Validated @RequestBody UpdateEventAdminRequest updateEventAdminRequest,
                                   BindingResult errors) {
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
        log.info("PATCH /admin/events event id{}, {}", eventId, updateEventAdminRequest);
        return eventService.updateEventAdmin(eventId, updateEventAdminRequest);
    }
}
