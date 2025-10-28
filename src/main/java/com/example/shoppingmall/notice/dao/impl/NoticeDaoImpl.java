package com.example.shoppingmall.notice.dao.impl;

import com.example.shoppingmall.notice.dao.NoticeDao;
import com.example.shoppingmall.notice.domain.Notice;
import com.example.shoppingmall.common.dto.PageRequestDto;
import org.apache.ibatis.session.SqlSession;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public class NoticeDaoImpl implements NoticeDao {
    private final SqlSession sqlSession;

    private static final String NAMESPACE = "com.example.shoppingmall.notice.dao.NoticeDao.";

    public NoticeDaoImpl(SqlSession sqlSession) {
        this.sqlSession = sqlSession;
    }

    @Override
    public List<Notice> findAll(){
        return sqlSession.selectList(NAMESPACE + "findAll");
    }

    @Override
    public Notice findById(Long id){
        return sqlSession.selectOne(NAMESPACE + "findById", id);
    }

    @Override
    public void insert(Notice notice){
        sqlSession.insert(NAMESPACE + "insert", notice);
    }

    @Override
    public int update(Notice notice){
        return sqlSession.update(NAMESPACE + "update", notice);
    }

    @Override
    public void delete(Long id){
        sqlSession.delete(NAMESPACE + "delete", id);
    }

    @Override
    public int increaseViewCount(Long noticeId){
        return sqlSession.update(NAMESPACE + "increaseViewCount", noticeId);
    }

    @Override
    public List<Notice> findPage(PageRequestDto pageRequestDto){
        var params = new java.util.HashMap<String, Object>();
        params.put("offset", pageRequestDto.getOffset());
        params.put("limit", pageRequestDto.getLimit());

        return sqlSession.selectList(NAMESPACE + "findPage", params);
    }

    @Override
    public int count(){
        return sqlSession.selectOne(NAMESPACE + "count");
    }
}