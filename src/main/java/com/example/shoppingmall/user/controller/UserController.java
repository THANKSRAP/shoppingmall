package com.example.shoppingmall.user.controller;

import com.example.shoppingmall.user.domain.User;
import com.example.shoppingmall.user.service.UserService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.time.LocalDateTime;

@Controller
@RequestMapping("/user")
public class UserController {

    private final UserService userService;

    public UserController(UserService userService) {
        this.userService = userService;
    }

    /**
     * 사용자 정보 조회
     */
    @GetMapping("/{userId}")
    public String getUserInfo(@PathVariable Long userId, Model model, HttpServletRequest request) {
        // 로그인 체크
        if (!LoginController.isLoggedIn(request)) {
            return "redirect:/user/loginForm";
        }

        User user = userService.getUserById(userId);
        model.addAttribute("user", user);
        return "user/userInfo";
    }

    /**
     * 사용자 정보 수정 폼
     */
    @GetMapping("/edit")
    public String editForm(HttpServletRequest request, Model model) {
        // 로그인 체크
        if (!LoginController.isLoggedIn(request)) {
            return "redirect:/user/loginForm";
        }

        User currentUser = LoginController.getCurrentUser(request);
        model.addAttribute("user", currentUser);
        return "user/editForm";
    }

    /**
     * 사용자 정보 수정 처리
     */
    @PostMapping("/edit")
    public String editUser(@ModelAttribute User user, HttpServletRequest request, Model model) {
        // 로그인 체크
        if (!LoginController.isLoggedIn(request)) {
            return "redirect:/user/loginForm";
        }

        try {
            User currentUser = LoginController.getCurrentUser(request);
            // 현재 로그인한 사용자만 자신의 정보를 수정할 수 있도록 체크
            if (!currentUser.getUser_id().equals(user.getUser_id())) {
                model.addAttribute("error", "본인의 정보만 수정할 수 있습니다.");
                return "user/editForm";
            }

            // 수정 시간 업데이트
            user.setUpdated_at(LocalDateTime.now());

            userService.updateUser(user);
            model.addAttribute("success", "정보가 성공적으로 수정되었습니다.");
            return "redirect:/user/mypage";
        } catch (Exception e) {
            model.addAttribute("error", "정보 수정 중 오류가 발생했습니다: " + e.getMessage());
            return "user/editForm";
        }
    }

    /**
     * 계정 삭제 (탈퇴)
     */
    @PostMapping("/withdraw")
    public String withdrawUser(HttpServletRequest request, Model model) {
        // 로그인 체크
        if (!LoginController.isLoggedIn(request)) {
            return "redirect:/user/loginForm";
        }

        try {
            User currentUser = LoginController.getCurrentUser(request);

            // 탈퇴 처리 (실제 삭제가 아닌 탈퇴 플래그 설정)
            currentUser.setIs_withdrawal(true);
            currentUser.setUpdated_at(LocalDateTime.now());
            userService.updateUser(currentUser);

            // 탈퇴 후 세션 무효화
            request.getSession().invalidate();

            return "redirect:/";
        } catch (Exception e) {
            model.addAttribute("error", "회원 탈퇴 중 오류가 발생했습니다: " + e.getMessage());
            return "user/mypage";
        }
    }

    /**
     * 주문 내역 페이지
     */
    @GetMapping("/orders")
    public String orders(HttpServletRequest request, Model model) {
        if (!LoginController.isLoggedIn(request)) {
            return "redirect:/user/loginForm";
        }

        User currentUser = LoginController.getCurrentUser(request);
        // 여기서 주문 서비스를 통해 주문 내역을 가져와야 함
        // List<Order> orders = orderService.getOrdersByUserId(currentUser.getUser_id());
        // model.addAttribute("orders", orders);

        return "user/orders";
    }

    /**
     * 내 리뷰 페이지
     */
    @GetMapping("/reviews")
    public String myReviews(HttpServletRequest request, Model model) {
        if (!LoginController.isLoggedIn(request)) {
            return "redirect:/user/loginForm";
        }

        User currentUser = LoginController.getCurrentUser(request);
        // 여기서 리뷰 서비스를 통해 내 리뷰를 가져와야 함
        // List<Review> reviews = reviewService.getReviewsByUserId(currentUser.getUser_id());
        // model.addAttribute("reviews", reviews);

        return "user/myReviews";
    }
}

//package com.example.shoppingmall.user.controller;
//
//import com.example.shoppingmall.user.domain.User;
//import com.example.shoppingmall.user.service.UserService;
//import org.springframework.stereotype.Controller;
//import org.springframework.ui.Model;
//import org.springframework.web.bind.annotation.*;
//import java.time.LocalDateTime;
//
//
//@Controller
//@RequestMapping("/user")
//public class UserController {
//
//    private final UserService userService;
//
//    public UserController(UserService userService) {
//        this.userService = userService;
//    }
//
//    @GetMapping("/signup")
//    public String signupForm() {
//        return "user/registerForm";
//    }
//
//    @PostMapping("/signup")
//    public String signup(@RequestParam String email,
//                         @RequestParam String password,
//                         @RequestParam String name,
//                         @RequestParam String phone_number,
//                         @RequestParam(defaultValue = "CUSTOMER") String role,                         // ← 🔥 role을 파라미터로 받기
//                         Model model) {        // Model 파라미터 추가
//
//        User user = new User();
//
//        // 기존 필드들은 그대로 사용
//        user.setEmail(email);
//        user.setPassword(password);
//        user.setName(name);
//        user.setPhone_number(phone_number);
//        user.setRole(role);
//        user.setCustomer_status("ACTIVE");
//
//        // 현재 시간 설정
//        LocalDateTime now = LocalDateTime.now();
//
//        // SQL 새 필드들만 추가로 설정
//        user.setCustomer_status("정상이용 가능");
//        user.setIs_withdrawal(false);
//        user.setRegistration_at(now);
//        user.setUser_id_creation_at(now);
//        user.setCreated_at(now);
//        user.setUpdated_at(now);
//        user.setGender("M");  // DB 기본값과 동일
//        user.setSms_marketing_status(false);  // DB 기본값과 동일
//        user.setEmail_marketing_status(false);  // DB 기본값과 동일
//        user.setIs_withdrawal(false);  // DB 기본값과 동일
//
//        userService.createUser(user);
//        model.addAttribute("user", user);  // Model에 user 객체 추가
//        return "redirect:/user/loginForm";        // 회원가입 후 로그인 페이지로
//    }
//
//    @PostMapping("/login")
//    public String login(User user) {
//        userService.login(user);
//        return "redirect:/";
//    }
//}
