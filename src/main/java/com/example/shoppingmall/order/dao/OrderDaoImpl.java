package com.example.shoppingmall.order.dao;

import com.example.shoppingmall.order.domain.OrderDto;
import org.apache.ibatis.session.SqlSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

@Repository
public class OrderDaoImpl implements OrderDao {

    @Autowired
    private SqlSession sqlSession;
    private static final String NAMESPACE = "OrderMapper.";

    @Override
    public int insertOrder(OrderDto orderDto) {
        return sqlSession.insert(NAMESPACE + "insertOrder", orderDto);
    }
}
