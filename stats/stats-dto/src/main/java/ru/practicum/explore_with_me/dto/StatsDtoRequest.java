package ru.practicum.explore_with_me.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class StatsDtoRequest {
    private Long id;
    @NotNull
    @NotBlank
    private String app;

    @NotNull
    @NotBlank
    private String uri;
    @NotNull
    @NotBlank
    private String ip;

    @NotNull
    private String timestamp;
}
