package ru.practicum.explore_with_me.controller;

import org.springframework.web.bind.annotation.*;
import ru.practicum.explore_with_me.dto.category.CategoryDtoResponse;
import ru.practicum.explore_with_me.dto.compilation.CompilationDto;
import ru.practicum.explore_with_me.dto.event.EventDtoResponse;
import ru.practicum.explore_with_me.dto.event.EventShortDto;

import javax.validation.constraints.Positive;
import javax.validation.constraints.PositiveOrZero;
import java.util.List;

@RestController
@RequestMapping
public class PublicController {

    @GetMapping("/compilations")
    public List<CompilationDto> getCompilations(
            @RequestParam(required = false) boolean pinned,
            @RequestParam(defaultValue = "0") Integer from,
            @RequestParam(defaultValue = "10") Integer size) {
        return null;
    }

    @GetMapping("/compilations/{compId}")
    public CompilationDto getCompilation(@PathVariable long compId) {
        return null;
    }

    @GetMapping("/categories")
    public List<CategoryDtoResponse> getCategories(@PositiveOrZero @RequestParam(defaultValue = "0") Integer from,
                                                   @Positive @RequestParam(defaultValue = "10") Integer size) {
        return null;
    }

    @GetMapping("/categories/{catId}")
    public CategoryDtoResponse getCategory(@PathVariable Long catId) {
        return null;
    }

    @GetMapping("/events")
    public List<EventShortDto> getEvents(@RequestParam(value = "text", required = false) String text,
                                         @RequestParam(value = "categories", required = false) List<Long> categoryIds,
                                         @RequestParam(value = "paid", required = false) Boolean paid,
                                         @RequestParam(value = "rangeStart", required = false) String rangeStart,
                                         @RequestParam(value = "rangeEnd", required = false) String rangeEnd,
                                         @RequestParam(value = "onlyAvailable", required = false) Boolean onlyAvailable,
                                         @RequestParam(value = "sort", required = false) String sort,
                                         @PositiveOrZero @RequestParam(value = "from", defaultValue = "0") Integer from,
                                         @Positive @RequestParam(value = "size", defaultValue = "10") Integer size) {
        return null;
    }

    @GetMapping("/events/{Id}")
    public EventDtoResponse getEvent(@PathVariable long id){
        return null;
    }
}
