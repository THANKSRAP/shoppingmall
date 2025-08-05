package com.example.shoppingmall.itemquestion.dao;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.ibatis.session.SqlSession;
import org.springframework.stereotype.Repository;

import com.example.shoppingmall.common.dto.PageRequest;
import com.example.shoppingmall.itemquestion.domain.ItemQuestion;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Repository
@RequiredArgsConstructor
@Slf4j
public class ItemQuestionDaoImpl implements ItemQuestionDao {
    
    private final SqlSession sqlSession;
    private static final String NAMESPACE = "ItemQuestionMapper.";

    @Override
    public List<ItemQuestion> findAll() {
        return sqlSession.selectList(NAMESPACE + "findAll");
    }

    @Override
    public ItemQuestion findById(Long id) {
        return sqlSession.selectOne(NAMESPACE + "findById", id);
    }

    @Override
    public void insert(ItemQuestion itemQuestion) {
        sqlSession.insert(NAMESPACE + "insert", itemQuestion);
    }

    @Override
    public int update(ItemQuestion itemQuestion) {
        return sqlSession.update(NAMESPACE + "update", itemQuestion);
    }

    @Override
    public void delete(Long id) {
        sqlSession.update(NAMESPACE + "delete", id);
    }

    @Override
    public List<ItemQuestion> findPage(PageRequest pageRequest) {
        Map<String, Object> params = new HashMap<>();
        params.put("offset", pageRequest.getOffset());
        params.put("limit", pageRequest.getLimit());
        return sqlSession.selectList(NAMESPACE + "findPage", params);
    }

    @Override
    public int count() {
        return sqlSession.selectOne(NAMESPACE + "count");
    }

    @Override
    public List<ItemQuestion> findByKeyword(String keyword, int limit, int offset) {
        Map<String, Object> params = new HashMap<>();
        params.put("keyword", keyword);
        params.put("limit", limit);
        params.put("offset", offset);
        return sqlSession.selectList(NAMESPACE + "findByKeyword", params);
    }

    @Override
    public int countByKeyword(String keyword) {
        return sqlSession.selectOne(NAMESPACE + "countByKeyword", keyword);
    }
}
