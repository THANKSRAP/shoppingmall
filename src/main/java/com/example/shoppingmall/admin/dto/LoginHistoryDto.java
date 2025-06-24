package com.example.shoppingmall.admin.dto;

import lombok.Data;
import java.time.LocalDateTime;

@Data
public class LoginHistoryDto {
    private Long id;
    private int rowNumber;         // 번호 (페이지별 순번)
    private String role;           // 권한 구분 (admin, employee)
    private String userId;         // 사용자 아이디
    private String name;           // 이름
    private LocalDateTime loginDateTime; // 로그인 일시
    private String department;     // 역할 (입출고 담당, 영업 담당 등)
    private String status;         // 상태 (활성, 비활성)
}