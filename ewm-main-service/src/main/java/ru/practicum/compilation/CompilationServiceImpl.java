package ru.practicum.compilation;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.practicum.compilation.dto.CompilationDto;
import ru.practicum.compilation.dto.NewCompilationDto;
import ru.practicum.compilation.dto.UpdateCompilationRequest;
import ru.practicum.event.Event;
import ru.practicum.event.EventRepository;
import ru.practicum.exception.DuplicateException;
import ru.practicum.exception.NotFoundException;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Service
@RequiredArgsConstructor
public class CompilationServiceImpl implements CompilationService {
    private final CompilationRepository compilationRepository;
    private final EventRepository eventRepository;


    public Compilation getCompilationById(Long compilationId) {
        return compilationRepository.findById(compilationId)
                .orElseThrow(() -> new NotFoundException("Подборка с ID " + compilationId + " не найдена"));
    }


//    A D M I N _ A P I

    @Override
    public CompilationDto createCompilation(NewCompilationDto dto) {
        Compilation compilation = CompilationMapper.toEntity(dto);

        if (compilationRepository.findByName(compilation.getTitle()).isPresent()) {
            throw new DuplicateException("Категория с таким именем уже существует");
        }

        if (dto.getEvents() == null || dto.getEvents().isEmpty()) {
            compilation.setEvents(Set.of());
        } else {
            Set<Event> events = new HashSet<>(eventRepository.findAllById(dto.getEvents()));

            if (dto.getEvents().size() != events.size()) {
                throw new NotFoundException("Некоторые события не найдены или не существуют");
            }
            compilation.setEvents(events);
        }

        Compilation savedCompilation = compilationRepository.save(compilation);

        return CompilationMapper.toDto(savedCompilation);
    }


    @Override
    public CompilationDto updateCompilation(UpdateCompilationRequest request, Long compilationId) {
        Compilation compilation = getCompilationById(compilationId);

        if (request.getTitle() != null) {
            if (compilationRepository.findByName(compilation.getTitle()).isPresent()) {
                throw new DuplicateException("Категория с таким именем уже существует");
            }
            compilation.setTitle(request.getTitle());
        }

        if (request.getPinned() != null) {
            compilation.setPinned(request.getPinned());
        }

        if (request.getEvents() != null) {
            Set<Event> events = new HashSet<>(eventRepository.findAllById(request.getEvents()));

            if (request.getEvents().size() != events.size()) {
                throw new NotFoundException("Некоторые события не найдены или не существуют");
            }
            compilation.setEvents(events);
        }
        compilationRepository.save(compilation);

        return CompilationMapper.toDto(compilation);
    }


    @Override
    public void deleteCompilation(Long compilationId) {
        getCompilationById(compilationId);

        compilationRepository.deleteById(compilationId);
    }


//    P U B L I C _ A P I

    @Override
    public List<CompilationDto> getCompilations(Boolean pinned, int from, int size) {
        List<Compilation> compilations = compilationRepository.findAll(pinned, from, size);

        if (compilations.isEmpty()) {
            return List.of();
        }

        return CompilationMapper.toListDtos(compilations);
    }


    @Override
    public CompilationDto getAboutCompilation(Long compilationId) {
        return CompilationMapper.toDto(getCompilationById(compilationId));
    }
}
