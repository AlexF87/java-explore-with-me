package ru.practicum.explore_with_me.dto.user;

import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class UserDto {
    private String email;
    private long id;
    private String name;
}
