package com.example.shoppingmall.user.domain;

import lombok.Data;
import java.time.LocalDateTime;

@Data
public class User {
    private Long userId;
    private String email;
    private String password;
    private String prePassword;
    private String name;
    private String phoneNumber;
    private String residentRegistrationNumber;  // 주민번호
    private String gender;                       // 성별
    private boolean smsMarketingStatus;               // SMS 마케팅 동의
    private boolean emailMarketingStatus;             // 이메일 마케팅 동의

    private String role;     // enum: ROLE_USER, ROLE_ADMIN
    private String customerStatus;   // enum: ACTIVE, INACTIVE, SUSPENDED
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
    private LocalDateTime registrationAt;


    // === SQL 스키마에서 새로 추가되는 필드들 ===
    private LocalDateTime userIdCreationAt;          // user_id_creation_at timestamp
    private Boolean isWithdrawal;                // is_user_withdrawal boolean
    private LocalDateTime withdrawalAt;          // user_withdrawal_at timestamp
    private String withdrawalReason;             // user_withdrawal_reason varchar(300)

    // 편의 메서드들
    public boolean isAdmin() {
        return "관리자".equals(role) || "ROLE_ADMIN".equals(role);
    }

    public boolean isEmployee() {
        return "직원".equals(role) || "ROLE_EMPLOYEE".equals(role);
    }

    public boolean isCustomer() {
        return "고객".equals(role) || "ROLE_USER".equals(role);
    }


    public Long getUser_id() {
        return userId;
    }

    public void setUser_id(Long userId) {
        this.userId = userId;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getPre_password() {
        return prePassword;
    }

    public void setPre_password(String prePassword) {
        this.prePassword = prePassword;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPhone_number() {
        return phoneNumber;
    }

    public void setPhone_number(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    public String getResident_registration_number() {
        return residentRegistrationNumber;
    }

    public void setResident_registration_number(String residentRegistrationNumber) {
        this.residentRegistrationNumber = residentRegistrationNumber;
    }

    public String getGender() {
        return gender;
    }

    public void setGender(String gender) {
        this.gender = gender;
    }

    public boolean isSms_marketing_status() {
        return smsMarketingStatus;
    }

    public void setSms_marketing_status(boolean smsMarketingStatus) {
        this.smsMarketingStatus = smsMarketingStatus;
    }

    public boolean isEmail_marketing_status() {
        return emailMarketingStatus;
    }

    public void setEmail_marketing_status(boolean emailMarketingStatus) {
        this.emailMarketingStatus = emailMarketingStatus;
    }

    public String getRole() {
        return role;
    }

    public void setRole(String role) {
        this.role = role;
    }

    public String getCustomer_status() {
        return customerStatus;
    }

    public void setCustomer_status(String customerStatus) {
        this.customerStatus = customerStatus;
    }

    public LocalDateTime getCreated_at() {
        return createdAt;
    }

    public void setCreated_at(LocalDateTime createdAt) {
        this.createdAt = createdAt;
    }

    public LocalDateTime getUpdated_at() {
        return updatedAt;
    }

    public void setUpdated_at(LocalDateTime updatedAt) {
        this.updatedAt = updatedAt;
    }

    public LocalDateTime getRegistration_at() {
        return registrationAt;
    }

    public void setRegistration_at(LocalDateTime registrationAt) {
        this.registrationAt = registrationAt;
    }

    public LocalDateTime getUser_id_creation_at() {
        return userIdCreationAt;
    }

    public void setUser_id_creation_at(LocalDateTime userIdCreationAt) {
        this.userIdCreationAt = userIdCreationAt;
    }

    public Boolean getIs_withdrawal() {
        return isWithdrawal;
    }

    public void setIs_withdrawal(Boolean isWithdrawal) {
        this.isWithdrawal = isWithdrawal;
    }

    public LocalDateTime getWithdrawal_at() {
        return withdrawalAt;
    }

    public void setWithdrawal_at(LocalDateTime withdrawalAt) {
        this.withdrawalAt = withdrawalAt;
    }

    public String getWithdrawal_reason() {
        return withdrawalReason;
    }

    public void setWithdrawal_reason(String withdrawalReason) {
        this.withdrawalReason = withdrawalReason;
    }

    // Lombok의 @Data 어노테이션이 자동으로 getter/setter를 생성해주지만,
    // 명시적으로 getPwd() 메서드를 추가합니다.
    public String getPwd() {
        return this.password;
    }

    // setter도 필요한 경우 추가
    public void setPwd(String password) {
        this.password = password;
    }

    @Override
    public String toString() {
        return "User{" +
                "userId=" + userId +
                ", email='" + email + '\'' +
                ", password='" + password + '\'' +
                ", prePassword='" + prePassword + '\'' +
                ", name='" + name + '\'' +
                ", phoneNumber='" + phoneNumber + '\'' +
                ", residentRegistrationNumber='" + residentRegistrationNumber + '\'' +
                ", gender='" + gender + '\'' +
                ", smsMarketingStatus=" + smsMarketingStatus +
                ", emailMarketingStatus=" + emailMarketingStatus +
                ", role='" + role + '\'' +
                ", customerStatus='" + customerStatus + '\'' +
                ", createdAt=" + createdAt +
                ", updatedAt=" + updatedAt +
                ", registrationAt=" + registrationAt +
                ", userIdCreationAt=" + userIdCreationAt +
                ", isWithdrawal=" + isWithdrawal +
                ", withdrawalAt=" + withdrawalAt +
                ", withdrawalReason='" + withdrawalReason + '\'' +
                '}';
    }
}