package ru.practicum.explore_with_me.controller.admin;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;
import ru.practicum.explore_with_me.repository.UserRepository;
import ru.practicum.explore_with_me.service.UserService;
import ru.practicum.explore_with_me.user.NewUserRequest;
import ru.practicum.explore_with_me.user.UserDto;
import ru.practicum.explore_with_me.user.UserRatingDto;

import javax.validation.Valid;
import javax.validation.constraints.Positive;
import javax.validation.constraints.PositiveOrZero;
import java.util.List;

@RestController
@Slf4j
@RequestMapping("admin/users")
@RequiredArgsConstructor
@Validated
public class AdminUserController {
    private final UserRepository userRepository;
    private final UserService userService;

    @GetMapping
    @Validated
    public List<UserDto> getUsers(@RequestParam(required = false) List<Long> ids,
                                  @RequestParam(defaultValue = "0") @PositiveOrZero Integer from,
                                  @RequestParam(defaultValue = "10") @Positive Integer size) {
        log.info("Get request for users: {}, from = {}, size = {}", ids, from, size);
        return userService.getUsers(ids, from, size);
    }

    @ResponseStatus(HttpStatus.CREATED)
    @PostMapping
    public UserDto createUser(@Valid @RequestBody NewUserRequest newUser) {
        log.info("Post request with user {}", newUser);
        return userService.createUser(newUser);
    }

    @ResponseStatus(HttpStatus.NO_CONTENT)
    @DeleteMapping("/{userId}")
    public void deleteUser(@PathVariable Long userId) {
        log.info("Delete request for userId {}", userId);
        userService.delete(userId);
    }

    //Рейтинги пользователей формируются на основании суммы рейтингов опубликованных ими событий
    @GetMapping("/rating")
    public List<UserRatingDto> getUsersByRating(@RequestParam(defaultValue = "0") @PositiveOrZero Integer from,
                                                @RequestParam(defaultValue = "10") @Positive Integer size) {
        log.info("Get request for all users by rating from {} size {}", from, size);
        return userService.getUsersByRating(from, size);
    }

    @GetMapping("/{userId}/rating")
    public UserRatingDto getUserWithRating(@PathVariable Long userId) {
        log.info("Get request for user rating for id {}", userId);
        return userService.getUserWithRating(userId);
    }
}