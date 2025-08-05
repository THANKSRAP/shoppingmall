package com.example.shoppingmall.cart.controller;

import java.util.Map;

import javax.servlet.http.HttpSession;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.shoppingmall.cart.domain.CartDto;
import com.example.shoppingmall.cart.service.CartService;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@RestController
@RequestMapping("/api/cart")
@RequiredArgsConstructor
@Slf4j
public class CartController {

    private final CartService cartService;

    /**
     * 장바구니에 상품 추가
     */
    @PostMapping("/add")
    public ResponseEntity<String> addToCart(@RequestBody Map<String, Object> data, HttpSession session) {
        try {
            Long userId = (Long) session.getAttribute("userId");
            if (userId == null) {
                log.warn("로그인하지 않은 사용자가 장바구니 추가 시도");
                return ResponseEntity.badRequest().body("로그인이 필요합니다.");
            }

            Long itemId = Long.valueOf(data.get("itemId").toString());
            Integer quantity = Integer.valueOf(data.get("quantity").toString());

            CartDto cartDto = new CartDto();
            cartDto.setUserId(userId);
            cartDto.setItemId(itemId);
            cartDto.setQuantity(quantity);
            
            cartService.insertCart(cartDto);
            log.info("장바구니 추가 완료: 사용자={}, 상품={}, 수량={}", userId, itemId, quantity);
            return ResponseEntity.ok("장바구니에 추가되었습니다.");
            
        } catch (Exception e) {
            log.error("장바구니 추가 실패", e);
            return ResponseEntity.badRequest().body("장바구니 추가에 실패했습니다.");
        }
    }

    /**
     * 장바구니 상품 수량 변경
     */
    @PatchMapping("/{cartId}")
    public ResponseEntity<String> updateCartItem(@PathVariable Long cartId,
                                               @RequestBody Map<String, Object> data,
                                               HttpSession session) {
        try {
            Long userId = (Long) session.getAttribute("userId");
            if (userId == null) {
                log.warn("로그인하지 않은 사용자가 장바구니 수량 변경 시도");
                return ResponseEntity.badRequest().body("로그인이 필요합니다.");
            }

            Integer quantity = Integer.valueOf(data.get("quantity").toString());
            cartService.updateQuantity(cartId, quantity);
            log.info("장바구니 수량 변경: 장바구니ID={}, 수량={}", cartId, quantity);
            return ResponseEntity.ok("수량이 변경되었습니다.");
            
        } catch (Exception e) {
            log.error("장바구니 수량 변경 실패: 장바구니ID={}", cartId, e);
            return ResponseEntity.badRequest().body("수량 변경에 실패했습니다.");
        }
    }

    /**
     * 장바구니에서 상품 삭제
     */
    @DeleteMapping("/{cartId}")
    public ResponseEntity<String> removeFromCart(@PathVariable Long cartId, HttpSession session) {
        try {
            Long userId = (Long) session.getAttribute("userId");
            if (userId == null) {
                log.warn("로그인하지 않은 사용자가 장바구니 삭제 시도");
                return ResponseEntity.badRequest().body("로그인이 필요합니다.");
            }

            cartService.deleteByCartId(cartId);
            log.info("장바구니 상품 삭제: 장바구니ID={}", cartId);
            return ResponseEntity.ok("상품이 삭제되었습니다.");
            
        } catch (Exception e) {
            log.error("장바구니 상품 삭제 실패: 장바구니ID={}", cartId, e);
            return ResponseEntity.badRequest().body("상품 삭제에 실패했습니다.");
        }
    }
}
