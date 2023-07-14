package ru.practicum.explore_with_me.request;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;

import java.time.LocalDateTime;

import static ru.practicum.explore_with_me.DateConstant.DATE_TIME_PATTERN;

@Data
public class ParticipationRequestDto {
    @JsonFormat(pattern = DATE_TIME_PATTERN)
    private LocalDateTime created;
    private Long event;
    private Long id;
    private Long requester;
    private String status;
}
