package ru.practicum.explore_with_me.service.category;

import ru.practicum.explore_with_me.dto.category.CategoryDtoRequest;
import ru.practicum.explore_with_me.dto.category.CategoryDtoResponse;
import ru.practicum.explore_with_me.model.Category;

import java.util.List;

public interface CategoryService {
    CategoryDtoResponse addCategory(CategoryDtoRequest categoryDtoRequest);

    void deleteCategory(Long id);

    CategoryDtoResponse updateCategory(Long id, CategoryDtoRequest categoryDtoRequest);

    CategoryDtoResponse findById(Long id);

    List<CategoryDtoResponse>findAll(Integer from, Integer size);

    Category checkCategory(Long id);
}
