package com.example.shoppingmall.notice.dao;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.ibatis.session.SqlSession;
import org.springframework.stereotype.Repository;

import com.example.shoppingmall.common.dto.PageRequest;
import com.example.shoppingmall.notice.domain.Notice;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Repository
@RequiredArgsConstructor
@Slf4j
public class NoticeDaoImpl implements NoticeDao {
    
    private final SqlSession sqlSession;
    private static final String NAMESPACE = "NoticeMapper.";

    @Override
    public List<Notice> findAll() {
        return sqlSession.selectList(NAMESPACE + "findAll");
    }

    @Override
    public Notice findById(Long id) {
        return sqlSession.selectOne(NAMESPACE + "findById", id);
    }

    @Override
    public void insert(Notice notice) {
        sqlSession.insert(NAMESPACE + "insert", notice);
    }

    @Override
    public int update(Notice notice) {
        return sqlSession.update(NAMESPACE + "update", notice);
    }

    @Override
    public void delete(Long id) {
        sqlSession.delete(NAMESPACE + "delete", id);
    }

    @Override
    public int increaseViewCount(Long noticeId) {
        return sqlSession.update(NAMESPACE + "increaseViewCount", noticeId);
    }

    @Override
    public List<Notice> findPage(PageRequest pageRequest) {
        Map<String, Object> params = new HashMap<>();
        params.put("offset", pageRequest.getOffset());
        params.put("limit", pageRequest.getLimit());
        
        return sqlSession.selectList(NAMESPACE + "findPage", params);
    }

    @Override
    public int count() {
        return sqlSession.selectOne(NAMESPACE + "count");
    }
}
