package com.example.shoppingmall.user.controller;

import java.time.LocalDateTime;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.example.shoppingmall.user.domain.User;
import com.example.shoppingmall.user.service.EmailService;
import com.example.shoppingmall.user.service.UserService;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Controller
@RequiredArgsConstructor
@Slf4j
public class RegisterController {

    private final UserService userService;
    private final EmailService emailService;

    @GetMapping("/registerForm")
    public String registerForm() {
        return "user/registerForm";
    }

    @PostMapping("/registerForm")
    public String register(@RequestParam String email,
                          @RequestParam String password,
                          @RequestParam String name,
                          @RequestParam String phoneNumber,
                          @RequestParam String gender,
                          @RequestParam(required = false, defaultValue = "false") boolean smsMarketing,
                          @RequestParam(required = false, defaultValue = "false") boolean emailMarketing,
                          Model model) {

        try {
            // 이메일 중복 체크
            if (userService.isEmailExists(email)) {
                model.addAttribute("error", "이미 가입된 이메일입니다.");
                return "user/registerForm";
            }

            // 사용자 생성
            User user = new User();
            user.setEmail(email);
            user.setPassword(password);
            user.setName(name);
            user.setPhone_number(phoneNumber);
            user.setGender(gender);
            user.setSms_marketing_status(smsMarketing);
            user.setEmail_marketing_status(emailMarketing);
            user.setCreated_at(LocalDateTime.now());
            user.setUpdated_at(LocalDateTime.now());

            userService.createUser(user);
            log.info("회원가입 완료: {}", email);

            return "redirect:/loginForm";

        } catch (Exception e) {
            log.error("회원가입 실패: {}", email, e);
            model.addAttribute("error", "회원가입 중 오류가 발생했습니다.");
            return "user/registerForm";
        }
    }

    @PostMapping("/sendVerificationEmail")
    @ResponseBody
    public String sendVerificationEmail(@RequestParam String email) {
        try {
            boolean success = emailService.sendVerificationEmail(email);
            if (success) {
                log.info("인증 이메일 발송 성공: {}", email);
                return "success";
            } else {
                log.warn("인증 이메일 발송 실패: {}", email);
                return "error";
            }
        } catch (Exception e) {
            log.error("인증 이메일 발송 중 오류: {}", email, e);
            return "error";
        }
    }

    @PostMapping("/verifyEmail")
    @ResponseBody
    public String verifyEmail(@RequestParam String email, @RequestParam String code) {
        try {
            boolean isValid = emailService.verifyCode(email, code);
            if (isValid) {
                log.info("이메일 인증 성공: {}", email);
                return "success";
            } else {
                log.warn("이메일 인증 실패: {}", email);
                return "error";
            }
        } catch (Exception e) {
            log.error("이메일 인증 중 오류: {}", email, e);
            return "error";
        }
    }
}