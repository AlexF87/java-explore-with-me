package ru.practicum.explore_with_me.dto.category;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Builder
public class CategoryDtoResponse {
    private long id;
    private String name;
}
