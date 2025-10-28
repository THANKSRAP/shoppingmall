package com.example.shoppingmall;

import com.example.shoppingmall.item.domain.dto.ItemDto;
import com.example.shoppingmall.item.service.ItemService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Slf4j
@Controller
public class HomeController {

    @Autowired
    private ItemService itemService;

    @GetMapping("/")
    public String home(@RequestParam(required = false) String message,
                       HttpServletRequest request,
                       Model model) {

        log.info("==================== HomeController 호출 ====================");
        log.info("요청 IP: {}", request.getRemoteAddr());
        log.info("User-Agent: {}", request.getHeader("User-Agent"));

//        List<ItemDto> bestSellers = itemService.getBestSellers();
//        List<ItemDto> newReleases = itemService.getNewItems();

        // [수정] N+1 문제가 해결된 서비스 메소드를 직접 호출
        // (JOIN을 통해 아이템 목록과 리뷰 요약 정보를 한 번의 쿼리로 가져옴)
        List<ItemDto> bestSellers = itemService.getBestSellersWithReviewSummary();
        List<ItemDto> newReleases = itemService.getNewItemsWithReviewSummary();

//        bestSellers.forEach(item -> {
//            ItemDto summary = itemService.getItemWithReviewSummary(item.getItemId());
//            if (summary != null) {
//                item.setAverageRating(summary.getAverageRating());
//                item.setReviewCount(summary.getReviewCount());
//            }
//        });

//        newReleases.forEach(item -> {
//            ItemDto summary = itemService.getItemWithReviewSummary(item.getItemId());
//            if (summary != null) {
//                item.setAverageRating(summary.getAverageRating());
//                item.setReviewCount(summary.getReviewCount());
//            }
//        });

        model.addAttribute("bestSellers", bestSellers);
        model.addAttribute("newReleases", newReleases);

        // 세션 정보를 올바르게 전달
        HttpSession httpSession = request.getSession(false);
        Map<String, Object> session = new HashMap<>();

        log.info("=== 세션 상태 진단 ===");

        if (httpSession != null) {
            log.info("✅ HTTP 세션 존재 - Session ID: {}", httpSession.getId());
            log.info("세션 생성 시간: {}", new java.util.Date(httpSession.getCreationTime()));
            log.info("마지막 접근 시간: {}", new java.util.Date(httpSession.getLastAccessedTime()));
            log.info("세션 최대 비활성 간격: {} 초", httpSession.getMaxInactiveInterval());

            // 세션의 모든 속성 나열
            log.info("=== 세션 내 모든 속성 ===");
            java.util.Enumeration<String> attributeNames = httpSession.getAttributeNames();
            while (attributeNames.hasMoreElements()) {
                String attributeName = attributeNames.nextElement();
                Object attributeValue = httpSession.getAttribute(attributeName);
                log.info("  - {}: {} (타입: {})",
                        attributeName,
                        attributeValue,
                        attributeValue != null ? attributeValue.getClass().getSimpleName() : "null");
            }

            Long userId = (Long) httpSession.getAttribute("userId");
            String email = (String) httpSession.getAttribute("email");
            Object user = httpSession.getAttribute("user");

            log.info("=== 로그인 관련 세션 속성 ===");
            log.info("userId: {} (타입: {})", userId, userId != null ? userId.getClass().getSimpleName() : "null");
            log.info("email: {} (타입: {})", email, email != null ? email.getClass().getSimpleName() : "null");
            log.info("user: {} (타입: {})", user, user != null ? user.getClass().getSimpleName() : "null");

            // header.html에서 사용하는 정확한 속성명으로 Map에 추가
            session.put("userId", userId);
            session.put("email", email);
            session.put("user", user);

            // 로그인 상태 판단
            if (userId != null && email != null) {
                log.info("✅ 로그인 상태 확인됨 - 사용자: {} (ID: {})", email, userId);
                model.addAttribute("isLoggedIn", true);
            } else {
                log.warn("❌ 세션은 존재하지만 userId 또는 email이 null");
                log.warn("   - userId null 여부: {}", userId == null);
                log.warn("   - email null 여부: {}", email == null);
                model.addAttribute("isLoggedIn", false);
            }
        } else {
            log.info("❌ HTTP Session이 null입니다 (로그인하지 않음)");
            model.addAttribute("isLoggedIn", false);
        }

        // 세션 정보를 모델에 추가
        model.addAttribute("session", session);

        // 메시지 처리
        if (message != null && !message.trim().isEmpty()) {
            log.info("메시지 파라미터: {}", message);
            model.addAttribute("message", message);
        }

        log.info("==================== HomeController 완료 ====================");
        return "home";
    }
}