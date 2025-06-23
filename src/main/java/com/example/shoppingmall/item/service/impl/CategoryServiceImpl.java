package com.example.shoppingmall.item.service.impl;

import com.example.shoppingmall.item.dao.CategoryDao;
import com.example.shoppingmall.item.domain.Category;
import com.example.shoppingmall.item.domain.dto.CategoryDto;
import com.example.shoppingmall.item.service.CategoryService;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class CategoryServiceImpl implements CategoryService {

    private final CategoryDao categoryDao;

    public CategoryServiceImpl(CategoryDao categoryDao) {
        this.categoryDao = categoryDao;
    }

    private CategoryDto toDto(Category category) {
        if (category == null) return null;

        CategoryDto dto = new CategoryDto();
        dto.setCategoryId(category.getCategoryId());
        dto.setParentCategoryId(category.getParentCategoryId());
        dto.setName(category.getName());
        dto.setDepth(category.getDepth());

        return dto;
    }

    private Category toEntity(CategoryDto categoryDto) {
        if (categoryDto == null) return null;

        Category category = new Category();
        category.setCategoryId(categoryDto.getCategoryId());
        category.setParentCategoryId(categoryDto.getParentCategoryId());
        category.setName(categoryDto.getName());
        category.setDepth(categoryDto.getDepth());

        return category;
    }

    private CategoryDto buildCategoryHierarchy(Category category) {
        if (category == null) return null;

        CategoryDto categoryDto = toDto(category);
        if (category.getParentCategoryId() != null) {
            Category parent = categoryDao.findById(category.getParentCategoryId());
            categoryDto.setParent(buildCategoryHierarchy(parent));
        }

        return categoryDto;
    }

    @Override
    public List<CategoryDto> getAllCategories() {
        return categoryDao.findAll().stream().map(this::toDto).collect(Collectors.toList());
    }

    @Override
    public CategoryDto getCategoryById(Long categoryId) {
        Category category = categoryDao.findById(categoryId);
        return toDto(category);
    }

    @Override
    public List<CategoryDto> getCategoriesByDepth(int depth) {
        return categoryDao.findByDepth(depth).stream()
                .map(this::toDto)
                .collect(Collectors.toList());
    }

    @Override
    public List<CategoryDto> getCategoriesByParentCategoryId(Long parentCategoryId) {
        return categoryDao.findByParentCategoryId(parentCategoryId).stream()
                .map(this::toDto)
                .collect(Collectors.toList());
    }

    @Override
    public CategoryDto getCategoryByItemId(Long itemId) {
        Category category = categoryDao.findByItemId(itemId);

        return buildCategoryHierarchy(category);
    }

    @Override
    public CategoryDto getParentCategoryById(Long categoryId) {
        Category category = categoryDao.findParentCategoryById(categoryId);

        return toDto(category);
    }

    @Override
    public List<CategoryDto> getCategoriesByDepthAndParentId(int depth, Long parentId) {
        return categoryDao.findByDepthAndParentCategoryId(depth, parentId).stream()
                .map(this::toDto)
                .collect(Collectors.toList());
    }
}
