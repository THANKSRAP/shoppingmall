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
            User user = userService.findByEmail(email);
            if (user == null) {
                return "not_found";
            }

            // 임시 비밀번호 생성 (6자리 숫자)
            String tempPassword = generateTempPassword();

            // 비밀번호 업데이트
            userService.updatePassword(user.getUserId(), tempPassword);

            log.info("비밀번호 리셋 완료 - 이메일: {}, 임시 비밀번호: {}", email, tempPassword);

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
        return String.format("%06d", (int)(Math.random() * 1000000));
    }
}