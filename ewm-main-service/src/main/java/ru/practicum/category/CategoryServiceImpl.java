package ru.practicum.category;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.practicum.category.dto.CategoryDto;
import ru.practicum.category.dto.NewCategoryDto;
import ru.practicum.event.EventRepository;
import ru.practicum.exception.DeleteException;
import ru.practicum.exception.DuplicateException;
import ru.practicum.exception.NotFoundException;

import java.util.List;

@Service
@RequiredArgsConstructor
public class CategoryServiceImpl implements CategoryService {
    private final CategoryRepository categoryRepository;
    private final EventRepository eventRepository;


    @Override
    public Category getCategoryById(Long categoryId) {
        return categoryRepository.findById(categoryId)
                .orElseThrow(() -> new NotFoundException("Категория по ID " + categoryId + " не найдена"));
    }


//    A D M I N _ A P I

    @Transactional
    @Override
    public CategoryDto createCategory(NewCategoryDto dto) {
        if (categoryRepository.findByName(dto.getName()).isPresent()) {
            throw new DuplicateException("Категория с таким именем уже существует");
        }
        Category category = CategoryMapper.toEntity(dto);
        Category savedCategory = categoryRepository.save(category);

        return CategoryMapper.toDto(savedCategory);
    }


    @Transactional
    @Override
    public CategoryDto updateCategory(CategoryDto dto, Long categoryId) {
        Category category = getCategoryById(categoryId);

        if (dto.getName() != null) {
            category.setName(dto.getName());
        }
        categoryRepository.save(category);

        return CategoryMapper.toDto(category);
    }


    @Transactional
    @Override
    public void deleteCategory(Long categoryId) {
        getCategoryById(categoryId);

        if (eventRepository.existsByCategoryId(categoryId)) {
            throw new DeleteException("С данной категорией связаны события");
        }

        categoryRepository.deleteById(categoryId);
    }


//    P U B L I C _ A P I

    @Override
    public List<CategoryDto> getCategories(int from, int size) {
        List<Category> categories = categoryRepository.findAll(from, size);

        if (categories.isEmpty()) {
            return List.of();
        }

        return CategoryMapper.toListDtos(categories);
    }


    @Override
    public CategoryDto getAboutCategory(Long categoryId) {
        Category category = getCategoryById(categoryId);

        return CategoryMapper.toDto(category);
    }
}