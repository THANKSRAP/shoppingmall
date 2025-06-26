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
    public List<CartDto> getCartByUserId(Long userId) {
        try {
            return cartdao.selectCartByUserId(userId);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }


    @Override
    public void updateQuantity(Long cartId, int quantity) {
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
    public void deleteByCartId(Long cartId) {
        try {
            cartdao.deleteByCartId(cartId);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public void deleteByCartIds(List<Long> cartItemIds) {
        try {
            cartdao.deleteByCartIds(cartItemIds);  // ✅ 한 번에 삭제
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }


    @Override
    public void deleteAllByUserId(Long userId) {
        try {
            cartdao.deleteAllByUserId(userId);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }


    @Override
    public void addToWishlist(Long userId, Long itemId)  {
        try {
            cartdao.addToWishlist(userId, itemId);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public Long insertCart(CartDto cartDto) {
        try {
            CartDto existingCartItem = cartdao.selectExistingCartItem(cartDto);
            if (existingCartItem != null) {
                int newQuantity = existingCartItem.getQuantity() + cartDto.getQuantity();
                existingCartItem.setQuantity(newQuantity);
                cartdao.updateItemQuantity(existingCartItem);
                return existingCartItem.getCartId();
            } else {
                cartdao.insertCart(cartDto);
                return cartDto.getCartId();
            }
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}
