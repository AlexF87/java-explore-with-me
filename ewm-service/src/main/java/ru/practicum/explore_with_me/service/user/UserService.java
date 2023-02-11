package ru.practicum.explore_with_me.service.user;

import ru.practicum.explore_with_me.dto.event.EventShortDto;
import ru.practicum.explore_with_me.dto.user.NewUserRequest;
import ru.practicum.explore_with_me.dto.user.UserDto;
import ru.practicum.explore_with_me.model.User;

import java.time.LocalDateTime;
import java.util.List;

public interface UserService {
    List<UserDto> getUsers(List<Long> ids, Integer from, Integer size);

    UserDto addUser(NewUserRequest user);

    void deleteUser(Long userId);

    User checkUser(Long userId);
}
