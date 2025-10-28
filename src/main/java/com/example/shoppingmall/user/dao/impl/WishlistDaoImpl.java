package com.example.shoppingmall.user.dao.impl;

import com.example.shoppingmall.user.dao.WishlistDao;
import com.example.shoppingmall.user.domain.dto.WishlistDto;
import com.example.shoppingmall.user.domain.dto.WishlistSearchDto;
import org.apache.ibatis.session.SqlSession;
import org.springframework.stereotype.Repository;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Repository
public class WishlistDaoImpl implements WishlistDao {
    private final SqlSession sqlSession;
    private static final String NAMESPACE = "com.example.shoppingmall.user.dao.WishlistDao.";

    public WishlistDaoImpl(SqlSession sqlSession) {
        this.sqlSession = sqlSession;
    }

    @Override
    public List<WishlistDto> selectWishlist(WishlistSearchDto searchDto) {
        return sqlSession.selectList(NAMESPACE + "selectWishlist", searchDto);
    }

    @Override
    public int countWishlist(Long userId) {
        return sqlSession.selectOne(NAMESPACE + "countWishlist", userId);
    }

    // 특정 상품+옵션 조합으로 삭제
    @Override
    public void deleteWishlistByItemAndOption(Long userId, Long itemId, Long itemOptionId) {
        Map<String, Object> params = new HashMap<>();
        params.put("userId", userId);
        params.put("itemId", itemId);
        params.put("itemOptionId", itemOptionId);
        sqlSession.delete(NAMESPACE + "deleteWishlistByItemAndOption", params);
    }

    @Override
    public void deleteAllWishlist(Long userId) {
        sqlSession.delete(NAMESPACE + "deleteAllWishlist", userId);
    }

    @Override
    public void insertWishlistItem(Long userId, Long itemId, Long itemOptionId) {
        Map<String, Object> params = new HashMap<>();
        params.put("userId", userId);
        params.put("itemId", itemId);
        params.put("itemOptionId", itemOptionId);
        sqlSession.insert(NAMESPACE + "insertWishlistItem", params);
    }

    @Override
    public boolean existsWishlistItem(Long userId, Long itemId, Long itemOptionId) {
        Map<String, Object> params = new HashMap<>();
        params.put("userId", userId);
        params.put("itemId", itemId);
        params.put("itemOptionId", itemOptionId);
        Boolean result = sqlSession.selectOne(NAMESPACE + "existsWishlistItem", params);
        return result != null && result;
    }
}