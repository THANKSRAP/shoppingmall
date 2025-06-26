package com.example.shoppingmall.user.domain.dto;

import lombok.Data;

@Data
public class WishlistDto {
    private Long id;          // 관심목록 ID
    private Long userId;      // 사용자 ID
    private Long itemId;      // 상품 ID
    private String name;   // 상품명
    private String image;  // 상품 이미지
    private String description; // 상품 설명
    private int price;     // 상품 가격
}