package com.example.shoppingmall.item.domain;

import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Data
public class ItemOption {
    private Long itemOptionId;
    private Long itemId;
    private Long colorOptionId; // FK to option (color)
    private Long sizeOptionId;  // FK to option (size)
    private BigDecimal additionalPrice;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
}
