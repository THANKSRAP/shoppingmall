package com.example.shoppingmall.item.dao;

import com.example.shoppingmall.item.domain.Category;

import java.util.List;

public interface CategoryDao {
    List<Category> findAll();
    Category findById(Long categoryId);

    List<Category> findByDepth(int depth); // 계층(대, 중, 소)별 카테고리 조회
    List<Category> findByParentCategoryId(Long parentCategoryId); // 상위 카테고리 기준 하위 카테고리 조회

    Category findByItemId(Long itemId);
    Category findParentCategoryById(Long categoryId);

    List<Category> findByDepthAndParentCategoryId(Integer depth, Long parentCategoryId);
}
