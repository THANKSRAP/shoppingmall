package com.example.shoppingmall.cart.service;


import com.example.shoppingmall.cart.dao.CartDao;
import com.example.shoppingmall.cart.domain.CartDto;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;


import java.util.List;


@Service
public class CartServiceImpl implements CartService {
    @Autowired
    private CartDao cartdao;


    @Override
    public List<CartDto> getCartByUserId(int userId) {
        try {
            System.out.println("== Fighting  ==");
            return cartdao.selectCartByUserId(userId);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }


    @Override
    public void updateQuantity(int cartId, int quantity) {
        try {
            CartDto cartDto = new CartDto();
            cartDto.setCartId(cartId);
            cartDto.setQuantity(quantity);
            cartdao.updateItemQuantity(cartDto); // 기존 updateItemQuantity 재활용
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public void deleteByCartId(int cartId) {
        try {
            cartdao.deleteByCartId(cartId);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public void deleteByCartIds(List<Integer> cartItemIds) {
        try {
            cartdao.deleteByCartIds(cartItemIds);  // ✅ 한 번에 삭제
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }


    @Override
    public void deleteAllByUserId(int userId) {
        try {
            cartdao.deleteAllByUserId(userId);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }


    @Override
    public void addToWishlist(int userId, int itemId, int itemOptionId)  {
        try {
            cartdao.addToWishlist(userId, itemId, itemOptionId);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public void insertCart(CartDto cartDto) {
        try {
            CartDto existingCartItem = cartdao.selectExistingCartItem(cartDto);
            if (existingCartItem != null) {
                int newQuantity = existingCartItem.getQuantity() + cartDto.getQuantity();
                existingCartItem.setQuantity(newQuantity);
                cartdao.updateItemQuantity(existingCartItem);
            } else {
                cartdao.insertCart(cartDto);
            }
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}
