package ru.practicum.explore_with_me.model.mapper;

import lombok.experimental.UtilityClass;
import ru.practicum.explore_with_me.category.CategoryDto;
import ru.practicum.explore_with_me.category.NewCategoryDto;
import ru.practicum.explore_with_me.model.Category;

@UtilityClass
public class CategoryMapper {

    public Category newCategoryToCategory(NewCategoryDto dto) {
        Category category = new Category();
        category.setName(dto.getName());
        return category;
    }

    public Category categoryDtoToCategory(CategoryDto dto) {
        Category category = new Category();
        category.setName(dto.getName());
        category.setId(dto.getId());
        return category;
    }

    public CategoryDto toCategoryDto(Category category) {
        CategoryDto dto = new CategoryDto();
        dto.setId(category.getId());
        dto.setName(category.getName());
        return dto;
    }
}
