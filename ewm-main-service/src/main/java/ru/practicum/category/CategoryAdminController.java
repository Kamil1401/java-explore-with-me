package ru.practicum.category;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ru.practicum.category.dto.CategoryDto;
import ru.practicum.category.dto.NewCategoryDto;

@RestController
@RequestMapping("/admin/categories")
@RequiredArgsConstructor
public class CategoryAdminController {
    private final CategoryService categoryService;


    @PostMapping
    public ResponseEntity<CategoryDto> createCategory(@Valid @RequestBody NewCategoryDto dto) {
        CategoryDto result = categoryService.createCategory(dto);

        return ResponseEntity.status(HttpStatus.CREATED).body(result);
    }


    @PatchMapping("/{catId}")
    public CategoryDto updateCategory(@Valid @RequestBody CategoryDto dto,
                                      @PathVariable("catId") Long categoryId) {

        return categoryService.updateCategory(dto, categoryId);
    }


    @DeleteMapping("/{catId}")
    public ResponseEntity<Void> deleteUser(@PathVariable("catId") Long categoryId) {
        categoryService.deleteCategory(categoryId);

        return ResponseEntity.noContent().build();
    }
}