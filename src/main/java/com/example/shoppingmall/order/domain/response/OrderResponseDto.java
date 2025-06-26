package com.example.shoppingmall.order.domain.response;

import java.math.BigDecimal;

public class OrderResponseDto {
    private String orderNumber;
    private BigDecimal itemsPrice;
    private BigDecimal deliveryFee;
    private BigDecimal discountAmount;
    private BigDecimal totalAmount;
    private String recipientName;
    private String recipientPhoneNumber;
    private String fullAddress;

    public OrderResponseDto(){}

    public OrderResponseDto(String orderNumber, BigDecimal itemsPrice, BigDecimal deliveryFee, BigDecimal discountAmount, BigDecimal totalAmount, String recipientName, String recipientPhoneNumber, String fullAddress) {
        this.orderNumber = orderNumber;
        this.itemsPrice = itemsPrice;
        this.deliveryFee = deliveryFee;
        this.discountAmount = discountAmount;
        this.totalAmount = totalAmount;
        this.recipientName = recipientName;
        this.recipientPhoneNumber = recipientPhoneNumber;
        this.fullAddress = fullAddress;
    }

    public String getOrderNumber() {
        return orderNumber;
    }

    public void setOrderNumber(String orderNumber) {
        this.orderNumber = orderNumber;
    }

    public BigDecimal getItemsPrice() {
        return itemsPrice;
    }

    public void setItemsPrice(BigDecimal itemsPrice) {
        this.itemsPrice = itemsPrice;
    }

    public BigDecimal getDeliveryFee() {
        return deliveryFee;
    }

    public void setDeliveryFee(BigDecimal deliveryFee) {
        this.deliveryFee = deliveryFee;
    }

    public BigDecimal getDiscountAmount() {
        return discountAmount;
    }

    public void setDiscountAmount(BigDecimal discountAmount) {
        this.discountAmount = discountAmount;
    }

    public BigDecimal getTotalAmount() {
        return totalAmount;
    }

    public void setTotalAmount(BigDecimal totalAmount) {
        this.totalAmount = totalAmount;
    }

    public String getRecipientName() {
        return recipientName;
    }

    public void setRecipientName(String recipientName) {
        this.recipientName = recipientName;
    }

    public String getRecipientPhoneNumber() {
        return recipientPhoneNumber;
    }

    public void setRecipientPhoneNumber(String recipientPhoneNumber) {
        this.recipientPhoneNumber = recipientPhoneNumber;
    }

    public String getFullAddress() {
        return fullAddress;
    }

    public void setFullAddress(String fullAddress) {
        this.fullAddress = fullAddress;
    }
}
