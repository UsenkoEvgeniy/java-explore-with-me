package ru.practicum.explore_with_me.model.mapper;

import lombok.experimental.UtilityClass;
import ru.practicum.explore_with_me.compilation.CompilationDto;
import ru.practicum.explore_with_me.compilation.NewCompilationDto;
import ru.practicum.explore_with_me.model.Compilation;

import java.util.ArrayList;
import java.util.stream.Collectors;

@UtilityClass
public class CompilationMapper {

    public Compilation newCompilationDtoToCompilation(NewCompilationDto dto) {
        Compilation compilation = new Compilation();
        compilation.setPinned(dto.getPinned());
        compilation.setTitle(dto.getTitle());
        return compilation;
    }

    public CompilationDto toCompilationDto(Compilation compilation) {
        CompilationDto dto = new CompilationDto();
        dto.setId(compilation.getId());
        dto.setPinned(compilation.getPinned());
        dto.setTitle(compilation.getTitle());
        if (compilation.getEvents() != null && !compilation.getEvents().isEmpty()) {
            dto.setEvents(compilation.getEvents().stream().map(EventMapper::toEventShortDto).collect(Collectors.toList()));
        } else {
            dto.setEvents(new ArrayList<>());
        }
        return dto;
    }
}
