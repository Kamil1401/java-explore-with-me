package ru.practicum.compilation;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ru.practicum.compilation.dto.CompilationDto;
import ru.practicum.compilation.dto.NewCompilationDto;
import ru.practicum.compilation.dto.UpdateCompilationRequest;

@RestController
@RequestMapping("/admin/compilations")
@RequiredArgsConstructor
public class CompilationAdminController {
    private final CompilationService compilationService;


    @PostMapping
    public ResponseEntity<CompilationDto> createCompilation(@Valid @RequestBody NewCompilationDto dto) {
        CompilationDto result = compilationService.createCompilation(dto);

        return ResponseEntity.status(HttpStatus.CREATED).body(result);
    }


    @PatchMapping("/{compId}")
    public CompilationDto updateCompilation(@Valid @RequestBody UpdateCompilationRequest request,
                                            @PathVariable("compId") Long compilationId) {

        return compilationService.updateCompilation(request, compilationId);
    }


    @DeleteMapping("/{compId}")
    public ResponseEntity<Void> deleteCompilation(@PathVariable("compId") Long compilationId) {
        compilationService.deleteCompilation(compilationId);

        return ResponseEntity.noContent().build();
    }
}
