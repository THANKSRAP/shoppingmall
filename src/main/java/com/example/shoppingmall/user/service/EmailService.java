
package com.example.shoppingmall.user.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

import java.security.SecureRandom;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

@Service
@RequiredArgsConstructor
@Slf4j
public class EmailService {

    private final JavaMailSender mailSender;

    // 인증 코드 저장소 (실제 운영에서는 Redis 사용 권장)
    private final ConcurrentHashMap<String, String> verificationCodes = new ConcurrentHashMap<>();

    // 인증 완료 상태 저장소 추가
    private final ConcurrentHashMap<String, Boolean> verifiedEmails = new ConcurrentHashMap<>();

    // 인증 코드 만료 시간 관리
    private final ScheduledExecutorService scheduler = Executors.newScheduledThreadPool(1);

    // 일반 키(Spring Boot X)
    @Value("${mail.username:noreply@shoppingmall.com}")
    private String fromEmail;

    /**
     * 이메일 인증 코드 발송
     */
    public boolean sendVerificationEmail(String toEmail) {
        try {
            // 6자리 인증 코드 생성
            String verificationCode = generateVerificationCode();

            // 인증 코드 저장 (5분 후 자동 삭제)
            verificationCodes.put(toEmail, verificationCode);
            scheduler.schedule(() -> verificationCodes.remove(toEmail), 5, TimeUnit.MINUTES);

            // 이메일 내용 작성
            SimpleMailMessage message = new SimpleMailMessage();
            message.setFrom(fromEmail);
            message.setTo(toEmail);
            message.setSubject("[쇼핑몰] 이메일 인증 코드");
            message.setText(createEmailContent(verificationCode));

            // 이메일 발송
            mailSender.send(message);
            log.info("이메일 인증 코드 발송 완료: {}", toEmail);

            // 개발 확인용 콘솔 출력
            System.out.println("━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━");
            System.out.println("📧 [Gmail 발송 완료!] " + toEmail);
            System.out.println("🔐 인증코드: " + verificationCode);
            System.out.println("━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━");

            return true;

        } catch (Exception e) {
            log.error("❌ 이메일 발송 실패: {}", toEmail, e);

            // 실패 시에도 개발용으로 콘솔 출력
            String backupCode = generateVerificationCode();
            verificationCodes.put(toEmail, backupCode);
            scheduler.schedule(() -> verificationCodes.remove(toEmail), 5, TimeUnit.MINUTES);

            System.out.println("━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━");
            System.out.println("⚠️ [Gmail 발송 실패 - 개발용 출력]");
            System.out.println("📬 수신자: " + toEmail);
            System.out.println("🔐 인증코드: " + backupCode);
            System.out.println("📄 오류: " + e.getMessage());
            System.out.println("━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━");

            return true; // 개발용으로는 성공 처리
        }
    }


    /**
     * 인증 코드 검증 + 인증 완료 상태 저장 추가
     */
    public boolean verifyCode(String email, String inputCode) {
        String storedCode = verificationCodes.get(email);

        if (storedCode == null) {
            log.warn("만료되거나 존재하지 않는 인증 코드: {}", email);
            return false;
        }

        boolean isValid = storedCode.equals(inputCode);

        if (isValid) {
            // 인증 성공 시 코드 삭제
            verificationCodes.remove(email);

            // 🎯 인증 완료 상태 저장 (5분 유지)
            verifiedEmails.put(email, true);
            scheduler.schedule(() -> verifiedEmails.remove(email), 5, TimeUnit.MINUTES);

            log.info("이메일 인증 성공: {}", email);
        } else {
            log.warn("잘못된 인증 코드 입력: {}", email);
        }

        return isValid;
    }


    /**
     * 6자리 숫자 인증 코드 생성
     */
    private String generateVerificationCode() {
        SecureRandom random = new SecureRandom();
        int code = 100000 + random.nextInt(900000); // 100000 ~ 999999
        return String.valueOf(code);
    }

    /**
     * 이메일 본문 내용 생성
     */
    private String createEmailContent(String verificationCode) {
        return String.format(
                "안녕하세요!\n\n" +
                        "쇼핑몰 회원가입을 위한 이메일 인증 코드입니다.\n\n" +
                        "인증 코드: %s\n\n" +
                        "위 코드를 회원가입 페이지에 입력해주세요.\n" +
                        "인증 코드는 5분 후 만료됩니다.\n\n" +
                        "감사합니다.",
                verificationCode
        );
    }

    /**
     * 특정 이메일의 인증 코드 존재 여부 확인
     */
    public boolean hasVerificationCode(String email) {
        return verificationCodes.containsKey(email);
    }

    /**
     *  이메일 인증 완료 상태 확인
     */
    public boolean isEmailVerified(String email) {
        return verifiedEmails.getOrDefault(email, false);
    }

}