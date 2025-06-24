package com.example.shoppingmall.item.domain.dto;

import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Data
public class ItemOptionDto {
    private Long itemOptionId;
    private Long itemId;
    private Long colorOptionId; // FK to option (color)
    private Long sizeOptionId;  // FK to option (size)
    private BigDecimal additionalPrice;

    // private LocalDateTime createdAt; // 외부에 노출할 필요 없고, 입력으로 받지 않는 값
    // private LocalDateTime updatedAt; // 외부에 노출할 필요 없고, 입력으로 받지 않는 값

    private String colorName;
    private String sizeName;
    private Integer quantity;
}

