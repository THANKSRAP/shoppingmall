package com.example.shoppingmall.user.controller;

import java.time.LocalDateTime;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import com.example.shoppingmall.user.domain.User;
import com.example.shoppingmall.user.service.UserService;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Controller
@RequestMapping("/user")
@RequiredArgsConstructor
@Slf4j
public class UserViewController {

    private final UserService userService;

    /**
     * 사용자 정보 조회
     */
    @GetMapping("/{userId}")
    public String getUserInfo(@PathVariable Long userId, Model model, HttpServletRequest request) {
        try {
            // 로그인 체크
            if (!isLoggedIn(request)) {
                log.warn("로그인하지 않은 사용자가 사용자 정보 조회 시도: userId={}", userId);
                return "redirect:/loginForm";
            }

            User user = userService.getUserById(userId);
            model.addAttribute("user", user);
            log.info("사용자 정보 조회 완료: userId={}", userId);
            return "user/userInfo";
            
        } catch (Exception e) {
            log.error("사용자 정보 조회 실패: userId={}", userId, e);
            model.addAttribute("error", "사용자 정보를 불러오는 중 오류가 발생했습니다.");
            return "redirect:/";
        }
    }

    /**
     * 사용자 정보 수정 폼
     */
    @GetMapping("/edit")
    public String editForm(HttpServletRequest request, Model model) {
        try {
            // 로그인 체크
            if (!isLoggedIn(request)) {
                log.warn("로그인하지 않은 사용자가 정보 수정 폼 접근 시도");
                return "redirect:/loginForm";
            }

            Long userId = getCurrentUserId(request);
            User currentUser = userService.getUserById(userId);
            model.addAttribute("user", currentUser);
            log.info("사용자 정보 수정 폼 로드: userId={}", userId);
            return "user/editForm";
            
        } catch (Exception e) {
            log.error("사용자 정보 수정 폼 로드 실패", e);
            model.addAttribute("error", "정보 수정 페이지를 불러오는 중 오류가 발생했습니다.");
            return "redirect:/";
        }
    }

    /**
     * 사용자 정보 수정 처리
     */
    @PostMapping("/edit")
    public String editUser(@ModelAttribute User user, HttpServletRequest request, Model model) {
        try {
            // 로그인 체크
            if (!isLoggedIn(request)) {
                log.warn("로그인하지 않은 사용자가 정보 수정 시도");
                return "redirect:/loginForm";
            }

            Long currentUserId = getCurrentUserId(request);
            
            // 현재 로그인한 사용자만 자신의 정보를 수정할 수 있도록 체크
            if (!currentUserId.equals(user.getUser_id())) {
                log.warn("권한 없는 사용자가 정보 수정 시도: 요청자={}, 대상={}", currentUserId, user.getUser_id());
                model.addAttribute("error", "본인의 정보만 수정할 수 있습니다.");
                return "user/editForm";
            }

            // 수정 시간 업데이트
            user.setUpdated_at(LocalDateTime.now());

            userService.updateUser(user);
            log.info("사용자 정보 수정 완료: userId={}", user.getUser_id());
            model.addAttribute("success", "정보가 성공적으로 수정되었습니다.");
            return "redirect:/user/mypage";
            
        } catch (Exception e) {
            log.error("사용자 정보 수정 실패: userId={}", user.getUser_id(), e);
            model.addAttribute("error", "정보 수정 중 오류가 발생했습니다.");
            return "user/editForm";
        }
    }

    /**
     * 계정 삭제 (탈퇴)
     */
    @PostMapping("/withdraw")
    public String withdrawUser(HttpServletRequest request, Model model) {
        try {
            // 로그인 체크
            if (!isLoggedIn(request)) {
                log.warn("로그인하지 않은 사용자가 회원 탈퇴 시도");
                return "redirect:/loginForm";
            }

            Long currentUserId = getCurrentUserId(request);
            User currentUser = userService.getUserById(currentUserId);

            // 탈퇴 처리 (실제 삭제가 아닌 탈퇴 플래그 설정)
            currentUser.setIs_withdrawal(true);
            currentUser.setUpdated_at(LocalDateTime.now());
            userService.updateUser(currentUser);

            // 탈퇴 후 세션 무효화
            request.getSession().invalidate();
            
            log.info("회원 탈퇴 완료: userId={}", currentUserId);
            return "redirect:/";
            
        } catch (Exception e) {
            log.error("회원 탈퇴 실패", e);
            model.addAttribute("error", "회원 탈퇴 중 오류가 발생했습니다.");
            return "user/mypage";
        }
    }

    /**
     * 주문 내역 페이지
     */
    @GetMapping("/orders")
    public String orders(HttpServletRequest request, Model model) {
        try {
            if (!isLoggedIn(request)) {
                log.warn("로그인하지 않은 사용자가 주문 내역 접근 시도");
                return "redirect:/loginForm";
            }

            Long currentUserId = getCurrentUserId(request);
            // TODO: 주문 서비스를 통해 주문 내역을 가져와야 함
            // List<Order> orders = orderService.getOrdersByUserId(currentUserId);
            // model.addAttribute("orders", orders);
            
            log.info("주문 내역 페이지 접근: userId={}", currentUserId);
            return "user/orders";
            
        } catch (Exception e) {
            log.error("주문 내역 페이지 로드 실패", e);
            model.addAttribute("error", "주문 내역을 불러오는 중 오류가 발생했습니다.");
            return "redirect:/";
        }
    }

    /**
     * 내 리뷰 페이지
     */
    @GetMapping("/reviews")
    public String myReviews(HttpServletRequest request, Model model) {
        try {
            if (!isLoggedIn(request)) {
                log.warn("로그인하지 않은 사용자가 내 리뷰 접근 시도");
                return "redirect:/loginForm";
            }

            Long currentUserId = getCurrentUserId(request);
            // TODO: 리뷰 서비스를 통해 내 리뷰를 가져와야 함
            // List<Review> reviews = reviewService.getReviewsByUserId(currentUserId);
            // model.addAttribute("reviews", reviews);
            
            log.info("내 리뷰 페이지 접근: userId={}", currentUserId);
            return "user/myReviews";
            
        } catch (Exception e) {
            log.error("내 리뷰 페이지 로드 실패", e);
            model.addAttribute("error", "내 리뷰를 불러오는 중 오류가 발생했습니다.");
            return "redirect:/";
        }
    }

    /**
     * 마이페이지
     */
    @GetMapping("/mypage")
    public String mypage(HttpServletRequest request, Model model) {
        try {
            if (!isLoggedIn(request)) {
                log.warn("로그인하지 않은 사용자가 마이페이지 접근 시도");
                return "redirect:/loginForm";
            }

            Long currentUserId = getCurrentUserId(request);
            User currentUser = userService.getUserById(currentUserId);
            model.addAttribute("user", currentUser);
            
            log.info("마이페이지 접근: userId={}", currentUserId);
            return "user/mypage";
            
        } catch (Exception e) {
            log.error("마이페이지 로드 실패", e);
            model.addAttribute("error", "마이페이지를 불러오는 중 오류가 발생했습니다.");
            return "redirect:/";
        }
    }

    /**
     * 로그인 상태 확인
     */
    private boolean isLoggedIn(HttpServletRequest request) {
        HttpSession session = request.getSession(false);
        return session != null && session.getAttribute("userId") != null;
    }

    /**
     * 현재 로그인한 사용자 ID 가져오기
     */
    private Long getCurrentUserId(HttpServletRequest request) {
        HttpSession session = request.getSession(false);
        if (session != null) {
            return (Long) session.getAttribute("userId");
        }
        return null;
    }
} 