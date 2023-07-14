package ru.practicum.explore_with_me.event;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;
import org.hibernate.validator.constraints.Length;

import javax.validation.constraints.AssertTrue;
import java.time.LocalDateTime;

import static ru.practicum.explore_with_me.DateConstant.DATE_TIME_PATTERN;

@Data
public class UpdateEventAdminRequest {
    @Length(min = 20, max = 2000)
    private String annotation;
    private Long category;
    @Length(min = 20, max = 7000)
    private String description;
    @JsonFormat(pattern = DATE_TIME_PATTERN)
    private LocalDateTime eventDate;
    private LocationDto location;
    private Boolean paid;
    private Integer participantLimit;
    private Boolean requestModeration;
    @Length(min = 3, max = 120)
    private String title;
    private String stateAction;

    @AssertTrue(message = "Event date can't be earlier then one hour from now")
    public boolean isAfterTwoHours() {
        return eventDate == null || eventDate.isAfter(LocalDateTime.now().plusHours(1));
    }
}
