package com.example.shoppingmall.cart.domain;

import java.math.BigDecimal;
import java.sql.Timestamp;
import java.time.LocalDateTime;

public class CartDto {

    private int cartId;
    private int userId;
    private int itemId;
    private int itemOptionId;
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

    public int getCartId() {
        return cartId;
    }

    public void setCartId(int cartId) {
        this.cartId = cartId;
    }

    public int getUserId() {
        return userId;
    }

    public void setUserId(int userId) {
        this.userId = userId;
    }

    public int getItemId() {
        return itemId;
    }

    public void setItemId(int itemId) {
        this.itemId = itemId;
    }

    public int getItemOptionId() {
        return itemOptionId;
    }

    public void setItemOptionId(int itemOptionId) {
        this.itemOptionId = itemOptionId;
    }

    public int getQuantity() {
        return quantity;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }

    public LocalDateTime getAdded_at() {
        return added_at;
    }

    public void setAdded_at(LocalDateTime added_at) {
        this.added_at = added_at;
    }

    public LocalDateTime getCreated_at() {
        return created_at;
    }

    public void setCreated_at(LocalDateTime created_at) {
        this.created_at = created_at;
    }

    public LocalDateTime getUpdated_at() {
        return updated_at;
    }

    public void setUpdated_at(LocalDateTime updated_at) {
        this.updated_at = updated_at;
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

    public String getItemImage() {
        return itemImage;
    }

    public void setItemImage(String itemImage) {
        this.itemImage = itemImage;
    }

    public String getItemName() {
        return itemName;
    }

    public void setItemName(String itemName) {
        this.itemName = itemName;
    }

    public BigDecimal getPrice() {
        return price;
    }

    public void setPrice(BigDecimal price) {
        this.price = price;
    }
}