package ru.practicum.explore_with_me.service;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.practicum.explore_with_me.category.CategoryDto;
import ru.practicum.explore_with_me.category.NewCategoryDto;
import ru.practicum.explore_with_me.model.Category;
import ru.practicum.explore_with_me.model.exceptions.NotFoundException;
import ru.practicum.explore_with_me.model.mapper.CategoryMapper;
import ru.practicum.explore_with_me.repository.CategoryRepository;
import ru.practicum.explore_with_me.utils.CustomPage;

import java.util.List;
import java.util.stream.Collectors;

@Service
@Transactional
@RequiredArgsConstructor
public class CategoryServiceImpl implements CategoryService {
    private final CategoryRepository categoryRepository;

    @Override
    public CategoryDto create(NewCategoryDto dto) {
        Category category = categoryRepository.save(CategoryMapper.newCategoryToCategory(dto));
        return CategoryMapper.toCategoryDto(category);
    }

    @Override
    public void delete(Long catId) {
        existsById(catId);
        categoryRepository.deleteById(catId);
    }

    @Override
    public CategoryDto update(Long catId, CategoryDto dto) {
        existsById(catId);
        dto.setId(catId);
        Category category = categoryRepository.save(CategoryMapper.categoryDtoToCategory(dto));
        return CategoryMapper.toCategoryDto(category);
    }

    @Override
    @Transactional(readOnly = true)
    public List<CategoryDto> getAll(Integer from, Integer size) {
        CustomPage pageable = new CustomPage(from, size);
        Page<Category> catPage = categoryRepository.findAll(pageable);
        return catPage.stream().map(CategoryMapper::toCategoryDto).collect(Collectors.toList());
    }

    @Override
    @Transactional(readOnly = true)
    public CategoryDto getById(Long catId) {
        return categoryRepository.findById(catId).map(CategoryMapper::toCategoryDto)
                .orElseThrow(() -> new NotFoundException("Category", catId));
    }

    @Override
    @Transactional(readOnly = true)
    public void existsById(Long catId) {
        if (!categoryRepository.existsById(catId)) {
            throw new NotFoundException("Category", catId);
        }
    }
}
