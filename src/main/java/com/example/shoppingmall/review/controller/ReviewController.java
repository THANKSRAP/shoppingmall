package com.example.shoppingmall.review.controller;


import com.example.shoppingmall.review.domain.ReviewDto;
import com.example.shoppingmall.review.service.ReviewService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;


import java.util.List;


@Controller
@RequestMapping("/review")
public class ReviewController {
    @Autowired
    private ReviewService reviewService;


    // 이 메서드가 http://localhost:8080/shoppingmall/review 요청을 처리해서 review.html 템플릿을 반환해요.
    @GetMapping // @RequestMapping("/review")에 대한 기본 경로 (즉, /review)
    public String reviewPage() {
        return "review/review"; // src/main/resources/templates/review.html 파일을 찾아서 렌더링해요.
    }


    @GetMapping("/list")
    @ResponseBody
    public List<ReviewDto> getReviewList() {
        return reviewService.getAllReviews();
    }


    @GetMapping("/detail")
    @ResponseBody // @Controller 안에서 DTO를 직접 반환할 때는 @ResponseBody를 명시해주는 게 좋아요!
    public ReviewDto getReviewDetail(@RequestParam("reviewId") Long reviewId) {
        reviewService.incrementViewCount(reviewId);
        return reviewService.getReviewDetail(reviewId);
    }


    @GetMapping("/item/{itemId}")
    @ResponseBody
    public List<ReviewDto> getReviewsByItemId(@PathVariable Long itemId) {
        return reviewService.getReviewsByItemId(itemId);
    }
}