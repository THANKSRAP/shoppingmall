package com.example.shoppingmall.order.dao;

import com.example.shoppingmall.order.domain.OrderItemDto;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import java.math.BigDecimal;

import static org.junit.Assert.*;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = {"file:src/main/webapp/WEB-INF/spring/root-context.xml"})
public class OrderItemDaoTest {
    @Autowired
    private OrderItemDao orderItemDao;

    private OrderItemDto baseOrderItem;

    @Before
    public void setUp() {
        baseOrderItem = new OrderItemDto();
        baseOrderItem.setItemSequence(5);
        baseOrderItem.setOrderId(1L);
        baseOrderItem.setItemId(100L);
        baseOrderItem.setStatus("PAID");
        baseOrderItem.setDelayReason(null);
        baseOrderItem.setPrice(new BigDecimal("12000.00"));
    }

    @Test
    public void shouldInsertOrderItemSuccessfully() {
        int result = orderItemDao.insertOrderItem(baseOrderItem);
        assertEquals(1, result);
    }

    @Test(expected = Exception.class)
    public void shouldFailToInsertOrderItemWithoutOrderId() {
        OrderItemDto invalidItem = new OrderItemDto();
        invalidItem.setItemSequence(1);
        invalidItem.setItemId(101L);
        invalidItem.setStatus("PAID");
        invalidItem.setPrice(new BigDecimal("9900.00"));

        orderItemDao.insertOrderItem(invalidItem);
    }
}