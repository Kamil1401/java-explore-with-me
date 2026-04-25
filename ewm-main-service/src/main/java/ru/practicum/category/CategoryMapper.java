package ru.practicum.category;

import ru.practicum.category.dto.CategoryDto;
import ru.practicum.category.dto.NewCategoryDto;

import java.util.List;

public class CategoryMapper {

    public static Category toEntity(NewCategoryDto dto) {
        Category category = new Category();
        category.setName(dto.getName());

        return category;
    }


    public static CategoryDto toDto(Category category) {
        return new CategoryDto(category.getId(), category.getName());
    }


    public static List<CategoryDto> toListDtos(List<Category> categories) {
        return categories.stream()
                .map(CategoryMapper::toDto)
                .toList();
    }
}