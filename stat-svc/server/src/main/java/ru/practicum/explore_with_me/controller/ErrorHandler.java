package ru.practicum.explore_with_me.controller;

import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import javax.validation.ValidationException;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.io.Writer;

@RestControllerAdvice
@Slf4j
public class ErrorHandler {

    @ExceptionHandler
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public String badRequest(ValidationException e) {
        Writer buffer = new StringWriter();
        e.printStackTrace(new PrintWriter(buffer));
        log.error(buffer.toString());
        return e.getMessage();
    }
}
