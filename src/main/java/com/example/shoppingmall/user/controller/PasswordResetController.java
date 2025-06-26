
package com.example.shoppingmall.user.controller;

import com.example.shoppingmall.user.domain.User;
import com.example.shoppingmall.user.service.UserService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import java.security.SecureRandom;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

@Controller
@RequestMapping("/password")
@RequiredArgsConstructor
@Slf4j
public class PasswordResetController {

    private final UserService userService;

    /**
     * 비밀번호 찾기 폼 페이지
     */
    @GetMapping("/reset")
    public String passwordResetForm() {
        return "user/passwordReset";
    }


    /**
     * 비밀번호 찾기 처리
     */
    @PostMapping("/reset")
    @ResponseBody
    public String processPasswordReset(@RequestParam String email) {
        try {
            User user = userService.getUserByEmail(email);
            if (user == null) {
                return "not_found";
            }

            // 임시 비밀번호 생성 (6자리 숫자)
            String tempPassword = generateTempPassword();

            // 비밀번호 업데이트
            userService.updatePassword(user.getUserId(), tempPassword);

            // 🔒 보안: 임시 비밀번호는 로그에 남기지 않음
            log.info("비밀번호 리셋 완료 - 이메일: {}, 시간: {}",
                    email, LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")));

            return "success:" + tempPassword;

        } catch (Exception e) {
            log.error("비밀번호 리셋 오류", e);
            return "error";
        }
    }

    /**
     * 임시 비밀번호 생성
     */
    private String generateTempPassword() {
        SecureRandom secureRandom = new SecureRandom();

        // 영문 대문자 + 숫자 조합
        String chars = "ABCDEFGHIJKLMNOPQRSTUVWXYZ0123456789";
        StringBuilder password = new StringBuilder();

        // 8자리 랜덤 문자 생성
        for (int i = 0; i < 8; i++) {
            int index = secureRandom.nextInt(chars.length());
            password.append(chars.charAt(index));
        }

        return password.toString();
    }
}