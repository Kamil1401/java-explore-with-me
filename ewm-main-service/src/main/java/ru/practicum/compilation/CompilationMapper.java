package ru.practicum.compilation;

import ru.practicum.compilation.dto.CompilationDto;
import ru.practicum.compilation.dto.NewCompilationDto;
import ru.practicum.event.EventMapper;

import java.util.List;

public class CompilationMapper {

    public static Compilation toEntity(NewCompilationDto dto) {
        Compilation compilation = new Compilation();
        compilation.setTitle(dto.getTitle());

        return compilation;
    }

    public static CompilationDto toDto(Compilation compilation) {
        return new CompilationDto(
                EventMapper.toEventShortDtos(compilation.getEvents()),
                compilation.getId(),
                compilation.getPinned(),
                compilation.getTitle()
        );
    }

    public static List<CompilationDto> toListDtos(List<Compilation> compilations) {
        return compilations.stream()
                .map(CompilationMapper::toDto)
                .toList();
    }
}
