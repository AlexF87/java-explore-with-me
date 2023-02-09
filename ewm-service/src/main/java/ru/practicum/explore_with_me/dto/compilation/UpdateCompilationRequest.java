package ru.practicum.explore_with_me.dto.compilation;

import java.util.List;

public class UpdateCompilationRequest {
    private List<Long> events;
    private boolean pinned;
    private String title;
}
