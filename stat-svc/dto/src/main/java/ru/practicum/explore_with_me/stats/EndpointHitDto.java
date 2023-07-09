package ru.practicum.explore_with_me.stats;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;
import ru.practicum.explore_with_me.DateConstant;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.Past;
import java.time.LocalDateTime;

@Data
public class EndpointHitDto {

    @NotEmpty
    private String app;
    @NotEmpty
    private String uri;
    @NotEmpty
    private String ip;
    @JsonFormat(pattern = DateConstant.DATE_TIME_PATTERN)
    @Past
    private LocalDateTime timestamp;
}
