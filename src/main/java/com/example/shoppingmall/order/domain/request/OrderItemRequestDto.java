package com.example.shoppingmall.order.domain.request;

import java.math.BigDecimal;

public class OrderItemRequestDto {
    private Long itemId;
    private int quantity;
    private BigDecimal price;

    public OrderItemRequestDto(){}

    public OrderItemRequestDto(Long itemId, int quantity, BigDecimal price) {
        this.itemId = itemId;
        this.quantity = quantity;
        this.price = price;
    }

    public Long getItemId() {
        return itemId;
    }

    public void setItemId(Long itemId) {
        this.itemId = itemId;
    }

    public int getQuantity() {
        return quantity;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }

    public BigDecimal getPrice() {
        return price;
    }

    public void setPrice(BigDecimal price) {
        this.price = price;
    }
}
