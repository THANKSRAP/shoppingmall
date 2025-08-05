package com.example.shoppingmall.cart.controller;

import java.util.List;

import javax.servlet.http.HttpSession;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import com.example.shoppingmall.cart.domain.CartDto;
import com.example.shoppingmall.cart.service.CartService;
import com.example.shoppingmall.item.domain.dto.ItemDto;
import com.example.shoppingmall.item.service.ItemService;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Controller
@RequestMapping("/cart")
@RequiredArgsConstructor
@Slf4j
public class CartViewController {

    private final CartService cartService;
    private final ItemService itemService;

    /**
     * 장바구니 페이지
     */
    @GetMapping
    public String cart(HttpSession session, Model model) {
        try {
            Long userId = (Long) session.getAttribute("userId");
            if (userId == null) {
                log.warn("로그인하지 않은 사용자가 장바구니 접근 시도");
                return "redirect:/loginForm";
            }

            List<CartDto> cartItems = cartService.getCartByUserId(userId);
            model.addAttribute("cartItems", cartItems);
            log.info("장바구니 페이지 접근: userId={}, 상품 수={}", userId, cartItems.size());
            return "cart/cart";
            
        } catch (Exception e) {
            log.error("장바구니 페이지 로드 실패", e);
            model.addAttribute("error", "장바구니를 불러오는 중 오류가 발생했습니다.");
            return "redirect:/";
        }
    }

    /**
     * 주문 페이지 (장바구니에서 체크아웃)
     */
    @GetMapping("/checkout")
    public String checkout(HttpSession session, Model model) {
        try {
            Long userId = (Long) session.getAttribute("userId");
            if (userId == null) {
                log.warn("로그인하지 않은 사용자가 주문 페이지 접근 시도");
                return "redirect:/loginForm";
            }

            List<CartDto> cartItems = cartService.getCartByUserId(userId);
            if (cartItems.isEmpty()) {
                log.warn("빈 장바구니로 주문 페이지 접근 시도: userId={}", userId);
                return "redirect:/cart";
            }

            // 상품 정보 조회
            for (CartDto cartItem : cartItems) {
                ItemDto item = itemService.getItemById(cartItem.getItemId());
                cartItem.setItemName(item.getName());
                cartItem.setPrice(item.getPrice());
            }

            model.addAttribute("cartItems", cartItems);
            log.info("주문 페이지 접근: userId={}, 상품 수={}", userId, cartItems.size());
            return "order/order";
            
        } catch (Exception e) {
            log.error("주문 페이지 로드 실패", e);
            model.addAttribute("error", "주문 페이지를 불러오는 중 오류가 발생했습니다.");
            return "redirect:/cart";
        }
    }
} 