package ru.practicum.explore_with_me.service;

import ru.practicum.explore_with_me.dto.StatsDtoRes;
import ru.practicum.explore_with_me.dto.StatsDtoRequest;

import java.time.LocalDateTime;
import java.util.List;

public interface StatsService {

    void add(StatsDtoRequest statsDto);

    List<StatsDtoRes> getStat(LocalDateTime start, LocalDateTime end, List<String> uris, boolean unique);
}