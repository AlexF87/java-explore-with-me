package ru.practicum.explore_with_me.mapper;

import org.springframework.stereotype.Component;
import ru.practicum.explore_with_me.dto.category.CategoryDtoRequest;
import ru.practicum.explore_with_me.dto.category.CategoryDtoResponse;
import ru.practicum.explore_with_me.model.Category;

@Component
public class CategoryMapper {
    public Category toCategory(CategoryDtoRequest categoryDtoRequest) {
        return new Category(categoryDtoRequest.getName());
    }

    public static CategoryDtoResponse toCategoryDtoResponse(Category category) {
        CategoryDtoResponse categoryDtoResponse = CategoryDtoResponse.builder()
                .id(category.getId())
                .name(category.getName())
                .build();
        return categoryDtoResponse;
    }
}
