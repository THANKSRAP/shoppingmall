package com.example.shoppingmall.review.controller;


import com.example.shoppingmall.review.domain.ReviewDto;
import com.example.shoppingmall.review.service.ReviewService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
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
public String reviewList(@RequestParam(required = false) Long itemId, Model model) {
    List<ReviewDto> reviews;

    // 🔍 itemId 로그
    System.out.println("📥 요청받은 itemId: " + itemId);

    if (itemId != null) {
        reviews = reviewService.getReviewsByItemId(itemId);
        System.out.println("🔍 해당 itemId에 대한 리뷰 수: " + reviews.size());

    } else {
        reviews = reviewService.getAllReviews();
        System.out.println("📦 전체 리뷰 수: " + reviews.size());
    }

    // ✅ 리뷰 하나하나 확인 (선택사항)
    for (ReviewDto review : reviews) {
        System.out.println("📝 리뷰 ID: " + review.getReviewId() + ", 제목: " + review.getTitle());
    }

    model.addAttribute("reviews", reviews);
    model.addAttribute("itemId", itemId); // JS에서도 사용 가능
    return "review/review";
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

    @GetMapping("/api/list")
    @ResponseBody
    public List<ReviewDto> reviewApiList(@RequestParam(required = false) Long itemId) {
        if (itemId != null) {
            return reviewService.getReviewsByItemId(itemId);
        } else {
            return reviewService.getAllReviews();
        }
    }
}