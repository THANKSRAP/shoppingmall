package com.example.shoppingmall.user.controller;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.example.shoppingmall.user.domain.User;
import com.example.shoppingmall.user.exception.LoginFailedException;
import com.example.shoppingmall.user.service.UserService;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Controller
@RequiredArgsConstructor
@Slf4j
public class LoginController {

    private final UserService userService;

    @GetMapping("/loginForm")
    public String loginForm() {
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
            loginUser.setEmail(email.trim().toLowerCase());
            loginUser.setPassword(password);

            log.info("로그인 시도: {}", email);

            User authenticatedUser = userService.login(loginUser);

            // 3. 기존 세션 무효화 (중복 로그인 방지)
            HttpSession oldSession = request.getSession(false);
            if (oldSession != null) {
                log.info("기존 세션 무효화: {}", oldSession.getId());
                oldSession.invalidate();
            }

            // 4. 새 세션 생성 및 정보 저장
            HttpSession newSession = request.getSession(true);
            newSession.setAttribute("userId", authenticatedUser.getUser_id());
            newSession.setAttribute("email", authenticatedUser.getEmail());
            newSession.setAttribute("user", authenticatedUser);

            log.info("새 세션 생성: {}, 사용자: {}", newSession.getId(), authenticatedUser.getEmail());

            // 5. 아이디 기억하기 처리
            if (rememberId) {
                Cookie rememberCookie = new Cookie("rememberedEmail", email);
                rememberCookie.setMaxAge(60 * 60 * 24 * 30); // 30일
                rememberCookie.setPath("/");
                response.addCookie(rememberCookie);
            } else {
                // 기존 쿠키 삭제
                Cookie rememberCookie = new Cookie("rememberedEmail", "");
                rememberCookie.setMaxAge(0);
                rememberCookie.setPath("/");
                response.addCookie(rememberCookie);
            }

            log.info("로그인 성공: {}", authenticatedUser.getEmail());
            return "redirect:/";

        } catch (LoginFailedException e) {
            log.warn("로그인 실패: {}", email);
            model.addAttribute("error", e.getMessage());
            model.addAttribute("email", email);
            model.addAttribute("rememberId", rememberId);
            return "user/loginForm";
        } catch (Exception e) {
            log.error("로그인 처리 중 오류 발생: {}", email, e);
            model.addAttribute("error", "로그인 처리 중 오류가 발생했습니다.");
            model.addAttribute("email", email);
            model.addAttribute("rememberId", rememberId);
            return "user/loginForm";
        }
    }

    /**
     * 로그아웃 처리
     */
    @GetMapping("/logout")
    public String logout(HttpServletRequest request) {
        HttpSession session = request.getSession(false);
        if (session != null) {
            log.info("로그아웃: {}", session.getAttribute("email"));
            session.invalidate();
        }
        return "redirect:/";
    }

    /**
     * 로그인 입력값 검증
     */
    private String validateLoginInput(String email, String password) {
        if (email == null || email.trim().isEmpty()) {
            return "이메일을 입력해주세요.";
        }
        if (password == null || password.trim().isEmpty()) {
            return "비밀번호를 입력해주세요.";
        }
        if (!email.contains("@")) {
            return "올바른 이메일 형식을 입력해주세요.";
        }
        return null;
    }
}