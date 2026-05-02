package ru.practicum.category;

import ru.practicum.category.dto.CategoryDto;
import ru.practicum.category.dto.NewCategoryDto;

import java.util.List;

public interface CategoryService {

    CategoryDto createCategory(NewCategoryDto dto);

    CategoryDto updateCategory(CategoryDto dto, Long catId);

    Category getCategoryById(Long categoryId);

    void deleteCategory(Long categoryId);

    List<CategoryDto> getCategories(int from, int size);

    CategoryDto getAboutCategory(Long categoryId);
}
