package com.example.shoppingmall.order.controller;

import javax.servlet.http.HttpSession;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.shoppingmall.order.domain.request.OrderRequestDto;
import com.example.shoppingmall.order.domain.response.OrderResponseDto;
import com.example.shoppingmall.order.service.OrderService;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@RestController
@RequestMapping("/api/order")
@RequiredArgsConstructor
@Slf4j
public class OrderController {

    private final OrderService orderService;

    /**
     * 주문 제출
     */
    @PostMapping("/submit")
    public ResponseEntity<OrderResponseDto> submitOrder(
            @RequestBody OrderRequestDto orderRequest,
            HttpSession session
    ) {
        try {
            Long userId = (Long) session.getAttribute("userId");
            if (userId == null) {
                log.warn("로그인하지 않은 사용자가 주문 제출 시도");
                return ResponseEntity.badRequest().build();
            }

            OrderResponseDto response = orderService.processOrder(userId, orderRequest);
            log.info("주문 제출 완료: userId={}, 주문번호={}", userId, response.getOrderNumber());
            return ResponseEntity.ok(response);
            
        } catch (Exception e) {
            log.error("주문 제출 실패", e);
            return ResponseEntity.badRequest().build();
        }
    }
}
