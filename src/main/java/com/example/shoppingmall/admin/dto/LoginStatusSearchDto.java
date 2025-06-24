package com.example.shoppingmall.admin.dto;

import lombok.Data;

@Data
public class LoginStatusSearchDto {
    private String startDate;
    private String endDate;
    private String role;
    private String name;
    private String status;
    private int page = 1;
    private int pageSize = 20;

    public int getOffset() {
        return (page - 1) * pageSize;
    }
}