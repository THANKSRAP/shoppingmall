package com.example.shoppingmall.item.domain.dto;

import lombok.Data;

@Data
public class CategoryDto {
    private Long categoryId; // Auto Increasement
    private Long parentCategoryId;
    private String name;
    private Integer depth;
    // private LocalDateTime createdAt; // 외부에 노출할 필요 없고, 입력으로 받지 않는 값
    // private LocalDateTime updatedAt; // 외부에 노출할 필요 없고, 입력으로 받지 않는 값

    private CategoryDto parent; // Category의 계층구조를 표현하기 위한 필드
}