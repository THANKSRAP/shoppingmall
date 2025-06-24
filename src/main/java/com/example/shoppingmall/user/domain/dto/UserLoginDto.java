package com.example.shoppingmall.user.domain.dto;

import lombok.Data;

@Data
public class UserLoginDto {
    private String email;
    private String password;
}
