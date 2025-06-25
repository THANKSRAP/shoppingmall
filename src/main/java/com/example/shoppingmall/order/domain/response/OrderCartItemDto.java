package com.example.shoppingmall.order.domain.response;

import java.math.BigDecimal;

public class OrderCartItemDto {
    private Long cartId;
    private Long itemId;
    private String itemName;
    private String itemImage;
    private BigDecimal price;
    private int quantity;
    private String colorOptionName;
    private String sizeOptionName;

    public OrderCartItemDto(){}

    public OrderCartItemDto(Long cartId, Long itemId, String itemName, String itemImage, BigDecimal price, int quantity, String colorOptionName, String sizeOptionName) {
        this.cartId = cartId;
        this.itemId = itemId;
        this.itemName = itemName;
        this.itemImage = itemImage;
        this.price = price;
        this.quantity = quantity;
        this.colorOptionName = colorOptionName;
        this.sizeOptionName = sizeOptionName;
    }

    public Long getCartId() {
        return cartId;
    }

    public void setCartId(Long cartId) {
        this.cartId = cartId;
    }

    public Long getItemId() {
        return itemId;
    }

    public void setItemId(Long itemId) {
        this.itemId = itemId;
    }

    public String getItemName() {
        return itemName;
    }

    public void setItemName(String itemName) {
        this.itemName = itemName;
    }

    public String getItemImage() {
        return itemImage;
    }

    public void setItemImage(String itemImage) {
        this.itemImage = itemImage;
    }

    public BigDecimal getPrice() {
        return price;
    }

    public void setPrice(BigDecimal price) {
        this.price = price;
    }

    public int getQuantity() {
        return quantity;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }

    public String getColorOptionName() {
        return colorOptionName;
    }

    public void setColorOptionName(String colorOptionName) {
        this.colorOptionName = colorOptionName;
    }

    public String getSizeOptionName() {
        return sizeOptionName;
    }

    public void setSizeOptionName(String sizeOptionName) {
        this.sizeOptionName = sizeOptionName;
    }
}
