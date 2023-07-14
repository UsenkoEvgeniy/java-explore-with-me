package ru.practicum.explore_with_me.service;

import ru.practicum.explore_with_me.user.NewUserRequest;
import ru.practicum.explore_with_me.user.UserDto;

import java.util.List;

public interface UserService {
    List<UserDto> getUsers(List<Long> ids, Integer from, Integer size);

    UserDto createUser(NewUserRequest newUser);

    void delete(Long userId);
}
