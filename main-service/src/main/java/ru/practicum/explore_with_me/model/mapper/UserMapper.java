package ru.practicum.explore_with_me.model.mapper;

import lombok.experimental.UtilityClass;
import ru.practicum.explore_with_me.model.User;
import ru.practicum.explore_with_me.user.NewUserRequest;
import ru.practicum.explore_with_me.user.UserDto;
import ru.practicum.explore_with_me.user.UserShortDto;

@UtilityClass
public class UserMapper {

    public User newUserToUser(NewUserRequest newUser) {
        User user = new User();
        user.setName(newUser.getName());
        user.setEmail(newUser.getEmail());
        return user;
    }

    public UserDto toUserDto(User user) {
        UserDto dto = new UserDto();
        dto.setId(user.getId());
        dto.setName(user.getName());
        dto.setEmail(user.getEmail());
        return dto;
    }

    public UserShortDto toUserShortDto(User user) {
        UserShortDto dto = new UserShortDto();
        dto.setId(user.getId());
        dto.setName(user.getName());
        return dto;
    }
}
