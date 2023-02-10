package ru.practicum.explore_with_me.service.category;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.practicum.explore_with_me.dto.category.CategoryDtoRequest;
import ru.practicum.explore_with_me.dto.category.CategoryDtoResponse;
import ru.practicum.explore_with_me.handler.exception.NotFoundException;
import ru.practicum.explore_with_me.mapper.CategoryMapper;
import ru.practicum.explore_with_me.model.Category;
import ru.practicum.explore_with_me.repository.CategoryRepository;

@RequiredArgsConstructor
@Service
public class CategoryServiceImpl implements CategoryService {

    private final CategoryRepository categoryRepository;
    private final CategoryMapper categoryMapper;

    @Override
    public CategoryDtoResponse addCategory(CategoryDtoRequest categoryDtoRequest) {
        Category newCat = categoryMapper.toCategory(categoryDtoRequest);
        return CategoryMapper.toCategoryDtoResponse(categoryRepository.save(newCat));
    }

    @Override
    public void deleteCategory(Long id) {
        checkCategory(id);
        categoryRepository.deleteById(id);
    }

    @Override
    public CategoryDtoResponse updateCategory(Long id, CategoryDtoRequest categoryDtoRequest) {
        Category oldCategory = checkCategory(id);
        oldCategory.setName(categoryDtoRequest.getName());
        return CategoryMapper.toCategoryDtoResponse(categoryRepository.save(oldCategory));
    }

    private Category checkCategory(Long id) {
        return categoryRepository.findById(id).orElseThrow(
                () -> new NotFoundException(String.format("Not found category, id: %d ", id)));
    }
}
