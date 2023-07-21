package ru.practicum.explore_with_me.service;

import ru.practicum.explore_with_me.user.NewUserRequest;
import ru.practicum.explore_with_me.user.UserDto;
import ru.practicum.explore_with_me.user.UserRatingDto;

import java.util.List;

public interface UserService {
    List<UserDto> getUsers(List<Long> ids, Integer from, Integer size);

    UserDto createUser(NewUserRequest newUser);

    void delete(Long userId);

    List<UserRatingDto> getUsersByRating(Integer from, Integer size);

    UserRatingDto getUserWithRating(Long userId);
}
