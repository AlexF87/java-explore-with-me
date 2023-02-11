package ru.practicum.explore_with_me.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import ru.practicum.explore_with_me.dto.event.EventDtoResponse;
import ru.practicum.explore_with_me.dto.event.EventShortDto;
import ru.practicum.explore_with_me.service.event.EventService;

import javax.validation.constraints.Positive;
import java.time.LocalDateTime;
import java.util.List;

@Slf4j
@Validated
@RestController
@RequestMapping("/events")
@RequiredArgsConstructor
public class EventsControllerPub {
    private final EventService eventService;

    @GetMapping
    @ResponseStatus(HttpStatus.OK)
    public List<EventShortDto> findAllEvents(@RequestParam(required = false) String text,
                                         @RequestParam(required = false) List<Long> categories,
                                         @RequestParam(required = false) Boolean paid,
                                         @RequestParam(required = false) LocalDateTime rangeStart,
                                         @RequestParam(required = false) LocalDateTime rangeEnd,
                                         @RequestParam(required = false) Boolean onlyAvailable,
                                         @RequestParam(required = false) String sort,
                                         @RequestParam(defaultValue = "0") Integer from,
                                         @RequestParam(defaultValue = "10") Integer size) {

        log.info("GET /events text:{},\ncategories:{},paid:{},rangeStart:{},rangeEnd:{},\n" +
                        "onlyAvailable:{},sort:{},from:{}, size:{}",
                text, categories, paid, rangeStart, rangeEnd, onlyAvailable, sort, from, size);

        return eventService.findAllEvents(text, categories, paid, rangeStart, rangeEnd,
                onlyAvailable, sort, from, size);
    }

    @GetMapping("/{id}")
    @ResponseStatus(HttpStatus.OK)
    public EventDtoResponse getEvent(@PathVariable Long id) {
        log.info("GET /events/ id {}", id);

        return eventService.getEvent(id);
    }
}
