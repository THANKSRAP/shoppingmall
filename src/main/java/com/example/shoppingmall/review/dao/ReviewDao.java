package com.example.shoppingmall.review.dao;

import com.example.shoppingmall.review.domain.ReviewDto;

import java.util.List;

public interface ReviewDao {
    ReviewDto findById(Long reviewId);
    void incrementView(Long reviewId);
    List<ReviewDto> selectAllReviews();
    List<ReviewDto> selectReviewsByItemId(Long itemId);
    List<ReviewDto> findReviewsByItemId(int itemId);

}