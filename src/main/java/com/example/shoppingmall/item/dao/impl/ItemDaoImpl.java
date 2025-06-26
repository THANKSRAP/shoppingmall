package com.example.shoppingmall.item.dao.impl;

import com.example.shoppingmall.item.dao.ItemDao;
import com.example.shoppingmall.item.domain.Item;
import com.example.shoppingmall.item.domain.dto.ItemDto;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.session.SqlSession;
import org.springframework.stereotype.Repository;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Repository
public class ItemDaoImpl implements ItemDao {

    private final SqlSession sqlSession;
    private static final String NAMESPACE = "ItemMapper.";

    public ItemDaoImpl(SqlSession sqlSession) {
        this.sqlSession = sqlSession;
    }

    @Override
    public List<Item> findAll() {
        return sqlSession.selectList(NAMESPACE + "findAll");
    }

    @Override
    public Item findById(Long id) {
        return sqlSession.selectOne(NAMESPACE + "findById", id);
    }

    @Override
    public void insert(Item item) {
        sqlSession.insert(NAMESPACE + "insert", item);
    }

    @Override
    public void update(Item item) {
        sqlSession.update(NAMESPACE + "update", item);
    }

    @Override
    public void deleteById(Long id) {
        sqlSession.delete(NAMESPACE + "delete", id);
    }

    @Override
    public List<Item> findBestSellers() {
        return sqlSession.selectList(NAMESPACE + "findBestSellers");
    }

    @Override
    public List<Item> findNewItems() {
        return sqlSession.selectList(NAMESPACE + "findNewItems");
    }

    @Override
    public List<Item> searchItemsByName(String name) {
        return sqlSession.selectList(NAMESPACE + "searchItemsByName", name);
    }

    @Override
    public List<Item> findItemsByCategory(Long majorId, Long middleId, Long minorId) {
        Map<String, Object> params = new HashMap<>();
        params.put("majorId", majorId);
        params.put("middleId", middleId);
        params.put("minorId", minorId);
        return sqlSession.selectList(NAMESPACE + "findItemsByCategory", params);
    }

    @Override
    public ItemDto selectItemWithReviewSummary(Long itemId) {
        return sqlSession.selectOne(NAMESPACE + "selectItemWithReviewSummary", itemId);
    }
}