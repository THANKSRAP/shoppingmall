package com.example.shoppingmall.item.service;

import com.example.shoppingmall.item.domain.dto.CategoryDto;
import org.springframework.stereotype.Service;

import java.util.List;

public interface CategoryService {
    List<CategoryDto> getAllCategories();
    CategoryDto getCategoryById(Long categoryId);

    List<CategoryDto> getCategoriesByDepth(int depth);
    List<CategoryDto> getCategoriesByParentCategoryId(Long parentCategoryId);

    CategoryDto getCategoryByItemId(Long itemId);
    CategoryDto getParentCategoryById(Long categoryId);

    List<CategoryDto> getCategoriesByDepthAndParentId(int depth, Long parentId);
}
