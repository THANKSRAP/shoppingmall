package com.example.shoppingmall.item.domain;

import lombok.Data;

import java.time.LocalDateTime;

@Data
public class Category {
    private Long categoryId;
    private Long parentCategoryId;
    private String name;
    private Integer depth; // 1(대분류), 2(중분류), 3(소분류)
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
}