package com.example.shoppingmall.item.controller;

import com.example.shoppingmall.item.domain.dto.CategoryDto;
import com.example.shoppingmall.item.service.CategoryService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/category")
public class CategoryController {

    private final CategoryService categoryService;

    public CategoryController(CategoryService categoryService) {
        this.categoryService = categoryService;
    }

    // 전체 카테고리 조회
    @GetMapping("/all")
    public List<CategoryDto> getAllCategories() {
        return categoryService.getAllCategories();
    }

    // 단일 카테고리 조회
    @GetMapping("/{categoryId}")
    public CategoryDto getCategoryById(@PathVariable Long categoryId) {
        return categoryService.getCategoryById(categoryId);
    }

    // depth 기준 조회 (예: 1=대분류, 2=중분류, 3=소분류)
    @GetMapping("/depth/{depth}")
    public List<CategoryDto> getCategoriesByDepth(@PathVariable int depth) {
        return categoryService.getCategoriesByDepth(depth);
    }

    // depth와 parentId 기준 조회
    @GetMapping("/depth/{depth}/parent/{parentId}")
    public List<CategoryDto> getCategoriesByDepthAndParentId(@PathVariable int depth, @PathVariable Long parentId) {
        return categoryService.getCategoriesByDepthAndParentId(depth, parentId);
    }

    // 상위 카테고리 ID로 하위 카테고리 조회
    @GetMapping("/parent/{parentId}/children")
    public List<CategoryDto> getCategoriesByParentCategoryId(@PathVariable Long parentId) {
        return categoryService.getCategoriesByParentCategoryId(parentId);
    }

    // 특정 상품의 카테고리 조회
    @GetMapping("/item/{itemId}")
    public CategoryDto getCategoryByItemId(@PathVariable Long itemId) {
        return categoryService.getCategoryByItemId(itemId);
    }

    // 상위 카테고리 조회 (역참조)
    @GetMapping("/{categoryId}/parent")
    public CategoryDto getParentCategory(@PathVariable Long categoryId) {
        return categoryService.getParentCategoryById(categoryId);
    }
}