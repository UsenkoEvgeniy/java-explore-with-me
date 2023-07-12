package ru.practicum.explore_with_me.service;

import ru.practicum.explore_with_me.compilation.CompilationDto;
import ru.practicum.explore_with_me.compilation.NewCompilationDto;
import ru.practicum.explore_with_me.compilation.UpdateCompilationRequest;

import java.util.List;

public interface CompilationService {
    CompilationDto createCompilation(NewCompilationDto dto);

    void deleteCompilation(Long compId);

    List<CompilationDto> getCompilations(Boolean pinned, Integer from, Integer size);

    CompilationDto getCompilation(Long compId);

    CompilationDto updateCompilation(Long compId, UpdateCompilationRequest request);
}
