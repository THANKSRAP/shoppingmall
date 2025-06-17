package com.example.shoppingmall.item.domain;

import lombok.Data;
import java.math.BigDecimal;
import java.time.LocalDateTime;

@Data
public class Item {
    private Long itemId;
    private Long categoryId;
    private String name;
    private String image;
    private String description;
    private BigDecimal price;
    private String status; // enum: SALE, OUT_OF_STOCK, ...
    private String grade;  // enum: A, B, C
    private String manufactureCountry;
    private Boolean isBestSeller;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
}
