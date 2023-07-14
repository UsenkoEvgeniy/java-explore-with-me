package ru.practicum.explore_with_me.stats;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;
import lombok.NoArgsConstructor;
import ru.practicum.explore_with_me.DateConstant;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.Past;
import java.time.LocalDateTime;

@Data
@NoArgsConstructor
public class EndpointHitDto {

    @NotEmpty
    private String app = "main";
    @NotEmpty
    private String uri;
    @NotEmpty
    private String ip;
    @JsonFormat(pattern = DateConstant.DATE_TIME_PATTERN)
    @Past
    private LocalDateTime timestamp;

    public EndpointHitDto(String uri, String ip, LocalDateTime timestamp) {
        this.uri = uri;
        this.ip = ip;
        this.timestamp = timestamp;
    }
}
