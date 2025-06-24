package com.example.shoppingmall.user.domain.dto;

import com.example.shoppingmall.user.domain.User;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class UserResponseDto {
    private Long user_id;
    private String password;
    private String name;
    private String email;
    private String role;

    // User 엔티티를 DTO로 변환
    public static UserResponseDto from(User user) {
        return UserResponseDto.builder()
                .user_id(user.getUser_id())
                .password(user.getPassword())
                .name(user.getName())
                .email(user.getEmail())
                .role(user.getRole())
                .build();
    }
}