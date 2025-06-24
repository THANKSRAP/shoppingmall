package com.example.shoppingmall.review.service;


import com.example.shoppingmall.review.dao.ReviewDao;
import com.example.shoppingmall.review.domain.ReviewDto;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;


import java.util.List;


@Service
public class ReviewServiceImpl implements ReviewService {


    @Autowired
    private ReviewDao reviewDao;


    @Override
    public ReviewDto getReviewDetail(Long reviewId) {
        return reviewDao.findById(reviewId);
    }


    @Override
    public void incrementViewCount(Long reviewId) {
        reviewDao.incrementView(reviewId);
    }
    @Override
    public List<ReviewDto> getAllReviews() {return reviewDao.selectAllReviews();}


    @Override
    public List<ReviewDto> getReviewsByItemId(Long itemId) {
        return reviewDao.selectReviewsByItemId(itemId);
    }


}