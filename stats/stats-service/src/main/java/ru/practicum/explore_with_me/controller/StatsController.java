package ru.practicum.explore_with_me.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;
import ru.practicum.explore_with_me.dto.StatsDtoRequest;
import ru.practicum.explore_with_me.dto.StatsDtoRes;
import ru.practicum.explore_with_me.service.StatsService;

import javax.validation.Valid;
import java.time.LocalDateTime;
import java.util.List;

@Slf4j
@RestController
@RequiredArgsConstructor
public class StatsController {
    private final StatsService statsServiceImpl;

    @PostMapping("/hit")
    public void add(@Valid @RequestBody StatsDtoRequest statsDto) {
        log.info("POST /hit dto={},", statsDto);
        statsServiceImpl.add(statsDto);
    }

    @GetMapping("/stats")
    public List<StatsDtoRes> getStat(@RequestParam("start") LocalDateTime start,
                                     @RequestParam("end") LocalDateTime end,
                                     @RequestParam(value = "uris", defaultValue = "") List<String> uris,
                                     @RequestParam(value = "unique", defaultValue = "false") boolean unique) {
        log.info("Get stat: start = {}, end = {}, uris = {}, unique = {}", start, end, uris, unique);
        return statsServiceImpl.getStat(start, end, uris, unique);
    }
}
