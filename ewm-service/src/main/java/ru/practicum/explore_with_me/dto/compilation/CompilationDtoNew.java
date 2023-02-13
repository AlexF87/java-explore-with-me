package ru.practicum.explore_with_me.dto.compilation;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import javax.validation.constraints.NotBlank;
import java.util.List;

@Getter
@Setter
@NotBlank
@ToString
@AllArgsConstructor
public class CompilationDtoNew {
    private List<Long> events;
    private boolean pinned;
    @NotBlank
    private String title;

}
