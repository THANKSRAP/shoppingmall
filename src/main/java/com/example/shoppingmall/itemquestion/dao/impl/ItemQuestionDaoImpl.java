package com.example.shoppingmall.itemquestion.dao.impl;


import com.example.shoppingmall.itemquestion.dao.ItemQuestionDao;
import com.example.shoppingmall.itemquestion.domain.ItemQuestion;
import com.example.shoppingmall.common.dto.PageRequestDto;
import org.apache.ibatis.session.SqlSession;
import org.springframework.stereotype.Repository;

import java.util.HashMap;
import java.util.List;

@Repository
public class ItemQuestionDaoImpl implements ItemQuestionDao {
    private final SqlSession sqlSession;
    private static final String NAMESPACE = "com.example.shoppingmall.itemquestion.dao.ItemQuestionDao.";

    public ItemQuestionDaoImpl(SqlSession sqlSession) {
        this.sqlSession = sqlSession;
    }

    //전체조회
    @Override
    public List<ItemQuestion> findAll() {
        return sqlSession.selectList(NAMESPACE + "findAll");
    }

    //단건조회
    @Override
    public ItemQuestion findById(Long id) {
        return sqlSession.selectOne(NAMESPACE + "findById", id);
    }

    //등록
    @Override
    public void insert(ItemQuestion itemQuestion) {
        sqlSession.insert(NAMESPACE + "insert", itemQuestion);
    }

    //수정
    @Override
    public int update(ItemQuestion itemQuestion) {
        return sqlSession.update(NAMESPACE + "update", itemQuestion);
    }

    //삭제
    @Override
    public void delete(Long id) {
        sqlSession.update(NAMESPACE + "delete", id);
    }

    @Override
    public List<ItemQuestion> findPage(PageRequestDto pageRequestDto) {
        var params = new java.util.HashMap<String, Object>();
        params.put("offset", pageRequestDto.getOffset());
        params.put("limit", pageRequestDto.getLimit());
        return sqlSession.selectList(NAMESPACE + "findPage", params);
    }

    @Override
    public int count() {
        return sqlSession.selectOne(NAMESPACE + "count");
    }

    @Override
    public List<ItemQuestion> findByKeyword(String keyword, int limit, int offset) {
        var params = new HashMap<String, Object>();
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
