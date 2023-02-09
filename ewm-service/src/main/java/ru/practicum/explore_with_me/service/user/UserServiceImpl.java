package ru.practicum.explore_with_me.service.user;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.practicum.explore_with_me.dto.user.NewUserRequest;
import ru.practicum.explore_with_me.dto.user.UserDto;
import ru.practicum.explore_with_me.mapper.UserMapper;
import ru.practicum.explore_with_me.model.User;

import java.util.List;
@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {
    UserMapper userMapper;
    @Override
    public List<UserDto> getUsers(List<Long> ids, Integer from, Integer size) {
        return null;
    }

    @Override
    public UserDto addUser(NewUserRequest user) {
        User newUser = userMapper.toUser(user);
        return null;
    }

    @Override
    public void deleteUser(Long userId) {

    }
}
