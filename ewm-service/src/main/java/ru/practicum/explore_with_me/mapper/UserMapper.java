package ru.practicum.explore_with_me.mapper;

import org.springframework.stereotype.Component;
import ru.practicum.explore_with_me.dto.user.NewUserRequest;
import ru.practicum.explore_with_me.dto.user.UserDto;
import ru.practicum.explore_with_me.model.User;

@Component
public class UserMapper {
    public User toUser(NewUserRequest dto) {
        User user = new User();
        user.setEmail(dto.getEmail());
        user.setName(dto.getName());
        return user;
    }

    public static UserDto toUserDto (User user) {
        return new UserDto(user.getEmail(), user.getId(), user.getName());
    }
}
