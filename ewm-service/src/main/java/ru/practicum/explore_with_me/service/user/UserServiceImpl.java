package ru.practicum.explore_with_me.service.user;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import ru.practicum.explore_with_me.common.CustomPageRequest;
import ru.practicum.explore_with_me.dto.user.NewUserRequest;
import ru.practicum.explore_with_me.dto.user.UserDto;
import ru.practicum.explore_with_me.handler.exception.NotFoundException;
import ru.practicum.explore_with_me.mapper.UserMapper;
import ru.practicum.explore_with_me.model.User;
import ru.practicum.explore_with_me.repository.UserRepository;

import javax.persistence.EntityManager;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {
    private final UserMapper userMapper;
    private final UserRepository userRepository;
    private final EntityManager em;

    @Override
    public List<UserDto> getUsers(List<Long> ids, Integer from, Integer size) {
        Pageable pageable = CustomPageRequest.of(from, size, Sort.by("id").ascending());
        List<User> userList = new ArrayList<>();
        if (ids == null || ids.isEmpty()) {
            userList = userRepository.findAll(pageable).getContent();
        } else {
            userList = userRepository.findAllByIdIn(ids, pageable);
        }
        return userList.stream().map(UserMapper::toUserDto).collect(Collectors.toList());
    }

    @Override
    public UserDto addUser(NewUserRequest user) {
        User newUser = userMapper.toUser(user);
        return UserMapper.toUserDto(userRepository.save(newUser));
    }

    @Override
    public void deleteUser(Long userId) {
        checkUser(userId);
        userRepository.deleteById(userId);
    }

    @Override
    public User checkUser(Long userId) {
        return userRepository.findById(userId).orElseThrow(
                () -> new NotFoundException(String.format("Not found user, id: %d ", userId)));
    }
}
