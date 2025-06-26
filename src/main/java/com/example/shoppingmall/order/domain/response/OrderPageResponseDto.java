package com.example.shoppingmall.order.domain.response;

import com.example.shoppingmall.user.domain.DeliveryAddressDto;

import java.math.BigDecimal;
import java.util.List;

public class OrderPageResponseDto {
    private List<DeliveryAddressDto> deliveryAddresses;
    private List<OrderCartItemDto> items;
    private BigDecimal itemsPrice;
    private BigDecimal deliveryFee;

    public OrderPageResponseDto() {}

    public OrderPageResponseDto(List<DeliveryAddressDto> deliveryAddresses, List<OrderCartItemDto> items, BigDecimal itemsPrice, BigDecimal deliveryFee) {
        this.deliveryAddresses = deliveryAddresses;
        this.items = items;
        this.itemsPrice = itemsPrice;
        this.deliveryFee = deliveryFee;
    }

    public List<DeliveryAddressDto> getDeliveryAddresses() {
        return deliveryAddresses;
    }

    public void setDeliveryAddresses(List<DeliveryAddressDto> deliveryAddresses) {
        this.deliveryAddresses = deliveryAddresses;
    }

    public List<OrderCartItemDto> getItems() {
        return items;
    }

    public void setItems(List<OrderCartItemDto> items) {
        this.items = items;
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
}
