package com.example.shoppingmall.user.domain.dto;

import com.example.shoppingmall.user.domain.User;
import lombok.Data;

import java.time.LocalDateTime;

@Data
public class UserDto {
    private Long userId;
    private String email;
    private String password;
    private String name;
    private String phoneNumber;
    private String role;
    private String customerStatus;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;

    public User toDomain() {
        User user = new User();
        user.setUser_id(this.userId);
        user.setEmail(this.email);
        user.setPassword(this.password);
        user.setName(this.name);
        user.setPhone_number(this.phoneNumber);
        user.setRole(this.role);
        user.setCustomer_status(this.customerStatus);
        user.setCreated_at(this.createdAt);
        user.setUpdated_at(this.updatedAt);
        return user;
    }

    public static UserDto fromDto(User user) {
        UserDto dto = new UserDto();
        dto.setUserId(user.getUser_id());
        dto.setEmail(user.getEmail());
        dto.setPassword(user.getPassword());
        dto.setName(user.getName());
        dto.setPhoneNumber(user.getPhone_number());
        dto.setRole(user.getRole());
        dto.setCustomerStatus(user.getCustomer_status());
        dto.setCreatedAt(user.getCreated_at());
        dto.setUpdatedAt(user.getUpdated_at());
        return dto;
    }
}