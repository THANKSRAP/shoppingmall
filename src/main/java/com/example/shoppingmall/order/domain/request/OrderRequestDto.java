package com.example.shoppingmall.order.domain.request;

import com.example.shoppingmall.user.domain.DeliveryAddressDto;

import java.math.BigDecimal;
import java.util.List;

public class OrderRequestDto {
    private boolean useExistingAddress;
    private Long deliveryAddressId;
    private DeliveryAddressDto deliveryAddress;
    private List<OrderItemRequestDto> items;
    private BigDecimal itemsPrice;
    private BigDecimal deliveryFee;

    public OrderRequestDto(){}

    public OrderRequestDto(boolean useExistingAddress, Long deliveryAddressId, DeliveryAddressDto deliveryAddress, List<OrderItemRequestDto> items, BigDecimal itemsPrice, BigDecimal deliveryFee) {
        this.useExistingAddress = useExistingAddress;
        this.deliveryAddressId = deliveryAddressId;
        this.deliveryAddress = deliveryAddress;
        this.items = items;
        this.itemsPrice = itemsPrice;
        this.deliveryFee = deliveryFee;
    }

    @Override
    public String toString() {
        return "OrderRequestDto{" +
                "useExistingAddress=" + useExistingAddress +
                ", deliveryAddressId=" + deliveryAddressId +
                ", deliveryAddress=" + deliveryAddress +
                ", items=" + items +
                ", itemsPrice=" + itemsPrice +
                ", deliveryFee=" + deliveryFee +
                '}';
    }

    public boolean isUseExistingAddress() {
        return useExistingAddress;
    }

    public void setUseExistingAddress(boolean useExistingAddress) {
        this.useExistingAddress = useExistingAddress;
    }

    public Long getDeliveryAddressId() {
        return deliveryAddressId;
    }

    public void setDeliveryAddressId(Long deliveryAddressId) {
        this.deliveryAddressId = deliveryAddressId;
    }

    public DeliveryAddressDto getDeliveryAddress() {
        return deliveryAddress;
    }

    public void setDeliveryAddress(DeliveryAddressDto deliveryAddress) {
        this.deliveryAddress = deliveryAddress;
    }

    public List<OrderItemRequestDto> getItems() {
        return items;
    }

    public void setItems(List<OrderItemRequestDto> items) {
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
