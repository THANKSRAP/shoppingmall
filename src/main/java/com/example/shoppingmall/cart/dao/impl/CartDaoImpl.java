package com.example.shoppingmall.cart.dao.impl;

import com.example.shoppingmall.cart.dao.CartDao;
import com.example.shoppingmall.cart.domain.CartDto;
import org.apache.ibatis.session.SqlSession;
import org.springframework.stereotype.Repository;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Repository
public class CartDaoImpl implements CartDao {

    private final SqlSession sqlSession;

    private static final String NAMESPACE = "com.example.shoppingmall.cart.dao.CartDao.";

    public CartDaoImpl(SqlSession sqlSession) {
        this.sqlSession = sqlSession;
    }

    @Override
    public List<CartDto> selectCartByUserId(Long userId) {
        return sqlSession.selectList(NAMESPACE + "selectCartByUserId", userId);
    }

    @Override
    public int insert(CartDto cart) {
        return sqlSession.insert(NAMESPACE + "insert", cart);
    }

    @Override
    public int updateItemQuantity(CartDto cart) {
        return sqlSession.update(NAMESPACE + "updateItemQuantity", cart);
    }

    @Override
    public int deleteByCartId(Long cartId) {
        return sqlSession.delete(NAMESPACE + "deleteByCartId", cartId);
    }

    @Override
    public void deleteByCartIds(List<Long> cartIds) {
        // XML의 <foreach collection="list">와 매핑됨
        sqlSession.delete(NAMESPACE + "deleteByCartIds", cartIds);
    }

    @Override
    public int deleteAllByUserId(Long userId) {
        return sqlSession.delete(NAMESPACE + "deleteAllByUserId", userId);
    }

    @Override
    public int countByUserId(Long userId) {
        return sqlSession.selectOne(NAMESPACE + "countByUserId", userId);
    }

    @Override
    public void updateQuantity(Long cartId, int quantity) {
        // 파라미터가 2개 이상이므로 Map으로 전달
        Map<String, Object> params = new HashMap<>();
        params.put("cartId", cartId);
        params.put("quantity", quantity);
        sqlSession.update(NAMESPACE + "updateQuantity", params);
    }

    @Override
    public void addToWishlist(Long userId, Long itemId) {
        // XML의 parameterType="map"에 맞춰 Map으로 전달
        Map<String, Object> params = new HashMap<>();
        params.put("userId", userId);
        params.put("itemId", itemId);
        sqlSession.insert(NAMESPACE + "addToWishlist", params);
    }

    @Override
    public void insertCart(CartDto cartDto) {
        sqlSession.insert(NAMESPACE + "insertCart", cartDto);
    }

    @Override
    public CartDto selectExistingCartItem(CartDto cartDto) {
        return sqlSession.selectOne(NAMESPACE + "selectExistingCartItem", cartDto);
    }

    @Override
    public List<CartDto> selectCartsByIds(List<Long> cartIds) {
        // XML의 <foreach collection="list">와 매핑됨
        return sqlSession.selectList(NAMESPACE + "selectCartsByIds", cartIds);
    }
}