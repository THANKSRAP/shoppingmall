package com.example.shoppingmall.order.dao;

import com.example.shoppingmall.order.domain.OrderDto;
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
public class OrderDaoTest {
    @Autowired
    private OrderDao orderDao;

    private OrderDto baseOrder;

    @Before
    public void setUp() {
        baseOrder = new OrderDto();
        baseOrder.setUserId(1L);
        baseOrder.setStatus("PAID");
        baseOrder.setItemsPrice(new BigDecimal("15000.00"));
        baseOrder.setDeliveryFee(new BigDecimal("3000.00"));
    }

    @Test
    public void shouldInsertOrderSuccessfully() {
        int result = orderDao.insertOrder(baseOrder);
        assertEquals(1, result);
    }

    @Test(expected = Exception.class)
    public void shouldFailToInsertOrderWithNullUserId() {
        OrderDto invalidOrder = new OrderDto();
        invalidOrder.setStatus("PAID");
        invalidOrder.setItemsPrice(new BigDecimal("10000.00"));
        invalidOrder.setDeliveryFee(new BigDecimal("2500.00"));

        orderDao.insertOrder(invalidOrder);
    }
}
