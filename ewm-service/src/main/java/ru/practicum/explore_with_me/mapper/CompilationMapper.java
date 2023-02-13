package ru.practicum.explore_with_me.mapper;

import org.springframework.stereotype.Component;
import ru.practicum.explore_with_me.dto.category.CategoryDtoResponse;
import ru.practicum.explore_with_me.dto.compilation.CompilationDto;
import ru.practicum.explore_with_me.dto.compilation.CompilationDtoNew;
import ru.practicum.explore_with_me.dto.event.EventShortDto;
import ru.practicum.explore_with_me.dto.user.UserShortDto;
import ru.practicum.explore_with_me.model.Compilation;
import ru.practicum.explore_with_me.model.Event;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

@Component
public class CompilationMapper {
    public static CompilationDto toCompilationDto(Compilation compilation) {
        List<EventShortDto> eventShortDtoList = new ArrayList<>();
        compilation.getEvents().forEach(
                (x) -> eventShortDtoList.add(EventShortDto.builder()
                        .annotation(x.getAnnotation())
                        .category(new CategoryDtoResponse(x.getCategory().getId(), x.getCategory().getName()))
                        .confirmedRequests(x.getConfirmedRequests())
                        .eventDate(x.getEventDate().toString())
                        .id(x.getId())
                        .initiator(new UserShortDto(x.getInitiator().getId(), x.getInitiator().getName()))
                        .paid(x.getPaid())
                        .title(x.getTitle())
                        .views(x.getViews())
                        .build())
        );

        CompilationDto compilationDto = CompilationDto.builder()
                .id(compilation.getId())
                .pinned(compilation.getPinned())
                .title(compilation.getTitle())
                .events(eventShortDtoList)
                .build();
        return compilationDto;
    }

    public Compilation toCompilation(CompilationDtoNew compilationDtoNew, Set<Event> events) {
        return new Compilation(events, compilationDtoNew.isPinned(), compilationDtoNew.getTitle());
    }
}
