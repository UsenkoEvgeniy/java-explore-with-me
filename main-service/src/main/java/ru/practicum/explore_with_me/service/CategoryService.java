package ru.practicum.explore_with_me.service;

import ru.practicum.explore_with_me.category.CategoryDto;
import ru.practicum.explore_with_me.category.NewCategoryDto;

import java.util.List;

public interface CategoryService {
    CategoryDto create(NewCategoryDto dto);

    void delete(Long catId);

    CategoryDto update(CategoryDto dto);

    List<CategoryDto> getAll(Integer from, Integer size);

    CategoryDto getById(Long catId);

    void existsById(Long catId);
}
