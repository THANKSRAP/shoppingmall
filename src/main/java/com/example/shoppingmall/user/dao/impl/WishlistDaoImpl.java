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
    // ... 나머지 코드는 동일
    private final SqlSession sqlSession;
    private static final String NAMESPACE = "WishlistMapper.";

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

    @Override
    public void deleteWishlistItem(Long id, Long userId) {
        Map<String, Object> params = new HashMap<>();
        params.put("id", id);
        params.put("userId", userId);
        sqlSession.delete(NAMESPACE + "deleteWishlistItem", params);
    }

    @Override
    public void deleteAllWishlist(Long userId) {
        sqlSession.delete(NAMESPACE + "deleteAllWishlist", userId);
    }

    @Override
    public void insertWishlistItem(Long userId, Long itemId) {
        Map<String, Object> params = new HashMap<>();
        params.put("userId", userId);
        params.put("itemId", itemId);
        sqlSession.insert(NAMESPACE + "insertWishlistItem", params);
    }

    @Override
    public boolean existsWishlistItem(Long userId, Long itemId) {
        Map<String, Object> params = new HashMap<>();
        params.put("userId", userId);
        params.put("itemId", itemId);
        Boolean result = sqlSession.selectOne(NAMESPACE + "existsWishlistItem", params);
        return result != null && result;
    }

}