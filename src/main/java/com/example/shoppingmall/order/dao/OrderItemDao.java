package com.example.shoppingmall.order.dao;

import com.example.shoppingmall.order.domain.OrderItemDto;

public interface OrderItemDao {
    int insertOrderItem(OrderItemDto orderItemDto);
}
