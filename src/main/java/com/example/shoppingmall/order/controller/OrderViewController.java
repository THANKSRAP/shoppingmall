package com.example.shoppingmall.order.controller;

import java.util.LinkedHashMap;
import java.util.List;

import javax.servlet.http.HttpSession;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;

import com.example.shoppingmall.order.domain.request.OrderPageRequestDto;
import com.example.shoppingmall.order.domain.response.OrderCartItemDto;
import com.example.shoppingmall.order.domain.response.OrderPageResponseDto;
import com.example.shoppingmall.order.service.OrderService;
import com.example.shoppingmall.user.domain.DeliveryAddressDto;
import com.example.shoppingmall.user.service.DeliveryAddressService;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Controller
@RequestMapping("/order")
@RequiredArgsConstructor
@Slf4j
public class OrderViewController {

    private final OrderService orderService;
    private final DeliveryAddressService deliveryAddressService;
    private final ObjectMapper objectMapper = new ObjectMapper();

    /**
     * 주문 페이지 처리 (POST)
     */
    @PostMapping
    public String handleOrder(@Valid @RequestBody OrderPageRequestDto orderRequest,
                              BindingResult bindingResult,
                              HttpSession session) {
        try {
            if (bindingResult.hasErrors()) {
                log.warn("주문 요청 데이터 검증 실패");
                return "redirect:/cart";
            }

            String json = objectMapper.writeValueAsString(orderRequest);
            session.setAttribute("orderData", json);
            log.info("주문 데이터 세션 저장 완료");
            return "redirect:/order";
            
        } catch (JsonProcessingException e) {
            log.error("주문 데이터 JSON 변환 실패", e);
            return "redirect:/cart";
        }
    }

    /**
     * 주문 페이지 (GET)
     */
    @GetMapping
    public String orderPage(HttpSession session, Model model) {
        try {
            Object raw = session.getAttribute("orderData");

            if (raw == null) {
                log.warn("세션에 주문 데이터가 없음");
                return "redirect:/cart";
            }

            OrderPageRequestDto orderData = null;

            if (raw instanceof String) {
                orderData = objectMapper.readValue((String) raw, OrderPageRequestDto.class);
            } else if (raw instanceof LinkedHashMap) {
                orderData = objectMapper.convertValue(raw, OrderPageRequestDto.class);
            }

            if (orderData == null || orderData.getCarts() == null || orderData.getCarts().isEmpty()) {
                log.warn("주문 데이터가 유효하지 않음");
                return "redirect:/cart";
            }

            Long userId = (Long) session.getAttribute("userId");
            if (userId == null) {
                log.warn("로그인하지 않은 사용자가 주문 페이지 접근 시도");
                return "redirect:/loginForm";
            }

            // 배송지 및 장바구니 아이템 조회
            List<DeliveryAddressDto> deliveryAddresses = deliveryAddressService.getActiveAddresses(userId);
            List<Long> cartIds = orderData.getCarts().stream()
                    .map(OrderPageRequestDto.CartItem::getCartId)
                    .toList();
            List<OrderCartItemDto> items = orderService.getOrderCartItemsByCartIds(cartIds);

            OrderPageResponseDto responseDto = new OrderPageResponseDto();
            responseDto.setDeliveryAddresses(deliveryAddresses);
            responseDto.setItems(items);
            responseDto.setItemsPrice(orderData.getItemsPrice());
            responseDto.setDeliveryFee(orderData.getDeliveryFee());

            model.addAttribute("orderPage", responseDto);
            log.info("주문 페이지 로드 완료: userId={}, 상품 수={}", userId, items.size());
            return "order/order";
            
        } catch (Exception e) {
            log.error("주문 페이지 로드 실패", e);
            return "redirect:/cart";
        }
    }
} 