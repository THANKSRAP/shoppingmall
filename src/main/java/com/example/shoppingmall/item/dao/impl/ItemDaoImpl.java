package com.example.shoppingmall.item.dao.impl;

import com.example.shoppingmall.item.dao.ItemDao;
import com.example.shoppingmall.item.domain.Item;
import org.apache.ibatis.session.SqlSession;
import org.springframework.stereotype.Repository;

import java.util.List;

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
    public void delete(Long id) {
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


}