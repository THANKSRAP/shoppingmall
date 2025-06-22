package com.example.shoppingmall.notice.dao;

import com.example.shoppingmall.notice.domain.Notice;
import org.apache.ibatis.session.SqlSession;
import org.springframework.stereotype.Repository;

import java.util.List;

//@Repository
public class NoticeDaoImpl implements NoticeDao {
    private final SqlSession sqlSession;
    private static final String namespace = "NoticeMapper.";

    public NoticeDaoImpl(SqlSession sqlSession) {
        this.sqlSession = sqlSession;
    }
        @Override
        public List<Notice> findAll(){
            return sqlSession.selectList("NoticeMapper.findAll");
    };
        @Override
        public Notice findAllById(long id){
            return sqlSession.selectOne("NoticeMapper.findAllById",id);
    };

    @Override
    public void insert(Notice notice){
            sqlSession.insert("NoticeMapper.insert",notice);
    };

    @Override
    public void update(Notice notice){
            sqlSession.update("NoticeMapper.update",notice);
    };

    @Override
    public void delete(long id){
            sqlSession.delete("NoticeMapper.delete",id);
    };
}
