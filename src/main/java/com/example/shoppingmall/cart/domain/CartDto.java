package com.example.shoppingmall.cart.domain;


import java.math.BigDecimal;
import java.sql.Timestamp;
import java.time.LocalDateTime;


public class CartDto {


    private Long cartId;
    private Long userId;
    private Long itemId;
    private Long itemOptionId;
    private int quantity;
    private String colorOptionName;
    private String sizeOptionName;
    private BigDecimal additionalPrice;
    private String itemName;
    private String itemImage;
    private BigDecimal price;


    private LocalDateTime added_at;
    private LocalDateTime created_at;
    private LocalDateTime updated_at;

    public CartDto(){};

    public CartDto(Long userId, Long itemId, Long itemOptionId, int quantity, String colorOptionName, String sizeOptionName, BigDecimal additionalPrice, String itemName, String itemImage, BigDecimal price) {
        this.userId = userId;
        this.itemId = itemId;
        this.itemOptionId = itemOptionId;
        this.quantity = quantity;
        this.colorOptionName = colorOptionName;
        this.sizeOptionName = sizeOptionName;
        this.additionalPrice = additionalPrice;
        this.itemName = itemName;
        this.itemImage = itemImage;
        this.price = price;
    }

    public Long getCartId() {
        return cartId;
    }

    public void setCartId(Long cartId) {
        this.cartId = cartId;
    }

    public Long getUserId() {
        return userId;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
    }

    public Long getItemId() {
        return itemId;
    }

    public void setItemId(Long itemId) {
        this.itemId = itemId;
    }

    public Long getItemOptionId() {
        return itemOptionId;
    }

    public void setItemOptionId(Long itemOptionId) {
        this.itemOptionId = itemOptionId;
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

    public BigDecimal getAdditionalPrice() {
        return additionalPrice;
    }

    public void setAdditionalPrice(BigDecimal additionalPrice) {
        this.additionalPrice = additionalPrice;
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

    @Override
    public String toString() {
        return "CartDto{" +
                "cartId=" + cartId +
                ", userId=" + userId +
                ", itemId=" + itemId +
                ", itemOptionId=" + itemOptionId +
                ", quantity=" + quantity +
                ", colorOptionName='" + colorOptionName + '\'' +
                ", sizeOptionName='" + sizeOptionName + '\'' +
                ", additionalPrice=" + additionalPrice +
                ", itemName='" + itemName + '\'' +
                ", itemImage='" + itemImage + '\'' +
                ", price=" + price +
                ", added_at=" + added_at +
                ", created_at=" + created_at +
                ", updated_at=" + updated_at +
                '}';
    }
}