package ru.practicum.explore_with_me.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import ru.practicum.explore_with_me.dto.category.CategoryDtoRequest;
import ru.practicum.explore_with_me.dto.category.CategoryDtoResponse;
import ru.practicum.explore_with_me.service.category.CategoryService;

import javax.validation.Valid;
import javax.validation.constraints.Positive;

@Slf4j
@Validated
@RequiredArgsConstructor
@RestController
@RequestMapping("/admin/categories")
public class CategoryControllerAdm {
    private final CategoryService categoryService;

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public CategoryDtoResponse addCategory(@Valid @RequestBody CategoryDtoRequest categoryDtoRequest) {
        log.info("POST /admin/categories {}", categoryDtoRequest);
        return categoryService.addCategory(categoryDtoRequest);
    }

    @DeleteMapping("/{catId}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deleteCategory(@Positive @PathVariable Long catId) {
        log.info("DELETE/admin/categories/ catId={}", catId);
        categoryService.deleteCategory(catId);
    }

    @PatchMapping("/{catId}")
    @ResponseStatus(HttpStatus.OK)
    public CategoryDtoResponse updateCategory(@Positive @PathVariable Long catId,
                                              @Valid @RequestBody CategoryDtoRequest categoryDtoRequest) {
        log.info("PATCH /admin/categories/ catId={}, dto={}", catId, categoryDtoRequest);
        return categoryService.updateCategory(catId, categoryDtoRequest);
    }
}
