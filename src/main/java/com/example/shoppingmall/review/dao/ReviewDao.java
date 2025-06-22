package com.example.shoppingmall.review.dao;

import com.example.shoppingmall.cart.domain.CartDto;
import com.example.shoppingmall.review.domain.ReviewDto;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

@Mapper
public interface ReviewDao {
    ReviewDto findById(Long reviewId);
    void incrementView(Long reviewId);
    List<ReviewDto> selectAllReviews();
}
