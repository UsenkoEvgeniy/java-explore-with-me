package ru.practicum.explore_with_me.service;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.practicum.explore_with_me.category.CategoryDto;
import ru.practicum.explore_with_me.category.NewCategoryDto;
import ru.practicum.explore_with_me.model.Category;
import ru.practicum.explore_with_me.model.exceptions.NotFoundException;
import ru.practicum.explore_with_me.model.mapper.CategoryMapper;
import ru.practicum.explore_with_me.repository.CategoryRepository;

import java.util.List;
import java.util.stream.Collectors;

@Service
@Transactional
@RequiredArgsConstructor
public class CategoryService {
    private final CategoryRepository categoryRepository;

    public CategoryDto create(NewCategoryDto dto) {
        Category category = categoryRepository.save(CategoryMapper.newCategoryToCategory(dto));
        return CategoryMapper.toCategoryDto(category);
    }

    public void delete(Long catId) {
        existsById(catId);
        categoryRepository.deleteById(catId);
    }

    public CategoryDto update(CategoryDto dto) {
        existsById(dto.getId());
        Category category = categoryRepository.save(CategoryMapper.categoryDtoToCategory(dto));
        return CategoryMapper.toCategoryDto(category);
    }

    @Transactional(readOnly = true)
    public List<CategoryDto> getAll(Integer from, Integer size) {
        Page<Category> catPage = categoryRepository.findAll(PageRequest.of(from / size, size));
        return catPage.stream().map(CategoryMapper::toCategoryDto).collect(Collectors.toList());
    }

    @Transactional(readOnly = true)
    public CategoryDto getById(Long catId) {
        return categoryRepository.findById(catId).map(CategoryMapper::toCategoryDto)
                .orElseThrow(() -> new NotFoundException("Category with id=" + catId + " was not found"));
    }

    @Transactional(readOnly = true)
    public void existsById(Long catId) {
        if (!categoryRepository.existsById(catId)) {
            throw new NotFoundException("Category with id=" + catId + " was not found");
        }
    }
}
