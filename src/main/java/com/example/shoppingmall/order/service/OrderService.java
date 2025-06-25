package com.example.shoppingmall.order.service;

import com.example.shoppingmall.cart.dao.CartDao;
import com.example.shoppingmall.cart.domain.CartDto;
import com.example.shoppingmall.order.dao.OrderDao;
import com.example.shoppingmall.order.dao.OrderItemDao;
import com.example.shoppingmall.order.domain.OrderDto;
import com.example.shoppingmall.order.domain.OrderItemDto;
import com.example.shoppingmall.order.domain.response.OrderCartItemDto;
import com.example.shoppingmall.order.domain.response.OrderResponseDto;
import com.example.shoppingmall.order.domain.request.OrderItemRequestDto;
import com.example.shoppingmall.order.domain.request.OrderRequestDto;
import com.example.shoppingmall.user.dao.DeliveryAddressDao;
import com.example.shoppingmall.user.domain.DeliveryAddressDto;
import com.example.shoppingmall.user.service.DeliveryAddressService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDateTime;
import java.util.List;

@Service
public class OrderService {
    @Autowired
    private CartDao cartDao;

    @Autowired
    private DeliveryAddressDao deliveryAddressDao;

    @Autowired
    private OrderDao orderDao;

    @Autowired
    private OrderItemDao orderItemDao;

    @Autowired
    private DeliveryAddressService deliveryAddressService;

    public List<OrderCartItemDto> getOrderCartItemsByCartIds(List<Long> cartIds) {
        List<CartDto> cartDtos = cartDao.selectCartsByIds(cartIds);

        return cartDtos.stream()
                .map(cart -> {
                    OrderCartItemDto dto = new OrderCartItemDto();
                    dto.setCartId(cart.getCartId());
                    dto.setItemId(cart.getItemId());
                    dto.setItemName(cart.getItemName());
                    dto.setItemImage(cart.getItemImage());
                    dto.setPrice(cart.getPrice());
                    dto.setQuantity(cart.getQuantity());
                    dto.setColorOptionName(cart.getColorOptionName());
                    dto.setSizeOptionName(cart.getSizeOptionName());
                    return dto;
                })
                .toList();
    }

    @Transactional
    public OrderResponseDto processOrder(Long userId, OrderRequestDto orderRequest) {
        DeliveryAddressDto deliveryAddress;

        if (orderRequest.isUseExistingAddress()) {
            deliveryAddress = deliveryAddressDao.findById(orderRequest.getDeliveryAddressId());
            if (deliveryAddress == null || !deliveryAddress.isActive() || !deliveryAddress.getUserId().equals(userId)) {
                throw new IllegalArgumentException("유효하지 않은 배송지입니다.");
            }
        } else {
            DeliveryAddressDto newAddress = orderRequest.getDeliveryAddress();
            newAddress.setUserId(userId);

            deliveryAddressService.upsertAddress(newAddress);
            deliveryAddress = deliveryAddressDao.findActiveAddressByUserIdAndAddress(newAddress);

            if (deliveryAddress == null) {
                throw new RuntimeException("배송지 등록 실패");
            }
        }

        BigDecimal itemsPrice = orderRequest.getItemsPrice();
        BigDecimal deliveryFee = orderRequest.getDeliveryFee();
        BigDecimal discountAmount = BigDecimal.ZERO;
        BigDecimal totalAmount = itemsPrice.add(deliveryFee).subtract(discountAmount);

        OrderDto order = new OrderDto();
        order.setUserId(userId);
        order.setStatus("PAID");
        order.setItemsPrice(itemsPrice);
        order.setDeliveryFee(deliveryFee);
        order.setCreatedAt(LocalDateTime.now());
        order.setUpdatedAt(LocalDateTime.now());

        orderDao.insertOrder(order);
        Long orderId = order.getOrderId();

        if (orderId == null) {
            throw new RuntimeException("주문 등록 실패");
        }

        int sequence = 1;
        for (OrderItemRequestDto itemReq : orderRequest.getItems()) {
            BigDecimal unitPrice = itemReq.getPrice().divide(BigDecimal.valueOf(itemReq.getQuantity()), 0, RoundingMode.HALF_UP);

            for (int i = 0; i < itemReq.getQuantity(); i++) {
                OrderItemDto itemDto = new OrderItemDto(); // 매 루프마다 새 객체 생성
                itemDto.setOrderId(orderId);
                itemDto.setItemSequence(sequence++); // 시퀀스 증가
                itemDto.setItemId(itemReq.getItemId());
                itemDto.setStatus("PAID");
                itemDto.setDelayReason(null);
                itemDto.setPrice(unitPrice);

                orderItemDao.insertOrderItem(itemDto);
            }
        }

        OrderResponseDto responseDto = new OrderResponseDto();
        responseDto.setOrderNumber("DC" + orderId);
        responseDto.setItemsPrice(itemsPrice);
        responseDto.setDeliveryFee(deliveryFee);
        responseDto.setDiscountAmount(discountAmount);
        responseDto.setTotalAmount(totalAmount);

        responseDto.setRecipientName(deliveryAddress.getRecipientName());
        responseDto.setRecipientPhoneNumber(deliveryAddress.getRecipientPhoneNumber());

        String fullAddress = "[" + deliveryAddress.getPostalCode() + "] " +
                deliveryAddress.getMainAddress() + " " +
                deliveryAddress.getDetailedAddress();

        responseDto.setFullAddress(fullAddress);

        return responseDto;
    }
}
