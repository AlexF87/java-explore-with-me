package ru.practicum.explore_with_me.service.compilation;

import ru.practicum.explore_with_me.dto.compilation.CompilationDto;
import ru.practicum.explore_with_me.dto.compilation.CompilationDtoNew;
import ru.practicum.explore_with_me.dto.compilation.UpdateCompilationRequest;
import ru.practicum.explore_with_me.model.Compilation;

import java.util.List;

public interface CompilationService {
    CompilationDto addCompilation(CompilationDtoNew compilationDtoNew);

    void deleteCompilation(Long compId);

    CompilationDto updateCompilation(Long compId, UpdateCompilationRequest updateCompilationRequest);

    List<CompilationDto> getCompilations(Boolean pinned, Integer from, Integer size);
    CompilationDto getCompilationById(Long compId);
}
