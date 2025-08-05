package com.example.shoppingmall.review.controller;

import java.util.List;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

import com.example.shoppingmall.review.domain.ReviewDto;
import com.example.shoppingmall.review.service.ReviewService;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Controller
@RequestMapping("/review")
@RequiredArgsConstructor
@Slf4j
public class ReviewViewController {

    private final ReviewService reviewService;

    /**
     * 특정 상품의 리뷰 목록
     */
    @GetMapping("/{itemId}")
    public String getReviewsByItemId(@PathVariable Long itemId, Model model) {
        try {
            log.info("상품 리뷰 조회 요청: itemId={}", itemId);
            
            List<ReviewDto> reviews = reviewService.getReviewsByItemId(itemId);
            log.info("상품 리뷰 조회 완료: itemId={}, 리뷰 수={}", itemId, reviews.size());
            
            model.addAttribute("reviews", reviews);
            model.addAttribute("itemId", itemId);
            return "review/review";
            
        } catch (Exception e) {
            log.error("상품 리뷰 조회 실패: itemId={}", itemId, e);
            model.addAttribute("error", "리뷰를 불러오는 중 오류가 발생했습니다.");
            return "redirect:/";
        }
    }

    /**
     * 전체 리뷰 목록
     */
    @GetMapping
    public String getAllReviews(Model model) {
        try {
            log.info("전체 리뷰 조회 요청");
            
            List<ReviewDto> reviews = reviewService.getAllReviews();
            log.info("전체 리뷰 조회 완료: 리뷰 수={}", reviews.size());
            
            model.addAttribute("reviews", reviews);
            return "review/review";
            
        } catch (Exception e) {
            log.error("전체 리뷰 조회 실패", e);
            model.addAttribute("error", "리뷰를 불러오는 중 오류가 발생했습니다.");
            return "redirect:/";
        }
    }
} 