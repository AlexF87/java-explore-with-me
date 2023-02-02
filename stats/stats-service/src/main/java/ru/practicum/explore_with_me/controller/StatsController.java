package ru.practicum.explore_with_me.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;
import ru.practicum.explore_with_me.dto.StatsDtoRequest;
import ru.practicum.explore_with_me.dto.StatsDtoRes;
import ru.practicum.explore_with_me.service.StatsServiceImpl;

import javax.validation.Valid;
import java.util.List;

@Slf4j
@RestController
@RequiredArgsConstructor
public class StatsController {
    private final StatsServiceImpl statsServiceImpl;

    @PostMapping("/hit")
    public void add(@Valid @RequestBody StatsDtoRequest statsDto) {
        log.info("POST /hit dto={},", statsDto);
        statsServiceImpl.add(statsDto);
    }

    @GetMapping("/stats")
    public List<StatsDtoRes> getStat(@RequestParam("start") String start,
                                     @RequestParam("end") String end,
                                     @RequestParam(value = "uris", required = false) List<String> uris,
                                     @RequestParam(value = "unique", defaultValue = "false") boolean unique) {
        log.info("Get stat: start = {}, end = {}, uris = {}, unique = {}", start, end, uris, unique);
        return statsServiceImpl.getStat(start, end, uris, unique);
    }

}
