package com.example.shoppingmall.cart.service;


import com.example.shoppingmall.cart.domain.CartDto;
import java.util.List;


public interface CartService {
    List<CartDto> getCartByUserId(int userId);

    void updateQuantity(int cartId, int quantity);
    void deleteByCartId(int cartId);

    void deleteByCartIds(List<Integer> cartItemIds);

    void deleteAllByUserId(int userId);

    void addToWishlist(int userId, int itemId, int itemOptionId);
    void insertCart(CartDto cartDto);
    }