package ru.practicum.explore_with_me.dto.user;

import javax.validation.constraints.NotBlank;

public class NewUserRequest {
    @NotBlank
    private String email;
    @NotBlank
    private String name;
}
