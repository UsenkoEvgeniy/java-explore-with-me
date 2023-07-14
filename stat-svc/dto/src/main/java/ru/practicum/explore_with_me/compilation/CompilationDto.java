package ru.practicum.explore_with_me.compilation;

import lombok.Data;
import ru.practicum.explore_with_me.event.EventShortDto;

import java.util.List;

@Data
public class CompilationDto {
    private Long id;
    private List<EventShortDto> events;
    private Boolean pinned;
    private String title;
}
