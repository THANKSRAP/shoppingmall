package com.example.shoppingmall.item.dao.impl;

import com.example.shoppingmall.item.dao.ItemOptionDao;
import com.example.shoppingmall.item.domain.dto.ItemOptionDto;
import org.mybatis.spring.SqlSessionTemplate;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public class ItemOptionDaoImpl implements ItemOptionDao {

    private final SqlSessionTemplate sqlSession;
    private static final String NAMESPACE = "com.example.shoppingmall.item.dao.ItemOptionDao.";

    public ItemOptionDaoImpl(SqlSessionTemplate sqlSession) {
        this.sqlSession = sqlSession;
    }

    @Override
    public List<ItemOptionDto> findItemOptionsWithInventory(Long itemId) {
        return sqlSession.selectList(NAMESPACE + "findItemOptionsWithInventory", itemId);
    }

    @Override
    public List<ItemOptionDto> findColorOptions(Long itemId) {
        return sqlSession.selectList(NAMESPACE + "findColorOptions", itemId);
    }

    @Override
    public List<ItemOptionDto> findSizeOptions(Long itemId) {
        return sqlSession.selectList(NAMESPACE + "findSizeOptions", itemId);
    }
}