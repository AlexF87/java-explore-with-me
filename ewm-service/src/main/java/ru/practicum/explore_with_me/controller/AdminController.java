package ru.practicum.explore_with_me.controller;

import org.springframework.web.bind.annotation.*;
import ru.practicum.explore_with_me.dto.category.CategoryDtoRequest;
import ru.practicum.explore_with_me.dto.category.CategoryDtoResponse;
import ru.practicum.explore_with_me.dto.compilation.CompilationDto;
import ru.practicum.explore_with_me.dto.compilation.CompilationDtoNew;
import ru.practicum.explore_with_me.dto.compilation.UpdateCompilationRequest;
import ru.practicum.explore_with_me.dto.event.EventDtoResponse;
import ru.practicum.explore_with_me.dto.event.EventState;
import ru.practicum.explore_with_me.dto.event.UpdateEventAdminRequest;
import ru.practicum.explore_with_me.dto.user.NewUserRequest;
import ru.practicum.explore_with_me.dto.user.UserDto;

import javax.validation.Valid;
import javax.validation.constraints.Positive;
import javax.validation.constraints.PositiveOrZero;
import java.util.List;

@RestController
@RequestMapping(path = "/admin")
public class AdminController {

//    @PostMapping("/categories")
//    public CategoryDtoResponse createCategory(@RequestBody CategoryDtoRequest categoryDtoRequest) {
//        return null;
//    }
//
//    @DeleteMapping("/categories/{catId}")
//    public void deleteCategory(@PathVariable long catId) {
//
//    }
//
//    @PatchMapping("/categories/{catId}")
//    public CategoryDtoResponse updateCategory(@RequestBody CategoryDtoRequest categoryDtoRequest,
//                                              @PathVariable Long catId) {
//        return null;
//    }

    @GetMapping("/events")
    public List<EventDtoResponse> getEvents(@RequestParam(value = "users", required = false) List<Long> userIds,
                                     @RequestParam(value = "states", required = false) List<EventState> states,
                                     @RequestParam(value = "categories", required = false) List<Long> categoryIds,
                                     @RequestParam(value = "rangeStart", required = false) String rangeStart,
                                     @RequestParam(value = "rangeEnd", required = false) String rangeEnd,
                                     @PositiveOrZero @RequestParam(value = "from", defaultValue = "0") Integer from,
                                     @Positive @RequestParam(value = "size", defaultValue = "10") Integer size){
        return null;
    }

    @PatchMapping("/events/{eventId}")
    public EventDtoResponse updateEvent(@PathVariable Long eventId,
                                 @RequestBody UpdateEventAdminRequest updateEventAdminRequest) {
        return null;
    }

//    @GetMapping("/users")
//    public List<UserDto> getUsers(@RequestParam(value = "ids", required = false) List<Long> userIds,
//                           @PositiveOrZero @RequestParam(value = "from", defaultValue = "0") Integer from,
//                           @Positive @RequestParam(value = "size", defaultValue = "10") Integer size) {
//        return null;
//    }
//
//    @PostMapping("/users")
//    public UserDto addUser(@Valid @RequestBody NewUserRequest newUserRequest) {
//        return null;
//    }
//
//    @DeleteMapping("/users/{userId}")
//    public void deleteUser(@PathVariable Long userId) {
//
//    }

    @PostMapping("/compilations")
    public CompilationDto addCompilation(@Valid @RequestBody CompilationDtoNew compilationDtoNew) {
        return null;
    }

    @DeleteMapping("/compilations/{comId}")
    public void deleteCompilation(@PathVariable long compId) {

    }

    @PatchMapping("/compilations/{compId}")
    public CompilationDto updateCompilation(@Valid @RequestBody UpdateCompilationRequest updateCompilationRequest,
                                            @PathVariable Long compId) {
    return null;
    }
}
