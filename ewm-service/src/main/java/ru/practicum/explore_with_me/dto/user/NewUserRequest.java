package ru.practicum.explore_with_me.dto.user;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.validation.constraints.NotBlank;
@Getter
@Setter
@NoArgsConstructor
public class NewUserRequest {
    @NotBlank
    private String email;
    @NotBlank
    private String name;
}
