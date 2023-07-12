package ru.practicum.explore_with_me.model.exceptions;

public class NotFoundException extends RuntimeException {
    public NotFoundException(String message, Long id) {
        super(message + " with id=" + id + " was not found");
    }
}
