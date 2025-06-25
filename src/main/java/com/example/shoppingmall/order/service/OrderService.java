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
}
