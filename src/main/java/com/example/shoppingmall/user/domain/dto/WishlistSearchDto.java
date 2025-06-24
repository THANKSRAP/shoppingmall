package com.example.shoppingmall.user.domain.dto;

import lombok.Data;

@Data
public class WishlistSearchDto {
    private Long userId;
    private int page = 1;
    private int pageSize = 10;
    private int offset; // 필드 추가


    public int getOffset() {
        return (page - 1) * pageSize;
    }

    // page 설정 시 자동으로 offset 계산
    public void setPage(int page) {
        this.page = page;
        this.offset = (page - 1) * pageSize;
    }
}