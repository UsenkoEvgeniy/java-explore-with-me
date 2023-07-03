package ru.practicum.explore_with_me;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.Past;
import java.time.LocalDateTime;

@Data
public class EndpointHitDto {
    public static final String DATE_TIME_PATTERN = "yyyy-MM-dd HH:mm:ss";

    @NotEmpty
    private String app;
    @NotEmpty
    private String uri;
    @NotEmpty
    private String ip;
    @JsonFormat(pattern = DATE_TIME_PATTERN)
    @Past
    private LocalDateTime timestamp;
}
