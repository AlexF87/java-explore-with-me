package ru.practicum.explore_with_me.dto.compilation;

import javax.validation.constraints.NotBlank;
import java.util.List;

public class CompilationDtoNew {
    private List<Long> events;
    private boolean pinned;
    @NotBlank
    private String title;

}
