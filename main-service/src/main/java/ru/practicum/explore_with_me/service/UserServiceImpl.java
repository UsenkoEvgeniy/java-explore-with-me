package ru.practicum.explore_with_me.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.practicum.explore_with_me.model.User;
import ru.practicum.explore_with_me.model.exceptions.NotFoundException;
import ru.practicum.explore_with_me.model.mapper.UserMapper;
import ru.practicum.explore_with_me.repository.UserRepository;
import ru.practicum.explore_with_me.user.NewUserRequest;
import ru.practicum.explore_with_me.user.UserDto;
import ru.practicum.explore_with_me.utils.CustomPage;

import java.util.List;
import java.util.stream.Collectors;

@Service
@Transactional
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {
    private final UserRepository userRepository;

    @Override
    @Transactional(readOnly = true)
    public List<UserDto> getUsers(List<Long> ids, Integer from, Integer size) {
        return userRepository.findByIds(ids, new CustomPage(from, size)).stream()
                .map(UserMapper::toUserDto).collect(Collectors.toList());

    }

    @Override
    public UserDto createUser(NewUserRequest newUser) {
        User user = userRepository.save(UserMapper.newUserToUser(newUser));
        return UserMapper.toUserDto(user);
    }

    @Override
    public void delete(Long userId) {
        if (!userRepository.existsById(userId)) {
            throw new NotFoundException("User", userId);
        }
        userRepository.deleteById(userId);
    }
}
