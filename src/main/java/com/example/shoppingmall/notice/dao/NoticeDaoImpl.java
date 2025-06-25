package com.example.shoppingmall.notice.dao;

import com.example.shoppingmall.notice.domain.Notice;
import com.example.shoppingmall.notice.domain.dto.PageRequest;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.session.SqlSession;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public class NoticeDaoImpl implements NoticeDao {
    private final SqlSession sqlSession;
    private static final String namespace = "NoticeMapper.";

    public NoticeDaoImpl(SqlSession sqlSession) {
        this.sqlSession = sqlSession;
    }
        @Override
        public List<Notice> findAll(){
            return sqlSession.selectList("NoticeMapper.findAll");
    }
        @Override
        public Notice findById(Long id){
            return sqlSession.selectOne("NoticeMapper.findById",id);
    }

    @Override
    public void insert(Notice notice){
            sqlSession.insert("NoticeMapper.insert",notice);
    }

    @Override
    public int update(Notice notice){
        return sqlSession.update("NoticeMapper.update",notice);

    }

    @Override
    public void delete(Long id){
            sqlSession.delete("NoticeMapper.delete",id);
    }
    @Override
    public int increaseViewCount(Long noticeId){
        return sqlSession.update("NoticeMapper.increaseViewCount",noticeId);
    }

    @Override
    public List<Notice> findPage(PageRequest pageRequest){
        var params = new java.util.HashMap<String, Object>();
        params.put("offset", pageRequest.getOffset());
        params.put("limit", pageRequest.getLimit());
        return sqlSession.selectList("NoticeMapper.findPage");
    }

    @Override
    public int count(){
        return sqlSession.selectOne(namespace + "count");
    }
}
