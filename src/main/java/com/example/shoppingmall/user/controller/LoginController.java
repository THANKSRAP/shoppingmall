package com.example.shoppingmall.user.controller;

import com.example.shoppingmall.user.service.UserService;
import com.example.shoppingmall.user.domain.User;
import com.example.shoppingmall.user.exception.LoginFailedException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.regex.Pattern;

@Slf4j
@Controller
@RequestMapping("/user")
public class LoginController {

    private final UserService userService;

    // 이메일 정규식 패턴
    private static final Pattern EMAIL_PATTERN =
            Pattern.compile("^[a-zA-Z0-9._-]+@[a-zA-Z0-9.-]+\\.[a-zA-Z]{2,4}$");

    @Autowired
    public LoginController(UserService userService) {
        this.userService = userService;
    }

    /**
     * 로그인 폼 페이지
     */
    @GetMapping("/loginForm")
    public String loginForm(HttpServletRequest request, Model model) {
        log.info("==================== 로그인 폼 접근 ====================");
        log.info("요청 IP: {}", request.getRemoteAddr());
        log.info("Referer: {}", request.getHeader("Referer"));


        // 이미 로그인된 사용자는 메인 페이지로 리다이렉트
        HttpSession session = request.getSession(false);
        if (session != null && session.getAttribute("userId") != null) {
            return "redirect:/";
        }

        // 저장된 이메일 정보 복원
        Cookie[] cookies = request.getCookies();
        if (cookies != null) {
            for (Cookie cookie : cookies) {
                if ("rememberedEmail".equals(cookie.getName())) {
                    String email = cookie.getValue();
                    if (email != null && !email.isEmpty()) {
                        model.addAttribute("email", email);
                        model.addAttribute("rememberId", true);
                    }
                    break;
                }
            }
        }
        log.info("로그인 폼 표시");
        return "user/loginForm";
    }

    /**
     * 로그인 처리
     */
    @PostMapping("/loginForm")
    public String login(@RequestParam String email,
                        @RequestParam String password,
                        @RequestParam(required = false, defaultValue = "false") boolean rememberId,
                        HttpServletRequest request,
                        HttpServletResponse response,
                        Model model) {

        try {
            // 1. 서버 사이드 입력값 검증
            String validationError = validateLoginInput(email, password);
            if (validationError != null) {
                model.addAttribute("error", validationError);
                model.addAttribute("email", email);
                model.addAttribute("rememberId", rememberId);
                return "user/loginForm";
            }

            // 2. 로그인 처리
            User loginUser = new User();
            loginUser.setEmail(email.trim().toLowerCase()); // 정규화
            loginUser.setPassword(password);

            log.info("=== 로그인 시도 ===");
            log.info("입력 이메일: {}", email);
            log.info("정규화된 이메일: {}", email.trim().toLowerCase());

            User authenticatedUser = userService.login(loginUser);

            log.info("=== 로그인 성공 ===");
            log.info("authenticatedUser: {}", authenticatedUser);
            log.info("authenticatedUser.getUser_id(): {}", authenticatedUser.getUser_id());
            log.info("authenticatedUser.getEmail(): {}", authenticatedUser.getEmail());
            log.info("authenticatedUser.getName(): {}", authenticatedUser.getName());

            // ✅ 3. 기존 세션 무효화 (중복 로그인 방지)
            HttpSession oldSession = request.getSession(false);
            if (oldSession != null) {
                log.info("기존 세션 무효화: {}", oldSession.getId());
                oldSession.invalidate();
            }

            // ✅ 4. 새 세션 생성 및 정보 저장 (한 번만!)
            HttpSession session = request.getSession(true);
            log.info("새 세션 생성: {}", session.getId());

            // 세션에 사용자 정보 저장
            session.setAttribute("userId", authenticatedUser.getUser_id());
            session.setAttribute("email", authenticatedUser.getEmail());
            session.setAttribute("user", authenticatedUser);
            session.setMaxInactiveInterval(30 * 60);

            log.info("=== 세션에 저장된 정보 ===");
            log.info("Session ID: {}", session.getId());
            log.info("session.setAttribute('userId'): {}", authenticatedUser.getUser_id());
            log.info("session.setAttribute('email'): {}", authenticatedUser.getEmail());
            log.info("session.setAttribute('user'): {}", authenticatedUser);

            // ✅ 세션 저장 후 즉시 확인
            Long savedUserId = (Long) session.getAttribute("userId");
            String savedEmail = (String) session.getAttribute("email");
            log.info("=== 세션 저장 확인 ===");
            log.info("세션에서 다시 조회한 userId: {}", savedUserId);
            log.info("세션에서 다시 조회한 email: {}", savedEmail);

            // ✅ 5. 세션 쿠키 설정
            setupSessionCookie(response, session.getId());

            // 6. 아이디 기억하기 처리
            handleRememberMe(email.trim().toLowerCase(), rememberId, response);

            // 7. 성공 리다이렉트
            return "redirect:/";

        } catch (LoginFailedException e) {
            log.error("로그인 실패: {}", e.getMessage());
            model.addAttribute("error", e.getMessage());
            model.addAttribute("email", email);
            model.addAttribute("rememberId", rememberId);
            return "user/loginForm";
        } catch (Exception e) {
            log.error("로그인 처리 중 시스템 오류 발생: {}", e.getMessage(), e);
            model.addAttribute("error", "시스템 오류가 발생했습니다. 잠시 후 다시 시도해주세요.");
            model.addAttribute("email", email);
            model.addAttribute("rememberId", rememberId);
            return "user/loginForm";
        }
    }



    /**
     *  세션 쿠키 설정 메서드 (URL 노출 방지)
     */
    private void setupSessionCookie(HttpServletResponse response, String sessionId) {
        Cookie sessionCookie = new Cookie("JSESSIONID", sessionId);
        sessionCookie.setMaxAge(-1); // 브라우저 세션 종료 시까지
        sessionCookie.setPath("/");
        sessionCookie.setHttpOnly(true); // XSS 방지
        sessionCookie.setSecure(false); // HTTP 환경에서는 false (HTTPS에서는 true)
        response.addCookie(sessionCookie);
    }


    /**
     * 서버 사이드 입력값 검증
     */
    private String validateLoginInput(String email, String password) {
        // 이메일 검증
        if (email == null || email.trim().isEmpty()) {
            return "이메일을 입력해주세요.";
        }

        if (!EMAIL_PATTERN.matcher(email.trim()).matches()) {
            return "올바른 이메일 형식이 아닙니다.";
        }

        if (email.trim().length() > 100) {
            return "이메일이 너무 깁니다. (최대 100자)";
        }

        // 비밀번호 검증
        if (password == null || password.isEmpty()) {
            return "비밀번호를 입력해주세요.";
        }

        if (password.length() < 8) {
            return "비밀번호는 최소 8자 이상이어야 합니다.";
        }

        if (password.length() > 100) {
            return "비밀번호가 너무 깁니다. (최대 100자)";
        }

        // 추가 보안 검증
        if (containsSqlInjectionPatterns(email) || containsSqlInjectionPatterns(password)) {
            log.warn("의심스러운 입력 감지: email={}", email);
            return "유효하지 않은 입력입니다.";
        }

        return null; // 검증 통과
    }

    /**
     * SQL 인젝션 패턴 검사
     */
    private boolean containsSqlInjectionPatterns(String input) {
        if (input == null) return false;

        String lowerInput = input.toLowerCase();
        String[] dangerousPatterns = {
                "select", "insert", "update", "delete", "drop", "union",
                "script", "<script", "</script", "javascript:", "onload=",
                "onclick=", "onerror=", "alert(", "eval(", "expression("
        };

        for (String pattern : dangerousPatterns) {
            if (lowerInput.contains(pattern)) {
                return true;
            }
        }
        return false;
    }

    /**
     * 아이디 기억하기 쿠키 처리
     */
    private void handleRememberMe(String email, boolean rememberId, HttpServletResponse response) {
        Cookie emailCookie = new Cookie("rememberedEmail", rememberId ? email : "");
        emailCookie.setMaxAge(rememberId ? 30 * 24 * 60 * 60 : 0);
        emailCookie.setPath("/");
        emailCookie.setHttpOnly(true);
        emailCookie.setSecure(false); // HTTP 환경에서는 false로 설정
        response.addCookie(emailCookie);
    }

    /**
     * URL 인코딩 헬퍼 메서드
     */
    private String encodeMessage(String message) {
        try {
            return URLEncoder.encode(message, "UTF-8");
        } catch (UnsupportedEncodingException e) {
            log.error("URL 인코딩 실패: {}", message, e);
            return message; // 인코딩 실패 시 원본 메시지 반환
        }
    }

    /**
     * 로그아웃 처리 (GET)
     */
    @GetMapping("/logout")
    public String logout(HttpServletRequest request, HttpServletResponse response) {
        HttpSession session = request.getSession(false);
        if (session != null) {
            session.invalidate();
        }

        clearSessionCookies(response);

        return "redirect:/?message=" + encodeMessage("로그아웃되었습니다.");
    }

    /**
     * 로그아웃 처리 (POST)
     */
    @PostMapping("/logout")
    public String logoutPost(HttpServletRequest request, HttpServletResponse response) {
        HttpSession session = request.getSession(false);
        if (session != null) {
            session.invalidate();
        }

        clearSessionCookies(response);

        return "redirect:/?message=" + encodeMessage("로그아웃되었습니다.");
    }

    /**
     * 세션 관련 쿠키 제거
     */
    private void clearSessionCookies(HttpServletResponse response) {
        Cookie jsessionCookie = new Cookie("JSESSIONID", null);
        jsessionCookie.setMaxAge(0);
        jsessionCookie.setPath("/");
        response.addCookie(jsessionCookie);
    }

    /**
     * 로그인 상태 확인
     */
    public static boolean isLoggedIn(HttpServletRequest request) {
        HttpSession session = request.getSession(false);
        return session != null && session.getAttribute("userId") != null;
    }

    /**
     * 현재 로그인한 사용자 정보 가져오기
     */
//    public static User getCurrentUser(HttpServletRequest request) {
//        HttpSession session = request.getSession(false);
//        if (session != null) {
//            return (User) session.getAttribute("user");
//        }
//        return null;
//    }

    public static Long getCurrentUserId(HttpServletRequest request) {
        HttpSession session = request.getSession(false);
        if (session != null) {
            return (Long) session.getAttribute("userId");
        }
        return null;
    }



    /**
     * 현재 로그인한 사용자 정보 가져오기 (필요시에만 DB 조회)
     */
    public static User getCurrentUser(HttpServletRequest request) {
        Long userId = getCurrentUserId(request);
        if (userId != null) {
            // 여기서 UserService나 UserDao를 통해 최신 정보 조회
            // 의존성 주입이 어려우므로 각 컨트롤러에서 처리하는 것이 좋음
            return null; // 각 컨트롤러에서 별도 처리
        }
        return null;
    }





    /**
     * 마이페이지
     */
    @GetMapping("/mypage")
    public String mypage(HttpServletRequest request, Model model) {
        HttpSession session = request.getSession(false);
        if (session == null || session.getAttribute("userId") == null) {
            return "redirect:/user/loginForm?msg=" + encodeMessage("로그인이 필요합니다.");
        }

        Long userId = (Long) session.getAttribute("userId");
        model.addAttribute("userId", userId);
        return "user/mypage";
    }
}