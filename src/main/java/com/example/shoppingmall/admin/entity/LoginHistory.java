package com.example.shoppingmall.admin.entity;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import java.time.LocalDateTime;

@Getter @Setter
@NoArgsConstructor
public class LoginHistory {
    private Long id;
    private String role;        // 권한 구분
    private String userId;      // 사용자 아이디
    private String name;        // 이름
    private LocalDateTime loginDateTime;  // 로그인 일시
    private String userRole;    // 역할
    private String status;      // 상태
}