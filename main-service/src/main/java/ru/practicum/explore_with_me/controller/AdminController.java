package ru.practicum.explore_with_me.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;
import ru.practicum.explore_with_me.category.CategoryDto;
import ru.practicum.explore_with_me.category.NewCategoryDto;
import ru.practicum.explore_with_me.compilation.CompilationDto;
import ru.practicum.explore_with_me.compilation.NewCompilationDto;
import ru.practicum.explore_with_me.compilation.UpdateCompilationRequest;
import ru.practicum.explore_with_me.event.EventFullDto;
import ru.practicum.explore_with_me.event.UpdateEventAdminRequest;
import ru.practicum.explore_with_me.model.State;
import ru.practicum.explore_with_me.service.CategoryService;
import ru.practicum.explore_with_me.service.CompilationService;
import ru.practicum.explore_with_me.service.EventService;
import ru.practicum.explore_with_me.service.UserService;
import ru.practicum.explore_with_me.user.NewUserRequest;
import ru.practicum.explore_with_me.user.UserDto;

import javax.validation.Valid;
import java.time.LocalDateTime;
import java.util.List;

import static ru.practicum.explore_with_me.DateConstant.DATE_TIME_PATTERN;

@RestController
@Slf4j
@RequestMapping("/admin")
@RequiredArgsConstructor
public class AdminController {
    private final EventService eventService;
    private final UserService userService;
    private final CategoryService categoryService;
    private final CompilationService compilationService;

    @GetMapping("/users")
    public List<UserDto> getUsers(@RequestParam(required = false) List<Long> ids,
                                  @RequestParam(defaultValue = "0") Integer from,
                                  @RequestParam(defaultValue = "10") Integer size) {
        log.info("Get request for users: {}, from = {}, size = {}", ids, from, size);
        return userService.getUsers(ids, from, size);
    }

    @ResponseStatus(HttpStatus.CREATED)
    @PostMapping("/users")
    public UserDto createUser(@Valid @RequestBody NewUserRequest newUser) {
        log.info("Post request with user {}", newUser);
        return userService.createUser(newUser);
    }

    @ResponseStatus(HttpStatus.NO_CONTENT)
    @DeleteMapping("/users/{userId}")
    public void deleteUser(@PathVariable Long userId) {
        log.info("Delete request for userId {}", userId);
        userService.delete(userId);
    }

    @ResponseStatus(HttpStatus.CREATED)
    @PostMapping("/categories")
    public CategoryDto createCategory(@Valid @RequestBody NewCategoryDto category) {
        log.info("Post request for category {}", category);
        return categoryService.create(category);
    }

    @ResponseStatus(HttpStatus.NO_CONTENT)
    @DeleteMapping("/categories/{catId}")
    public void deleteCategory(@PathVariable Long catId) {
        log.info("Delete request for categoryId {}", catId);
        categoryService.delete(catId);
    }

    @PatchMapping("/categories/{catId}")
    public CategoryDto updateCategory(@PathVariable Long catId,
                                      @Valid @RequestBody CategoryDto dto) {
        dto.setId(catId);
        log.info("Patch request for category {}", dto);
        return categoryService.update(dto);
    }

    @GetMapping("/events")
    public List<EventFullDto> getEventsByParams(@RequestParam(required = false) List<Long> users,
                                                @RequestParam(required = false) List<State> states,
                                                @RequestParam(required = false) List<Long> categories,
                                                @RequestParam(required = false) @DateTimeFormat(pattern = DATE_TIME_PATTERN) LocalDateTime rangeStart,
                                                @RequestParam(required = false) @DateTimeFormat(pattern = DATE_TIME_PATTERN) LocalDateTime rangeEnd,
                                                @RequestParam(defaultValue = "0") Integer from,
                                                @RequestParam(defaultValue = "10") Integer size) {
        log.info("Get request for events by admin users={} states={} categories={} rangeStart={} rangeEnd={} " +
                "from={}, size={}", users, states, categories, rangeStart, rangeEnd, from, size);
        return eventService.getEventsByAdminParams(users, states, categories, rangeStart, rangeEnd, from, size);
    }

    @PatchMapping("/events/{eventId}")
    public EventFullDto updateEventByAdmin(@PathVariable Long eventId,
                                           @Valid @RequestBody UpdateEventAdminRequest updateRequest) {
        log.info("Patch request {} by admin for eventId {}", updateRequest, eventId);
        return eventService.updateEventByAdmin(eventId, updateRequest);
    }

    @ResponseStatus(HttpStatus.CREATED)
    @PostMapping("/compilations")
    public CompilationDto createCompilation(@Valid @RequestBody NewCompilationDto dto) {
        log.info("Post request to create compilation {}", dto);
        return compilationService.createCompilation(dto);
    }

    @ResponseStatus(HttpStatus.NO_CONTENT)
    @DeleteMapping("/compilations/{compId}")
    public void deleteCompilation(@PathVariable Long compId) {
        log.info("Delete request for compilation {}", compId);
        compilationService.deleteCompilation(compId);
    }

    @PatchMapping("/compilations/{compId}")
    public CompilationDto updateCompilation(@PathVariable Long compId,
                                            @Valid @RequestBody UpdateCompilationRequest request) {
        log.info("Patch request update compilation {} with {}", compId, request);
        return compilationService.updateCompilation(compId, request);
    }
}
