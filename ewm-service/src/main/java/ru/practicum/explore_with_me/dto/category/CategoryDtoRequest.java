package ru.practicum.explore_with_me.dto.category;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import javax.validation.constraints.NotBlank;

@Getter
@Setter
@ToString
public class CategoryDtoRequest {
    @NotBlank
    private String name;
}
