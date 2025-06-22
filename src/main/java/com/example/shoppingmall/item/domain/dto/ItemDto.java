package com.example.shoppingmall.item.domain.dto;

import com.example.shoppingmall.item.domain.Item;
import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Data
public class ItemDto {
    private Long itemId; // Auto Incresement 되는 값(상품에 대한 조회를 할 때 id 값 사용)
    private Long categoryId;
    private String name;
    private String image;
    private String description;
    private BigDecimal price;
    private String status;
    private String grade;
    private String manufactureCountry;
    private Boolean isBestSeller;
    // private LocalDateTime createdAt; // 외부에 노출할 필요 없고, 입력으로 받지 않는 값
    // private LocalDateTime updatedAt; // 외부에 노출할 필요 없고, 입력으로 받지 않는 값
}
