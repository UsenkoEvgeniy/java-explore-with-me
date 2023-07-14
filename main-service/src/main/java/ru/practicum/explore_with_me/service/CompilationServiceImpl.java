package ru.practicum.explore_with_me.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.practicum.explore_with_me.compilation.CompilationDto;
import ru.practicum.explore_with_me.compilation.NewCompilationDto;
import ru.practicum.explore_with_me.compilation.UpdateCompilationRequest;
import ru.practicum.explore_with_me.model.Compilation;
import ru.practicum.explore_with_me.model.Event;
import ru.practicum.explore_with_me.model.exceptions.NotFoundException;
import ru.practicum.explore_with_me.model.mapper.CompilationMapper;
import ru.practicum.explore_with_me.repository.CompilationRepository;
import ru.practicum.explore_with_me.repository.EventRepository;
import ru.practicum.explore_with_me.utils.CustomPage;

import java.util.List;
import java.util.stream.Collectors;

@Service
@Transactional
@RequiredArgsConstructor
public class CompilationServiceImpl implements CompilationService {
    private final CompilationRepository compilationRepository;
    private final EventRepository eventRepository;

    @Override
    public CompilationDto createCompilation(NewCompilationDto dto) {
        Compilation compilation = CompilationMapper.newCompilationDtoToCompilation(dto);
        if (dto.getEvents() != null && !dto.getEvents().isEmpty()) {
            compilation.setEvents(eventRepository.findAllById(dto.getEvents()));
        }
        return CompilationMapper.toCompilationDto(compilationRepository.save(compilation));
    }

    @Override
    public void deleteCompilation(Long compId) {
        Compilation compilation = compilationRepository.findById(compId)
                .orElseThrow(() -> new NotFoundException("Compilation", compId));
        compilationRepository.delete(compilation);
    }

    @Override
    @Transactional(readOnly = true)
    public List<CompilationDto> getCompilations(Boolean pinned, Integer from, Integer size) {
        CustomPage pageable = new CustomPage(from, size);
        return compilationRepository.findByPinned(pinned, pageable).stream()
                .map(CompilationMapper::toCompilationDto).collect(Collectors.toList());
    }

    @Override
    @Transactional(readOnly = true)
    public CompilationDto getCompilation(Long compId) {
        return CompilationMapper.toCompilationDto(compilationRepository.findById(compId)
                .orElseThrow(() -> new NotFoundException("Compilation", compId)));
    }

    @Override
    public CompilationDto updateCompilation(Long compId, UpdateCompilationRequest request) {
        Compilation compilation = compilationRepository.findById(compId)
                .orElseThrow(() -> new NotFoundException("Compilation", compId));
        if (request.getEvents() != null) {
            List<Event> events = request.getEvents().stream().map(id -> {
                Event event = new Event();
                event.setId(id);
                return event;
            }).collect(Collectors.toList());
            compilation.setEvents(events);
        }
        if (request.getPinned() != null) {
            compilation.setPinned(request.getPinned());
        }
        if (request.getTitle() != null) {
            compilation.setTitle(request.getTitle());
        }
        return CompilationMapper.toCompilationDto(compilationRepository.save(compilation));
    }
}
