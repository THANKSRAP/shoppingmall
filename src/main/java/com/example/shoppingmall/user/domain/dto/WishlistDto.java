package com.example.shoppingmall.user.domain.dto;

import lombok.Data;

@Data
public class WishlistDto {
    private Long id;          // 관심목록 ID
    private Long userId;      // 사용자 ID
    private Long productId;   // 상품 ID
    private String productName;   // 상품명
    private String productImage;  // 상품 이미지
    private String productDescription; // 상품 설명
    private int productPrice;     // 상품 가격
}