package com.example.shoppingmall.user.controller;

import com.example.shoppingmall.common.dto.PageResult;
import com.example.shoppingmall.user.dao.UserDao;
import com.example.shoppingmall.user.domain.User;
import com.example.shoppingmall.user.domain.dto.WishlistDto;
import com.example.shoppingmall.user.domain.dto.WishlistSearchDto;
import com.example.shoppingmall.user.service.WishlistService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpSession;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.util.List;

@Slf4j
@Controller
@RequestMapping("/wishlist")
@RequiredArgsConstructor
public class WishlistController {

    private final WishlistService wishlistService;
    private final UserDao userDao;

    @GetMapping
    public String wishlistPage(@RequestParam(defaultValue = "1") int page,
                               HttpSession session, Model model) {

        log.info("관심목록 페이지 접근 시도 - 페이지: {}", page);

        // 세션에서 사용자 정보 확인
        User user = getUserFromSession(session);
        if (user == null) {
            log.warn("로그인하지 않은 사용자가 관심목록 접근 시도");
            String encodedMsg = encodeMessage("관심목록을 보려면 로그인이 필요합니다.");
            return "redirect:/user/loginForm?msg=" + encodedMsg;
        }

        log.info("로그인 사용자: {} (ID: {})", user.getEmail(), user.getUser_id());

        // 검색 조건 설정
        WishlistSearchDto searchDto = new WishlistSearchDto();
        searchDto.setUserId(user.getUser_id());
        searchDto.setPage(page);
        searchDto.setPageSize(10); // 한 페이지에 10개

        // 관심목록 조회 (빈 목록도 정상 처리)
        PageResult<WishlistDto> wishlistPage = wishlistService.getWishlistPage(searchDto);

        // 페이지네이션 정보 계산
        int totalPages = wishlistPage.getTotalPages();
        int startPage = ((page - 1) / 10) * 10 + 1;
        int endPage = Math.min(startPage + 9, totalPages);

        // 모델에 데이터 추가
        model.addAttribute("wishlistPage", wishlistPage);
        model.addAttribute("currentPage", page);
        model.addAttribute("startPage", startPage);
        model.addAttribute("endPage", endPage);
        model.addAttribute("user", user);

        log.info("관심목록 조회 완료 - 총 {}개 아이템, {}페이지",
                wishlistPage.getTotalCount(), totalPages);

        return "user/wishlist";
    }

    /**
     * 관심목록에 상품 추가 (AJAX)
     */
    @PostMapping("/add")
    @ResponseBody
    public String addToWishlist(@RequestParam Long itemId, HttpSession session) {
        log.info("관심목록 추가 요청 - 상품ID: {}", itemId);

        User user = getUserFromSession(session);
        if (user == null) {
            log.warn("로그인하지 않은 사용자가 관심목록 추가 시도");
            return "login_required";
        }

        log.info("=== 사용자 정보 디버깅 ===");
        log.info("user 객체: {}", user);
        log.info("user.getUser_id(): {}", user.getUser_id());
        log.info("user.getEmail(): {}", user.getEmail());
        log.info("user.getName(): {}", user.getName());

        try {
            // 중복 체크 (itemOptionId 제거)
            if (wishlistService.isItemInWishlist(user.getUser_id(), itemId)) {
                log.info("이미 관심목록에 있는 상품 - 사용자: {}, 상품ID: {}", user.getEmail(), itemId);
                return "already_exists";
            }

            // 관심목록 추가 (itemOptionId 제거)
            wishlistService.addToWishlist(user.getUser_id(), itemId);
            log.info("관심목록 추가 완료 - 사용자: {}, 상품ID: {}", user.getEmail(), itemId);
            return "success";

        } catch (Exception e) {
            log.error("관심목록 추가 실패: {}", e.getMessage(), e);
            return "error";
        }
    }


    /**
     * 관심목록 아이템 삭제 (선택 삭제 & 개별 삭제)
     * 배열 파라미터를 콤마 구분 문자열로 받도록 수정
     */

    @PostMapping("/delete")
    @ResponseBody
    public String deleteWishlistItems(@RequestParam("itemIds") String itemIdsStr,
                                      HttpSession session) {
        log.info("관심목록 아이템 삭제 요청 - itemIds: {}", itemIdsStr);

        User user = getUserFromSession(session);
        if (user == null) {
            log.warn("로그인하지 않은 사용자가 관심목록 삭제 시도");
            return "login_required";
        }

        try {
            // 콤마로 구분된 문자열을 배열로 변환
            String[] itemIdArray = itemIdsStr.split(",");

            // 각 상품 삭제
            for (String itemIdStr : itemIdArray) {
                Long itemId = Long.parseLong(itemIdStr.trim());
                wishlistService.deleteWishlistByItem(user.getUser_id(), itemId);
            }

            log.info("관심목록 아이템 삭제 완료 (사용자: {})", user.getEmail());
            return "success";
        } catch (Exception e) {
            log.error("관심목록 아이템 삭제 중 오류 발생: ", e);
            return "error";
        }
    }



    @PostMapping("/deleteAll")
    @ResponseBody
    public String deleteAllWishlist(HttpSession session) {
        log.info("전체 관심목록 삭제 요청");

        User user = getUserFromSession(session);
        if (user == null) {
            log.warn("로그인하지 않은 사용자가 전체 관심목록 삭제 시도");
            return "login_required";
        }

        try {
            wishlistService.deleteAllWishlist(user.getUser_id());
            log.info("전체 관심목록 삭제 완료 (사용자: {})", user.getEmail());
            return "success";
        } catch (Exception e) {
            log.error("전체 관심목록 삭제 중 오류 발생: ", e);
            return "error";
        }
    }


    /**
     * 세션에서 사용자 정보 가져오기 (정리된 버전)
     */
    private User getUserFromSession(HttpSession session) {
        // 1. userId로 조회
        Long userId = (Long) session.getAttribute("userId");
        if (userId != null) {
            User user = userDao.findById(userId);
            if (user != null) {
                return user;
            }
        }

        // 2. 이메일로 조회 (기존 세션 호환성)
        String email = (String) session.getAttribute("email");
        if (email != null) {
            User user = userDao.findByEmail(email);
            if (user != null) {
                // 세션에 userId 저장
                session.setAttribute("userId", user.getUser_id());
                return user;
            }
        }

        return null;
    }


    /**
     * 세션에서 사용자 정보 가져오기
     */
//    private User getUserFromSession(HttpSession session) {
//        // 먼저 User 객체 확인
//        User user = (User) session.getAttribute("user");
//        if (user != null) {
//            return user;
//        }
//
//        // User 객체가 없으면 이메일로 조회
//        String email = (String) session.getAttribute("id");
//        if (email != null) {
//            user = userDao.findByEmail(email);
//            if (user != null) {
//                // 세션에 User 객체 저장 (다음 요청에서 재사용)
//                session.setAttribute("user", user);
//                return user;
//            }
//        }
//
//        return null;
//    }


    /**
     * URL 인코딩 헬퍼 메서드
     */
    private String encodeMessage(String message) {
        try {
            return URLEncoder.encode(message, StandardCharsets.UTF_8.toString());
        } catch (UnsupportedEncodingException e) {
            log.error("URL 인코딩 실패: {}", message, e);
            return message;
        }
    }
}