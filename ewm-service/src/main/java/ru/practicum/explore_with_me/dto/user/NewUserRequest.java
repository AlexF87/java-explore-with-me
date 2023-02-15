package ru.practicum.explore_with_me.dto.user;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

import javax.validation.constraints.NotBlank;

@Getter
@Setter
@ToString
@NoArgsConstructor
public class NewUserRequest {
    @NotBlank
    private String email;
    @NotBlank
    private String name;
}
