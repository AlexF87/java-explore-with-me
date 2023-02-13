package ru.practicum.explore_with_me.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import ru.practicum.explore_with_me.dto.compilation.CompilationDto;
import ru.practicum.explore_with_me.service.compilation.CompilationService;

import java.util.List;

@Slf4j
@Validated
@RestController
@RequestMapping("/compilations")
@RequiredArgsConstructor
public class CompilationControllerPub {

    private final CompilationService compilationService;

    @GetMapping
    @ResponseStatus(HttpStatus.OK)
    public List<CompilationDto> getCompilations(@RequestParam(required = false, defaultValue = "false") Boolean pinned,
                                                @RequestParam(defaultValue = "0") Integer from,
                                                @RequestParam(defaultValue = "10") Integer size) {
        log.info("GET /compilations pinned {}, from {}, size {}", pinned, from, size);
        return compilationService.getCompilations(pinned, from, size);
    }

    @GetMapping("/{compId}")
    @ResponseStatus(HttpStatus.OK)
    public CompilationDto getCompilationById(@PathVariable Long compId) {
        log.info("GET /compilations compId {}", compId);
        return compilationService.getCompilationById(compId);
    }
}
