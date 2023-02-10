package ru.practicum.explore_with_me.dto.compilation;

import ru.practicum.explore_with_me.dto.event.EventShortDto;

import java.util.List;

public class CompilationDto {
    private List<EventShortDto> events;
    private long id;
    //Закреплена ли подборка на главной странице сайта
    private boolean pinned;
    //Заголовок подборки
    private String title;

}
