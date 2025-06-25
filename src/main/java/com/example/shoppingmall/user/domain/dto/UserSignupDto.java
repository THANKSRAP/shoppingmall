package com.example.shoppingmall.user.domain.dto;

import com.example.shoppingmall.user.domain.User;
import lombok.Data;

@Data
public class UserSignupDto {
    private String email;
    private String password;
    private String name;
    private String phoneNumber;
}