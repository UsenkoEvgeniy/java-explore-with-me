package ru.practicum.explore_with_me.model.mapper;

import ru.practicum.explore_with_me.category.CategoryDto;
import ru.practicum.explore_with_me.category.NewCategoryDto;
import ru.practicum.explore_with_me.model.Category;

public class CategoryMapper {
    private CategoryMapper() {
    }

    public static Category newCategoryToCategory(NewCategoryDto dto) {
        Category category = new Category();
        category.setName(dto.getName());
        return category;
    }

    public static Category categoryDtoToCategory(CategoryDto dto) {
        Category category = new Category();
        category.setName(dto.getName());
        category.setId(dto.getId());
        return category;
    }

    public static CategoryDto toCategoryDto(Category category) {
        CategoryDto dto = new CategoryDto();
        dto.setId(category.getId());
        dto.setName(category.getName());
        return dto;
    }
}
