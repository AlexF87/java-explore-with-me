package ru.practicum.explore_with_me.service.category;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.practicum.explore_with_me.common.CustomPageRequest;
import ru.practicum.explore_with_me.dto.category.CategoryDtoRequest;
import ru.practicum.explore_with_me.dto.category.CategoryDtoResponse;
import ru.practicum.explore_with_me.handler.exception.NotFoundException;
import ru.practicum.explore_with_me.mapper.CategoryMapper;
import ru.practicum.explore_with_me.model.Category;
import ru.practicum.explore_with_me.repository.CategoryRepository;

import java.util.List;
import java.util.stream.Collectors;

@RequiredArgsConstructor
@Service
@Transactional(readOnly = true)
public class CategoryServiceImpl implements CategoryService {

    private final CategoryRepository categoryRepository;
    private final CategoryMapper categoryMapper;

    @Override
    @Transactional
    public CategoryDtoResponse addCategory(CategoryDtoRequest categoryDtoRequest) {
        Category newCat = categoryMapper.toCategory(categoryDtoRequest);
        return CategoryMapper.toCategoryDtoResponse(categoryRepository.save(newCat));
    }

    @Override
    @Transactional
    public void deleteCategory(Long id) {
        checkCategory(id);
        categoryRepository.deleteById(id);
    }

    @Override
    @Transactional
    public CategoryDtoResponse updateCategory(Long id, CategoryDtoRequest categoryDtoRequest) {
        Category oldCategory = checkCategory(id);
        oldCategory.setName(categoryDtoRequest.getName());
        return CategoryMapper.toCategoryDtoResponse(oldCategory);
    }

    @Override
    public CategoryDtoResponse findById(Long id) {
        Category category = checkCategory(id);
        return CategoryMapper.toCategoryDtoResponse(category);
    }

    @Override
    public List<CategoryDtoResponse> findAll(Integer from, Integer size) {
        Pageable pageable = CustomPageRequest.of(from, size, Sort.by("name").ascending());
        List<Category> allCat = categoryRepository.findAll(pageable).getContent();
        return allCat.stream().map(CategoryMapper::toCategoryDtoResponse).collect(Collectors.toList());
    }

    @Override
    public Category checkCategory(Long id) {
        return categoryRepository.findById(id).orElseThrow(
                () -> new NotFoundException(String.format("Not found category, id: %d ", id)));
    }
}

