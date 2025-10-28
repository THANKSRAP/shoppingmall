package com.example.shoppingmall.review.dao.impl;

import com.example.shoppingmall.review.dao.ReviewDao;
import com.example.shoppingmall.review.domain.ReviewDto;
import org.apache.ibatis.session.SqlSession;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public class ReviewDaoImpl implements ReviewDao {

    private final SqlSession sqlSession;

    private static final String NAMESPACE = "com.example.shoppingmall.review.dao.ReviewDao.";

    // 생성자 주입
    public ReviewDaoImpl(SqlSession sqlSession) {
        this.sqlSession = sqlSession;
    }

    @Override
    public ReviewDto findById(Long reviewId) {
        return sqlSession.selectOne(NAMESPACE + "findById", reviewId);
    }

    @Override
    public void incrementView(Long reviewId) {
        sqlSession.update(NAMESPACE + "incrementView", reviewId);
    }

    @Override
    public List<ReviewDto> selectAllReviews() {
        return sqlSession.selectList(NAMESPACE + "selectAllReviews");
    }

    @Override
    public List<ReviewDto> selectReviewsByItemId(Long itemId) {
        return sqlSession.selectList(NAMESPACE + "selectReviewsByItemId", itemId);
    }

    @Override
    public List<ReviewDto> findReviewsByItemId(int itemId) {
        return sqlSession.selectList(NAMESPACE + "findReviewsByItemId", itemId);
    }
}