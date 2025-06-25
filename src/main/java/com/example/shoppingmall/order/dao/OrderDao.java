package com.example.shoppingmall.order.dao;

import com.example.shoppingmall.order.domain.OrderDto;

public interface OrderDao {
    int insertOrder(OrderDto orderDto);
}
