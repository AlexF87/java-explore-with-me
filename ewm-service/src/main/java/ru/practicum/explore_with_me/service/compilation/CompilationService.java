package ru.practicum.explore_with_me.service.compilation;

import ru.practicum.explore_with_me.dto.compilation.CompilationDto;
import ru.practicum.explore_with_me.dto.compilation.CompilationDtoNew;
import ru.practicum.explore_with_me.dto.compilation.UpdateCompilationRequest;

public interface CompilationService {
    CompilationDto addCompilation(CompilationDtoNew compilationDtoNew);

    void deleteCompilation(Long compId);

    CompilationDto updateCompilation(Long compId, UpdateCompilationRequest updateCompilationRequest);
}
