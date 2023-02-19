package ru.practicum.explore_with_me.dto.compilation;

import lombok.*;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;
import java.util.List;

@Value
public class CompilationDtoNew {
    private List<Long> events;
    private boolean pinned;
    @NotBlank
    @Size(max = 200)
    private String title;
}
