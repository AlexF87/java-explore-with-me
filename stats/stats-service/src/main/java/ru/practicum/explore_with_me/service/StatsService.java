package ru.practicum.explore_with_me.service;

import ru.practicum.explore_with_me.dto.StatsDtoRequest;
import ru.practicum.explore_with_me.dto.StatsDtoRes;

import java.util.List;

public interface StatsService {

    void add(StatsDtoRequest statsDto);

    List<StatsDtoRes> getStat(String start, String end, List<String> uris, boolean unique);
}