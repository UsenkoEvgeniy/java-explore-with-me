package ru.practicum.explore_with_me.event;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;
import ru.practicum.explore_with_me.category.CategoryDto;
import ru.practicum.explore_with_me.user.UserShortDto;

import java.time.LocalDateTime;

import static ru.practicum.explore_with_me.DateConstant.DATE_TIME_PATTERN;

@Data
public class EventShortDto {
    private Long id;
    private String annotation;
    private CategoryDto category;
    private Integer confirmedRequests;
    @JsonFormat(pattern = DATE_TIME_PATTERN)
    private LocalDateTime eventDate;
    private UserShortDto initiator;
    private Boolean paid;
    private String title;
    private Integer views;
    private Integer likes = 0;
}
