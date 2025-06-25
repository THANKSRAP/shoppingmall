package com.example.shoppingmall.order.domain.request;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;

import java.math.BigDecimal;
import java.util.List;

public class OrderPageRequestDto {

    @NotEmpty(message = "장바구니 목록은 비어 있을 수 없습니다.")
    private List<CartItem> carts;

    @NotNull(message = "상품 가격 합계는 필수입니다.")
    @JsonProperty("items_price")
    private BigDecimal itemsPrice;

    @NotNull(message = "배송비는 필수입니다.")
    @JsonProperty("delivery_fee")
    private BigDecimal deliveryFee;

    public OrderPageRequestDto() {
    }

    public OrderPageRequestDto(List<CartItem> carts, BigDecimal itemsPrice, BigDecimal deliveryFee) {
        this.carts = carts;
        this.itemsPrice = itemsPrice;
        this.deliveryFee = deliveryFee;
    }

    public List<CartItem> getCarts() {
        return carts;
    }

    public void setCarts(List<CartItem> carts) {
        this.carts = carts;
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

    public static class CartItem {

        @NotNull(message = "cartId는 필수입니다.")
        @JsonProperty("cart_id")
        private Long cartId;

        @NotNull(message = "가격은 필수입니다.")
        private BigDecimal price;

        public CartItem() {
        }

        public CartItem(Long cartId, BigDecimal price) {
            this.cartId = cartId;
            this.price = price;
        }

        public Long getCartId() {
            return cartId;
        }

        public void setCartId(Long cartId) {
            this.cartId = cartId;
        }

        public BigDecimal getPrice() {
            return price;
        }

        public void setPrice(BigDecimal price) {
            this.price = price;
        }
    }
}
