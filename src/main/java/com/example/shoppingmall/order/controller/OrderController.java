package com.example.shoppingmall.order.controller;

import com.example.shoppingmall.order.domain.response.OrderCartItemDto;
import com.example.shoppingmall.order.domain.response.OrderPageResponseDto;
import com.example.shoppingmall.order.domain.response.OrderResponseDto;
import com.example.shoppingmall.order.domain.request.OrderPageRequestDto;
import com.example.shoppingmall.order.domain.request.OrderRequestDto;
import com.example.shoppingmall.order.service.OrderService;
import com.example.shoppingmall.user.domain.DeliveryAddressDto;
import com.example.shoppingmall.user.service.DeliveryAddressService;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.servlet.http.HttpSession;
import java.util.LinkedHashMap;
import java.util.List;

@Controller
@RequestMapping("/order")
public class OrderController {

    @Autowired
    private OrderService orderService;

    @Autowired
    private DeliveryAddressService deliveryAddressService;

    private final ObjectMapper objectMapper = new ObjectMapper();

    @PostMapping
    public String handleOrder(@Valid @RequestBody OrderPageRequestDto orderRequest,
                              BindingResult bindingResult,
                              HttpSession session) throws JsonProcessingException {
        if (bindingResult.hasErrors()) {
            return "redirect:/cart";
        }

        String json = objectMapper.writeValueAsString(orderRequest);
        session.setAttribute("orderData", json);
        return "redirect:/order";
    }

    @GetMapping
    public String orderPage(HttpSession session, Model model) throws Exception {
        Object raw = session.getAttribute("orderData");

        if (raw == null) {
            return "redirect:/cart";
        }

        OrderPageRequestDto orderData = null;

        if (raw instanceof String) {
            orderData = objectMapper.readValue((String) raw, OrderPageRequestDto.class);
        } else if (raw instanceof LinkedHashMap) {
            orderData = objectMapper.convertValue(raw, OrderPageRequestDto.class);
        }

        if (orderData == null || orderData.getCarts() == null || orderData.getCarts().isEmpty()) {
            return "redirect:/cart";
        }

        // TODO: 로그인 사용자로 교체 예정
        Long userId = 1L;

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
        return "order/order";
    }

    @PostMapping("/submit")
    public ResponseEntity<OrderResponseDto> submitOrder(
            @RequestBody OrderRequestDto orderRequest,
            HttpSession session
    ) {
        // TODO: 로그인 사용자로 교체 예정
        Long userId = 1L;

        OrderResponseDto response = orderService.processOrder(userId, orderRequest);

        return ResponseEntity.ok(response);
    }
}
