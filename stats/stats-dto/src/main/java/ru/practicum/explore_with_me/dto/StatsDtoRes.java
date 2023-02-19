package ru.practicum.explore_with_me.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class StatsDtoRes {
    private String app;
    private String uri;
    private Long hits;
}
