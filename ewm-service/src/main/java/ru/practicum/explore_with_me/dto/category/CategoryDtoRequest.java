package ru.practicum.explore_with_me.dto.category;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;

@Getter
@Setter
@ToString
public class CategoryDtoRequest {
    @NotBlank
    @Size(max = 200)
    private String name;
}
