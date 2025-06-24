package com.example.shoppingmall.user.domain;

import lombok.Data;
import java.time.LocalDateTime;

@Data
public class User {
    private Long user_id;
    private String email;
    private String password;
    private String pre_password;
    private String name;
    private String phone_number;
    private String resident_registration_number;  // 주민번호
    private String gender;                       // 성별
    private boolean sms_marketing_status;               // SMS 마케팅 동의
    private boolean email_marketing_status;             // 이메일 마케팅 동의

    private String role;     // enum: ROLE_USER, ROLE_ADMIN
    private String customer_status;   // enum: ACTIVE, INACTIVE, SUSPENDED
    private LocalDateTime created_at;
    private LocalDateTime updated_at;
    private LocalDateTime registration_at;


    // === SQL 스키마에서 새로 추가되는 필드들 ===
    private LocalDateTime user_id_creation_at;          // user_id_creation_at timestamp
    private Boolean is_withdrawal;                // is_user_withdrawal boolean
    private LocalDateTime withdrawal_at;          // user_withdrawal_at timestamp
    private String withdrawal_reason;             // user_withdrawal_reason varchar(300)

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
        return user_id;
    }

    public void setUser_id(Long user_id) {
        this.user_id = user_id;
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
        return pre_password;
    }

    public void setPre_password(String pre_password) {
        this.pre_password = pre_password;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPhone_number() {
        return phone_number;
    }

    public void setPhone_number(String phone_number) {
        this.phone_number = phone_number;
    }

    public String getResident_registration_number() {
        return resident_registration_number;
    }

    public void setResident_registration_number(String resident_registration_number) {
        this.resident_registration_number = resident_registration_number;
    }

    public String getGender() {
        return gender;
    }

    public void setGender(String gender) {
        this.gender = gender;
    }

    public boolean isSms_marketing_status() {
        return sms_marketing_status;
    }

    public void setSms_marketing_status(boolean sms_marketing_status) {
        this.sms_marketing_status = sms_marketing_status;
    }

    public boolean isEmail_marketing_status() {
        return email_marketing_status;
    }

    public void setEmail_marketing_status(boolean email_marketing_status) {
        this.email_marketing_status = email_marketing_status;
    }

    public String getRole() {
        return role;
    }

    public void setRole(String role) {
        this.role = role;
    }

    public String getCustomer_status() {
        return customer_status;
    }

    public void setCustomer_status(String customer_status) {
        this.customer_status = customer_status;
    }

    public LocalDateTime getCreated_at() {
        return created_at;
    }

    public void setCreated_at(LocalDateTime created_at) {
        this.created_at = created_at;
    }

    public LocalDateTime getUpdated_at() {
        return updated_at;
    }

    public void setUpdated_at(LocalDateTime updated_at) {
        this.updated_at = updated_at;
    }

    public LocalDateTime getRegistration_at() {
        return registration_at;
    }

    public void setRegistration_at(LocalDateTime registration_at) {
        this.registration_at = registration_at;
    }

    public LocalDateTime getUser_id_creation_at() {
        return user_id_creation_at;
    }

    public void setUser_id_creation_at(LocalDateTime user_id_creation_at) {
        this.user_id_creation_at = user_id_creation_at;
    }

    public Boolean getIs_withdrawal() {
        return is_withdrawal;
    }

    public void setIs_withdrawal(Boolean is_withdrawal) {
        this.is_withdrawal = is_withdrawal;
    }

    public LocalDateTime getWithdrawal_at() {
        return withdrawal_at;
    }

    public void setWithdrawal_at(LocalDateTime withdrawal_at) {
        this.withdrawal_at = withdrawal_at;
    }

    public String getWithdrawal_reason() {
        return withdrawal_reason;
    }

    public void setWithdrawal_reason(String withdrawal_reason) {
        this.withdrawal_reason = withdrawal_reason;
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
}