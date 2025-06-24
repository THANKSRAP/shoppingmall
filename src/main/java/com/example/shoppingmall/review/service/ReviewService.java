package com.example.shoppingmall.review.service;


import com.example.shoppingmall.review.domain.ReviewDto;


import java.util.List;


public interface ReviewService {
    ReviewDto getReviewDetail(Long reviewId);
    void incrementViewCount(Long reviewId);
    List<ReviewDto> getAllReviews();
    List<ReviewDto> getReviewsByItemId(Long itemId);


}