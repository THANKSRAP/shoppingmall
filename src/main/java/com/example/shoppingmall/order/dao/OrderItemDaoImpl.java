package com.example.shoppingmall.order.dao;

import com.example.shoppingmall.order.domain.OrderItemDto;
import org.apache.ibatis.session.SqlSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

@Repository
public class OrderItemDaoImpl implements OrderItemDao {

    @Autowired
    private SqlSession sqlSession;
    private static final String NAMESPACE = "OrderItemMapper.";

    @Override
    public int insertOrderItem(OrderItemDto orderItemDto) {
        return sqlSession.insert(NAMESPACE + "insertOrderItem", orderItemDto);
    }
}
