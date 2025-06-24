package com.example.shoppingmall.user.controller;

import com.example.shoppingmall.user.domain.User;
import com.example.shoppingmall.user.service.UserService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import javax.servlet.http.HttpServletRequest;
import java.time.LocalDateTime;

@Controller
@RequestMapping("/register")
public class RegisterController {

    private final UserService userService;

    public RegisterController(UserService userService) {
        this.userService = userService;
    }

    /**
     * 회원가입 폼 페이지
     */
    @GetMapping("/add")
    public String register(HttpServletRequest request) {
        // 이미 로그인된 사용자는 메인 페이지로 리다이렉트
        if (LoginController.isLoggedIn(request)) {
            return "redirect:/";
        }
        return "user/registerForm";
    }

    /**
     * 회원가입 처리 - 모든 파라미터를 필수로 변경
     */
    @PostMapping("/save")
    public String save(@RequestParam String email,
                       @RequestParam String password,
                       @RequestParam String name,
                       @RequestParam String residentRegistrationNumber,  // required = false 제거
                       @RequestParam String phone_number,
                       @RequestParam String gender,
                       @RequestParam(defaultValue = "false") boolean smsMarketingStatus,  // 체크박스는 기본값 설정
                       @RequestParam(defaultValue = "false") boolean emailMarketingStatus, // 체크박스는 기본값 설정
                       Model model) {

        try {
            // 서버 측 유효성 검사
            if (email == null || email.trim().isEmpty()) {
                model.addAttribute("error", "이메일은 필수 입력 사항입니다.");
                setModelAttributes(model, email, name, residentRegistrationNumber,
                        phone_number, gender, smsMarketingStatus, emailMarketingStatus);
                return "user/registerForm";
            }

            if (password == null || password.trim().isEmpty()) {
                model.addAttribute("error", "비밀번호는 필수 입력 사항입니다.");
                setModelAttributes(model, email, name, residentRegistrationNumber,
                        phone_number, gender, smsMarketingStatus, emailMarketingStatus);
                return "user/registerForm";
            }

            if (name == null || name.trim().isEmpty()) {
                model.addAttribute("error", "이름은 필수 입력 사항입니다.");
                setModelAttributes(model, email, name, residentRegistrationNumber,
                        phone_number, gender, smsMarketingStatus, emailMarketingStatus);
                return "user/registerForm";
            }

            if (residentRegistrationNumber == null || residentRegistrationNumber.trim().isEmpty()) {
                model.addAttribute("error", "주민등록번호는 필수 입력 사항입니다.");
                setModelAttributes(model, email, name, residentRegistrationNumber,
                        phone_number, gender, smsMarketingStatus, emailMarketingStatus);
                return "user/registerForm";
            }

            if (phone_number == null || phone_number.trim().isEmpty()) {
                model.addAttribute("error", "전화번호는 필수 입력 사항입니다.");
                setModelAttributes(model, email, name, residentRegistrationNumber,
                        phone_number, gender, smsMarketingStatus, emailMarketingStatus);
                return "user/registerForm";
            }

            if (gender == null || gender.trim().isEmpty()) {
                model.addAttribute("error", "성별은 필수 선택 사항입니다.");
                setModelAttributes(model, email, name, residentRegistrationNumber,
                        phone_number, gender, smsMarketingStatus, emailMarketingStatus);
                return "user/registerForm";
            }

            // 이메일 중복 체크
            User existingUser = userService.getUserByEmail(email);
            if (existingUser != null) {
                model.addAttribute("error", "이미 가입된 이메일입니다.");
                setModelAttributes(model, email, name, residentRegistrationNumber,
                        phone_number, gender, smsMarketingStatus, emailMarketingStatus);
                return "user/registerForm";
            }

            // User 객체 생성 및 데이터 설정
            User user = new User();
            LocalDateTime now = LocalDateTime.now();

            // 폼 데이터 설정
            user.setEmail(email.trim());
            user.setPassword(password.trim());
            user.setName(name.trim());
            user.setResident_registration_number(residentRegistrationNumber.trim());
            user.setPhone_number(phone_number.trim());
            user.setGender(gender.trim());
            user.setSms_marketing_status(smsMarketingStatus);
            user.setEmail_marketing_status(emailMarketingStatus);

            // 기본값 설정
            user.setRole("CUSTOMER");
            user.setCustomer_status("ACTIVE");
            user.setCreated_at(now);
            user.setUpdated_at(now);
            user.setRegistration_at(now);
            user.setUser_id_creation_at(now);
            user.setIs_withdrawal(false);

            // UserService를 통해 DB에 사용자 정보 저장
            userService.createUser(user);

            // 회원가입 성공 메시지와 함께 로그인 페이지로 이동
            model.addAttribute("success", "회원가입이 완료되었습니다. 로그인해주세요.");
            model.addAttribute("email", email); // 로그인 폼에 이메일 미리 입력

            return "user/loginForm";

        } catch (Exception e) {
            // 회원가입 실패 시 에러 메시지와 함께 입력값 유지
            model.addAttribute("error", "회원가입 처리 중 오류가 발생했습니다: " + e.getMessage());
            setModelAttributes(model, email, name, residentRegistrationNumber,
                    phone_number, gender, smsMarketingStatus, emailMarketingStatus);

            return "user/registerForm";
        }
    }

    /**
     * 모델에 입력값 다시 설정하는 헬퍼 메서드
     */
    private void setModelAttributes(Model model, String email, String name, String residentRegistrationNumber,
                                    String phone_number, String gender, boolean smsMarketingStatus, boolean emailMarketingStatus) {
        model.addAttribute("email", email);
        model.addAttribute("name", name);
        model.addAttribute("residentRegistrationNumber", residentRegistrationNumber);
        model.addAttribute("phone_number", phone_number);
        model.addAttribute("gender", gender);
        model.addAttribute("smsMarketingStatus", smsMarketingStatus);
        model.addAttribute("emailMarketingStatus", emailMarketingStatus);
    }
}


//package com.example.shoppingmall.user.controller;
//
//import com.example.shoppingmall.user.domain.User;
//import com.example.shoppingmall.user.service.UserService;
//import org.springframework.stereotype.Controller;
//import org.springframework.web.bind.annotation.GetMapping;
//import org.springframework.web.bind.annotation.PostMapping;
//import org.springframework.web.bind.annotation.RequestParam;
//
//import java.time.LocalDateTime;
//
//@Controller
//public class RegisterController {
//
//    private final UserService userService;
//
//    // UserService 의존성 주입
//    public RegisterController(UserService userService) {
//        this.userService = userService;
//    }
//
//    @GetMapping("/register/add")
//    public String register() {
//        return "user/registerForm";
//    }
//
//    @PostMapping("/register/save")
//    public String save(@RequestParam String email,
//                       @RequestParam String password,
//                       @RequestParam String name,
//                       @RequestParam String residentRegistrationNumber,
//                       @RequestParam String phone_number,
//                       @RequestParam String gender,
//                       @RequestParam(required = false) boolean smsMarketing,
//                       @RequestParam(required = false) boolean emailMarketing) {
//
//        // User 객체 생성 및 데이터 설정
//        User user = new User();
//
//        // 현재 시간 설정
//        LocalDateTime now = LocalDateTime.now();
//
//        // 폼 데이터 설정
//
//        user.setEmail(email);
//        user.setPassword(password);
//        user.setName(name);
//        user.setResident_registration_number(residentRegistrationNumber);
//        user.setPhone_number(phone_number);  // User 클래스의 setPhone() 메서드 사용
//        user.setGender(gender);
//        user.setSms_marketing_status(smsMarketing);
//        user.setEmail_marketing_status(emailMarketing);
//
//        // 기본값 설정
//        user.setRole("CUSTOMER");  // 기본 역할: 일반 사용자
//        user.setCustomer_status("ACTIVE");   // 기본 상태: 활성
//        user.setCreated_at(now);
//        user.setUpdated_at(now);
//        user.setRegistration_at(now);
//        user.setUser_id_creation_at(now);
//        user.setRegistration_at(now);
//        user.setIs_withdrawal(false);  // 탈퇴하지 않음
//
//        // UserService를 통해 DB에 사용자 정보 저장
//        userService.createUser(user);
//
//        // 개발 확인용 로그 (실제 운영에서는 제거하거나 로깅 프레임워크 사용)
//        System.out.println("=== 회원가입 완료 ===");
//        System.out.println("저장된 사용자 정보:");
//        System.out.println("이메일: " + email);
//        System.out.println("이름: " + name);
//        System.out.println("전화번호: " + phone_number);
//        System.out.println("성별: " + gender);
//        System.out.println("SMS 마케팅 동의: " + smsMarketing);
//        System.out.println("이메일 마케팅 동의: " + emailMarketing);
//        System.out.println("====================");
//
//        // 회원가입 완료 후 홈화면으로 리다이렉트
//        return "redirect:/";
//    }
//}