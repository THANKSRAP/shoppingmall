package com.example.shoppingmall.cart.service;


import com.example.shoppingmall.cart.domain.CartDto;
import java.util.List;


public interface CartService {
    List<CartDto> getCartByUserId(Long userId);

    void updateQuantity(Long cartId, int quantity);
    void deleteByCartId(Long cartId);

    void deleteByCartIds(List<Long> cartItemIds);

    void deleteAllByUserId(Long userId);

    void addToWishlist(Long userId, Long itemId, Long itemOptionId);
    void insertCart(CartDto cartDto);
}