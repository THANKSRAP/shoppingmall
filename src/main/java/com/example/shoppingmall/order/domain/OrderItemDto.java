package com.example.shoppingmall.order.domain;

import java.math.BigDecimal;
import java.util.Objects;

public class OrderItemDto {
    private Integer itemSequence;
    private Long orderId;
    private Long itemId;
    private String status;
    private String delayReason;
    private BigDecimal price;

    public OrderItemDto() {}

    public OrderItemDto(Integer itemSequence, Long orderId, Long itemId, String status, String delayReason, BigDecimal price) {
        this.itemSequence = itemSequence;
        this.orderId = orderId;
        this.itemId = itemId;
        this.status = status;
        this.delayReason = delayReason;
        this.price = price;
    }

    public Integer getItemSequence() {
        return itemSequence;
    }

    public void setItemSequence(Integer itemSequence) {
        this.itemSequence = itemSequence;
    }

    public Long getOrderId() {
        return orderId;
    }

    public void setOrderId(Long orderId) {
        this.orderId = orderId;
    }

    public Long getItemId() {
        return itemId;
    }

    public void setItemId(Long itemId) {
        this.itemId = itemId;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getDelayReason() {
        return delayReason;
    }

    public void setDelayReason(String delayReason) {
        this.delayReason = delayReason;
    }

    public BigDecimal getPrice() {
        return price;
    }

    public void setPrice(BigDecimal price) {
        this.price = price;
    }

    @Override
    public String toString() {
        return "OrderItemDto{" +
                "itemSequence=" + itemSequence +
                ", orderId=" + orderId +
                ", itemId=" + itemId +
                ", status='" + status + '\'' +
                ", delayReason='" + delayReason + '\'' +
                ", price=" + price +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (o == null || getClass() != o.getClass()) return false;
        OrderItemDto that = (OrderItemDto) o;
        return Objects.equals(itemSequence, that.itemSequence) && Objects.equals(orderId, that.orderId);
    }

    @Override
    public int hashCode() {
        return Objects.hash(itemSequence, orderId);
    }
}
