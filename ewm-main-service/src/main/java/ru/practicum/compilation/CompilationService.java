package ru.practicum.compilation;

import ru.practicum.compilation.dto.CompilationDto;
import ru.practicum.compilation.dto.NewCompilationDto;
import ru.practicum.compilation.dto.UpdateCompilationRequest;

import java.util.List;

public interface CompilationService {

    CompilationDto createCompilation(NewCompilationDto dto);

    CompilationDto updateCompilation(UpdateCompilationRequest request, Long compilationId);

    void deleteCompilation(Long compilationId);

    List<CompilationDto> getCompilations(Boolean pinned, int from, int size);

    CompilationDto getAboutCompilation(Long compilationId);
}
