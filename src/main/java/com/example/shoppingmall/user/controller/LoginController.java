package com.example.shoppingmall.user.controller;

import com.example.shoppingmall.user.service.UserService;
import com.example.shoppingmall.user.domain.User;
import com.example.shoppingmall.user.exception.LoginFailedException;
import lombok.extern.slf4j.Slf4j; // 로깅을 위한 Lombok 어노테이션 추가
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

@Slf4j // 디버깅을 위한 로깅 기능 추가(Lombok의 로깅 어노테이션 추가)
@Controller
@RequestMapping("/user")
public class LoginController {

    private final UserService userService;

    @Autowired
    public LoginController(UserService userService) {
        this.userService = userService;
    }

    /**
     * 로그인 폼 페이지
     */
    @GetMapping("/loginForm")
    public String loginForm(HttpServletRequest request, Model model) {
        // 이미 로그인된 사용자는 메인 페이지로 리다이렉트
        HttpSession session = request.getSession(false);
        if (session != null && session.getAttribute("id") != null) {
            return "redirect:/";
        }

        // 쿠키에서 저장된 이메일 읽어오기 (아이디 기억하기 기능)
        Cookie[] cookies = request.getCookies();
        if (cookies != null) {
            for (Cookie cookie : cookies) {
                if ("rememberedEmail".equals(cookie.getName())) {
                    model.addAttribute("rememberedEmail", cookie.getValue());
                    model.addAttribute("rememberId", true);
                    break;
                }
            }
        }

        return "user/loginForm";
    }

    /**
     * 로그아웃 처리
     */
    @GetMapping("/logout")
    public String logout(HttpSession session) {
        // 세션 무효화
        if (session != null) {
            session.invalidate();
        }
        return "redirect:/";
    }

    /**
     * 로그인 처리 - 예외 처리 및 로깅 개선
     */
    @PostMapping("/loginForm")
    public String login(@RequestParam String email,
                        @RequestParam String password,
                        @RequestParam(required = false, defaultValue = "false") boolean rememberId,
                        HttpServletRequest request,
                        HttpServletResponse response,
                        Model model) {  // throws Exception 제거하여 예외를 내부에서 처리

        log.info("로그인 시도 시작 - 이메일: {}", email); // 로그인 시도 로그 추가

        try {
            // 입력값 유효성 검사 추가
            if (email == null || email.trim().isEmpty()) {
                log.warn("로그인 실패 - 이메일이 비어있음");
                model.addAttribute("error", "이메일을 입력해주세요.");
                return "user/loginForm";
            }

            if (password == null || password.trim().isEmpty()) {
                log.warn("로그인 실패 - 비밀번호가 비어있음");
                model.addAttribute("error", "비밀번호를 입력해주세요.");
                return "user/loginForm";
            }

            // UserService를 통한 사용자 인증
            User loginUser = new User();
            loginUser.setEmail(email.trim()); // 공백 제거 추가
            loginUser.setPassword(password.trim()); // 공백 제거 추가

            log.info("UserService.login() 호출 전"); // 서비스 호출 전 로그
            User authenticatedUser = userService.login(loginUser);
            log.info("UserService.login() 호출 후 - 인증된 사용자 ID: {}", authenticatedUser.getUser_id()); // 서비스 호출 후 로그

            // 로그인 성공 시 세션에 사용자 정보 저장 (header.html에서 사용하는 'id' 키 사용)
            HttpSession session = request.getSession();
            session.setAttribute("id", authenticatedUser.getEmail()); // header.html에서 체크하는 키
            session.setAttribute("user", authenticatedUser); // 전체 사용자 객체
            session.setAttribute("userId", authenticatedUser.getUser_id());
            session.setAttribute("userName", authenticatedUser.getName());
            session.setAttribute("userRole", authenticatedUser.getRole());
            session.setAttribute("customerStatus", authenticatedUser.getCustomer_status());

            log.info("로그인 성공 - 세션 생성 완료: {}", authenticatedUser.getEmail()); // 세션 생성 완료 로그

            // 아이디 기억하기 처리
            handleRememberMe(email, rememberId, response);

            return "redirect:/";

        } catch (LoginFailedException e) {
            // LoginFailedException은 이미 RuntimeException을 상속받으므로 별도 처리
            log.warn("로그인 실패 - LoginFailedException: {}", e.getMessage()); // 로그인 실패 상세 로그
            model.addAttribute("error", e.getMessage());
            model.addAttribute("email", email); // 입력했던 이메일 유지
            model.addAttribute("rememberId", rememberId); // 체크박스 상태 유지
            return "user/loginForm";
        } catch (Exception e) {
            // 예상하지 못한 오류 처리 - 상세한 에러 로깅 추가
            log.error("로그인 처리 중 예상치 못한 오류 발생 - 이메일: {}, 오류: {}", email, e.getMessage(), e); // 스택 트레이스 포함한 상세 로그
            model.addAttribute("error", "로그인 처리 중 시스템 오류가 발생했습니다. 잠시 후 다시 시도해주세요."); // 사용자에게 더 친화적인 메시지
            model.addAttribute("email", email);
            model.addAttribute("rememberId", rememberId);
            return "user/loginForm";
        }
    }

    /**
     * 아이디 기억하기 쿠키 처리
     */
    private void handleRememberMe(String email, boolean rememberId, HttpServletResponse response) {
        Cookie cookie = new Cookie("rememberedEmail", rememberId ? email : "");
        cookie.setPath("/");
        cookie.setHttpOnly(true); // XSS 방지

        if (rememberId) {
            cookie.setMaxAge(60 * 60 * 24 * 7); // 7일간 유지
        } else {
            cookie.setMaxAge(0); // 쿠키 삭제
        }

        response.addCookie(cookie);
    }

    /**
     * 로그인 상태 확인 (다른 컨트롤러에서 사용할 수 있는 유틸리티 메서드)
     */
    public static boolean isLoggedIn(HttpServletRequest request) {
        HttpSession session = request.getSession(false);
        return session != null && session.getAttribute("id") != null;
    }

    /**
     * 현재 로그인한 사용자 정보 가져오기
     */
    public static User getCurrentUser(HttpServletRequest request) {
        HttpSession session = request.getSession(false);
        if (session != null) {
            return (User) session.getAttribute("user");
        }
        return null;
    }

    /**
     * 마이페이지 (세션 확인 예시)
     */
    @GetMapping("/mypage")
    public String mypage(HttpServletRequest request, Model model) {
        if (!isLoggedIn(request)) {
            return "redirect:/user/loginForm";
        }

        User currentUser = getCurrentUser(request);
        model.addAttribute("user", currentUser);
        return "user/mypage";
    }
}


//package com.example.shoppingmall.user.controller;
//
//import com.example.shoppingmall.user.dao.UserDao;
//import com.example.shoppingmall.user.domain.User;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.stereotype.Controller;
//import org.springframework.web.bind.annotation.GetMapping;
//import org.springframework.web.bind.annotation.PostMapping;
//import org.springframework.web.bind.annotation.RequestMapping;
//
//import javax.servlet.http.Cookie;
//import javax.servlet.http.HttpServletRequest;
//import javax.servlet.http.HttpServletResponse;
//import javax.servlet.http.HttpSession;
//import java.net.URLEncoder;
//
//@Controller
//@RequestMapping("/user")
//public class LoginController {
//
//    private final UserDao userDao;
//
//    @Autowired
//    public LoginController(UserDao userDao) {
//        this.userDao = userDao;
//    }
//
///* <<<<<<<<<<<<<<  ✨ Windsurf Command ⭐ >>>>>>>>>>>>>>>> */
//    /**
//     * Login form view
//     *
//     * @return login form view
//     */
///* <<<<<<<<<<  5a35d80d-f1ee-45b8-9137-b6e3d556e4a3  >>>>>>>>>>> */
//    @GetMapping("/loginForm")
//    public String loginForm() {
//        return "user/loginForm";
//    }
//
//    @GetMapping("/logout")
//    public String logout(HttpSession session) {
//        // 세션 종료
//        session.invalidate();
//        // 홈으로 이동
//        return "redirect:/";
//    }
//
//    @PostMapping("/loginForm")
//    public String login(String email, String password, boolean rememberId,
//                        HttpServletRequest request, HttpServletResponse response) throws Exception {
//        if (!loginCheck(email, password)) {
//            String msg = URLEncoder.encode("아이디 또는 비밀번호가 일치하지 않습니다.", "utf-8");
//            return "user/loginForm";
//        }
//
//        // 로그인 성공 시 로그인 이력 저장
//        HttpSession session = request.getSession();
//        session.setAttribute("id", email);
//
//        // 로그인 유지 체크박스 처리
//        Cookie cookie = new Cookie("id", rememberId ? email : "");
//        cookie.setPath("/");
//        if (rememberId) {
//            cookie.setMaxAge(60 * 60 * 24 * 7); // 1주일 유지
//        } else {
//            cookie.setMaxAge(0); // 쿠키 삭제
//        }
//        response.addCookie(cookie);
//
//        return "redirect:/";
//    }
//
//    private boolean loginCheck(String id, String pwd) {
//        try {
//            User user = userDao.findByEmail(id);
//            return user != null && user.getPwd().equals(pwd);
//        } catch (Exception e) {
//            e.printStackTrace();
//            return false;
//        }
//    }
//}