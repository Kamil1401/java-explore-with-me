package ru.practicum.compilation;

import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;
import ru.practicum.compilation.dto.CompilationDto;

import java.util.List;

@RestController
@RequestMapping("compilations")
@RequiredArgsConstructor
public class CompilationPublicController {
    private CompilationService compilationService;


    @GetMapping
    public List<CompilationDto> getCompilations(@RequestParam Boolean pinned,
                                                @RequestParam(defaultValue = "0") int from,
                                                @RequestParam(defaultValue = "10") int size) {

        return compilationService.getCompilations(pinned, from, size);
    }


    @GetMapping("/{compId}")
    public CompilationDto getCompilationById(@PathVariable("compId") Long compilationId) {
        return compilationService.getAboutCompilation(compilationId);
    }
}
