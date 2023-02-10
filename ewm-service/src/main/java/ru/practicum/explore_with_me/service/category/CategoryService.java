package ru.practicum.explore_with_me.service.category;

import ru.practicum.explore_with_me.dto.category.CategoryDtoRequest;
import ru.practicum.explore_with_me.dto.category.CategoryDtoResponse;

public interface CategoryService {
    CategoryDtoResponse addCategory(CategoryDtoRequest categoryDtoRequest);

    void deleteCategory(Long id);

    CategoryDtoResponse updateCategory(Long id, CategoryDtoRequest categoryDtoRequest);
}
