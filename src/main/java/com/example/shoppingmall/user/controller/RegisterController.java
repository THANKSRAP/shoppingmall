package com.example.shoppingmall.user.controller;

import com.example.shoppingmall.user.domain.User;
import com.example.shoppingmall.user.service.UserService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.net.URLEncoder;
import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;
import java.util.regex.Pattern;

import com.example.shoppingmall.user.service.EmailService;


@Controller
@RequestMapping("/register")
public class RegisterController {

    private final UserService userService;
    private final EmailService emailService;

    // 정규식 패턴 상수 정의 (보안 및 성능 향상)
    private static final Pattern PHONE_PATTERN = Pattern.compile("^\\d{3}-\\d{4}-\\d{4}$"); // 전화번호 형식: 010-1234-5678
    private static final Pattern EMAIL_PATTERN = Pattern.compile("^[a-zA-Z0-9._%+-]+@[a-zA-Z0-9.-]+\\.[a-zA-Z]{2,}$"); // 이메일 형식 검증
    private static final Pattern RESIDENT_NUMBER_PATTERN = Pattern.compile("^\\d{6}-\\d{7}$"); // 주민번호 형식: 123456-1234567


    public RegisterController(UserService userService, EmailService emailService) {
        this.userService = userService;
        this.emailService = emailService;
    }


    /**
     * 회원가입 폼 페이지 - 로그인 사용자 체크 추가
     */
    @GetMapping("/add")
    public String register(HttpServletRequest request) {
        // 이미 로그인된 사용자는 메시지와 함께 홈화면으로 리다이렉트
        if (LoginController.isLoggedIn(request)) {
            try {
                String message = URLEncoder.encode("이미 가입하셨습니다.", "UTF-8");
                return "redirect:/?message=" + message;
            } catch (Exception e) {
                return "redirect:/?message=already_registered";
            }
        }
        return "user/registerForm";
    }

    /**
     * 실시간 이메일 중복 확인
     */
    @PostMapping("/checkEmail")
    @ResponseBody
    public Map<String, Object> checkEmailDuplicate(@RequestParam String email) {
        Map<String, Object> result = new HashMap<>();

        // 이메일 형식 검증
        if (!EMAIL_PATTERN.matcher(email).matches()) {
            result.put("available", false);
            result.put("message", "올바른 이메일 형식이 아닙니다.");
            return result;
        }

        // 중복 확인
        User existingUser = userService.getUserByEmail(email);
        if (existingUser != null) {
            result.put("available", false);
            result.put("message", "이미 사용 중인 이메일입니다.");
        } else {
            result.put("available", true);
            result.put("message", "사용 가능한 이메일입니다.");
        }

        return result;
    }


    /**
     * 이메일 인증 코드 발송
     */
    @PostMapping("/sendVerification")
    @ResponseBody
    public Map<String, Object> sendVerificationEmail(@RequestParam String email) {
        Map<String, Object> result = new HashMap<>();

        try {
            // 이메일 형식 검증
            if (!EMAIL_PATTERN.matcher(email).matches()) {
                result.put("success", false);
                result.put("message", "올바른 이메일 형식이 아닙니다.");
                return result;
            }

            // 중복 확인
            User existingUser = userService.getUserByEmail(email);
            if (existingUser != null) {
                result.put("success", false);
                result.put("message", "이미 가입된 이메일입니다.");
                return result;
            }

            // 인증 코드 발송
            boolean sent = emailService.sendVerificationEmail(email);

            if (sent) {
                result.put("success", true);
                result.put("message", "인증 코드가 발송되었습니다. 이메일을 확인해주세요.");
            } else {
                result.put("success", false);
                result.put("message", "이메일 발송에 실패했습니다. 다시 시도해주세요.");
            }

        } catch (Exception e) {
            result.put("success", false);
            result.put("message", "인증 코드 발송 중 오류가 발생했습니다.");
        }

        return result;
    }

    /**
     * 이메일 인증 코드 확인
     */
    @PostMapping("/verifyEmail")
    @ResponseBody
    public Map<String, Object> verifyEmailCode(@RequestParam String email,
                                               @RequestParam String code) {
        Map<String, Object> result = new HashMap<>();

        try {
            boolean isValid = emailService.verifyCode(email, code);

            if (isValid) {
                result.put("success", true);
                result.put("message", "이메일 인증이 완료되었습니다.");
            } else {
                result.put("success", false);
                result.put("message", "인증 코드가 올바르지 않거나 만료되었습니다.");
            }

        } catch (Exception e) {
            result.put("success", false);
            result.put("message", "인증 확인 중 오류가 발생했습니다.");
        }

        return result;
    }


    /**
     * 회원가입 처리 - 모든 파라미터를 필수로 변경, 강화된 검증 기능
     */
    @PostMapping("/save")
    public String save(@RequestParam String email,
                       @RequestParam String password,
                       @RequestParam String name,
                       @RequestParam String residentRegistrationNumber,  // required = false 제거
                       @RequestParam String phoneNumber,
                       @RequestParam String gender,
                       @RequestParam(defaultValue = "false") boolean smsMarketingStatus,  // 체크박스는 기본값 설정
                       @RequestParam(defaultValue = "false") boolean emailMarketingStatus, // 체크박스는 기본값 설정
                       Model model) {

        try {
            // 서버 측 유효성 검사
            // === 1. 필수 입력 필드 검증 (공백 제거 후 검사) ===
            email = email != null ? email.trim() : "";
            password = password != null ? password.trim() : "";
            name = name != null ? name.trim() : "";
            residentRegistrationNumber = residentRegistrationNumber != null ? residentRegistrationNumber.trim() : "";
            phoneNumber = phoneNumber != null ? phoneNumber.trim() : "";
            gender = gender != null ? gender.trim() : "";

            if (email.isEmpty()) {
                model.addAttribute("error", "이메일은 필수 입력 사항입니다.");
                setModelAttributes(model, email, name, residentRegistrationNumber, phoneNumber, gender, smsMarketingStatus, emailMarketingStatus);
                return "user/registerForm";
            }

            if (password.isEmpty()) {
                model.addAttribute("error", "비밀번호는 필수 입력 사항입니다.");
                setModelAttributes(model, email, name, residentRegistrationNumber, phoneNumber, gender, smsMarketingStatus, emailMarketingStatus);
                return "user/registerForm";
            }

            if (name.isEmpty()) {
                model.addAttribute("error", "이름은 필수 입력 사항입니다.");
                setModelAttributes(model, email, name, residentRegistrationNumber, phoneNumber, gender, smsMarketingStatus, emailMarketingStatus);
                return "user/registerForm";
            }

            if (residentRegistrationNumber.isEmpty()) {
                model.addAttribute("error", "주민등록번호는 필수 입력 사항입니다.");
                setModelAttributes(model, email, name, residentRegistrationNumber, phoneNumber, gender, smsMarketingStatus, emailMarketingStatus);
                return "user/registerForm";
            }

            if (phoneNumber.isEmpty()) {
                model.addAttribute("error", "전화번호는 필수 입력 사항입니다.");
                setModelAttributes(model, email, name, residentRegistrationNumber, phoneNumber, gender, smsMarketingStatus, emailMarketingStatus);
                return "user/registerForm";
            }

            if (gender.isEmpty()) {
                model.addAttribute("error", "성별은 필수 선택 사항입니다.");
                setModelAttributes(model, email, name, residentRegistrationNumber, phoneNumber, gender, smsMarketingStatus, emailMarketingStatus);
                return "user/registerForm";
            }

            // === 2. 형식 검증 (정규식 사용) ===
            // 이메일 형식 검증
            if (!EMAIL_PATTERN.matcher(email).matches()) {
                model.addAttribute("error", "올바른 이메일 형식을 입력해주세요. (예: example@email.com)");
                setModelAttributes(model, email, name, residentRegistrationNumber, phoneNumber, gender, smsMarketingStatus, emailMarketingStatus);
                return "user/registerForm";
            }

            // 전화번호 형식 검증 (000-0000-0000)
            if (!PHONE_PATTERN.matcher(phoneNumber).matches()) {
                model.addAttribute("error", "전화번호는 000-0000-0000 형식으로 '-'를 사용하여 입력해주세요.");
                setModelAttributes(model, email, name, residentRegistrationNumber, phoneNumber, gender, smsMarketingStatus, emailMarketingStatus);
                return "user/registerForm";
            }

            // 주민등록번호 형식 검증 (000000-0000000)
            if (!RESIDENT_NUMBER_PATTERN.matcher(residentRegistrationNumber).matches()) {
                model.addAttribute("error", "주민등록번호는 000000-0000000 형식으로 '-'를 사용하여 입력해주세요.");
                setModelAttributes(model, email, name, residentRegistrationNumber, phoneNumber, gender, smsMarketingStatus, emailMarketingStatus);
                return "user/registerForm";
            }

            // === 3. 비밀번호 강도 검증 (보안 강화) ===
            if (password.length() < 8) {
                model.addAttribute("error", "비밀번호는 8자 이상이어야 합니다.");
                setModelAttributes(model, email, name, residentRegistrationNumber, phoneNumber, gender, smsMarketingStatus, emailMarketingStatus);
                return "user/registerForm";
            }

            // === 4. 성별 값 검증 (보안 강화) ===
            if (!gender.equals("M") && !gender.equals("F")) {
                model.addAttribute("error", "올바른 성별을 선택해주세요.");
                setModelAttributes(model, email, name, residentRegistrationNumber, phoneNumber, gender, smsMarketingStatus, emailMarketingStatus);
                return "user/registerForm";
            }

            // === 5. 이메일 중복 검사 (보안 강화) ===
            User existingUser = userService.getUserByEmail(email);
            if (existingUser != null) {
                model.addAttribute("error", "이미 가입된 이메일입니다. 다른 이메일을 사용해주세요.");
                setModelAttributes(model, email, name, residentRegistrationNumber, phoneNumber, gender, smsMarketingStatus, emailMarketingStatus);
                return "user/registerForm";
            }

            // === 6. User 객체 생성 및 데이터 설정 ===
            User user = new User();
            LocalDateTime now = LocalDateTime.now();

            // 입력 데이터 설정 (trim 적용된 값 사용)
            user.setEmail(email);
            user.setPassword(password);
            user.setName(name);
            user.setResident_registration_number(residentRegistrationNumber);
            user.setPhoneNumber(phoneNumber);
            user.setGender(gender);
            user.setSms_marketing_status(smsMarketingStatus);
            user.setEmail_marketing_status(emailMarketingStatus);

            // 기본값 설정
            user.setRole("CUSTOMER"); // 일반 고객으로 설정
            user.setCustomer_status("ACTIVE"); // 활성 상태로 설정
            user.setCreated_at(now);
            user.setUpdated_at(now);
            user.setRegistration_at(now);
            user.setUser_id_creation_at(now);
            user.setIs_withdrawal(false); // 탈퇴하지 않은 상태

            // === 7. 회원가입 처리 ===
            userService.createUser(user);
            System.out.println("회원가입 처리 완료");
            System.out.println(user);

            // 성공 시 로그인 페이지로 이동 (성공 메시지 포함)
            return "redirect:/user/loginForm?signup=success";

        } catch (Exception e) {
            // 예외 발생 시 에러 메시지와 함께 입력값 유지
            model.addAttribute("error", "회원가입 처리 중 오류가 발생했습니다: " + e.getMessage());
            setModelAttributes(model, email, name, residentRegistrationNumber, phoneNumber, gender, smsMarketingStatus, emailMarketingStatus);
            return "user/registerForm";
        }
    }

    /**
     * 모델에 입력값 다시 설정하는 헬퍼 메서드 (에러 발생 시 입력값 유지용)
     */
    private void setModelAttributes(Model model, String email, String name, String residentRegistrationNumber,
                                    String phoneNumber, String gender, boolean smsMarketingStatus, boolean emailMarketingStatus) {
        model.addAttribute("email", email);
        model.addAttribute("name", name);
        model.addAttribute("residentRegistrationNumber", residentRegistrationNumber);
        model.addAttribute("phoneNumber", phoneNumber);
        model.addAttribute("gender", gender);
        model.addAttribute("smsMarketingStatus", smsMarketingStatus);
        model.addAttribute("emailMarketingStatus", emailMarketingStatus);

        // 🎯 이메일 인증 상태 확인 및 전달
        if (email != null && !email.trim().isEmpty()) {
            try {
                boolean isVerified = emailService.isEmailVerified(email);
                model.addAttribute("emailVerified", isVerified);

                if (isVerified) {
                    model.addAttribute("verificationMessage", "이메일 인증이 완료되었습니다.");
                }
            } catch (Exception e) {
                model.addAttribute("emailVerified", false);
            }
        }
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
//                       @RequestParam String phoneNumber,
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
//        user.setphoneNumber(phoneNumber);  // User 클래스의 setPhone() 메서드 사용
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
//        System.out.println("전화번호: " + phoneNumber);
//        System.out.println("성별: " + gender);
//        System.out.println("SMS 마케팅 동의: " + smsMarketing);
//        System.out.println("이메일 마케팅 동의: " + emailMarketing);
//        System.out.println("====================");
//
//        // 회원가입 완료 후 홈화면으로 리다이렉트
//        return "redirect:/";
//    }
//}