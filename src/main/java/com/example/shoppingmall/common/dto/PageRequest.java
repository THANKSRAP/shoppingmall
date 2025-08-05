package com.example.shoppingmall.common.dto;

import lombok.Getter;

/**
 * 페이징 요청을 위한 DTO
 * 페이지 번호와 페이지 크기를 관리합니다.
 */
@Getter
public class PageRequest {
    private final int page;
    private final int size;

    public PageRequest(int page, int size) {
        validatePageAndSize(page, size);
        this.page = page;
        this.size = size;
    }

    /**
     * 페이지와 크기 유효성 검증
     */
    private void validatePageAndSize(int page, int size) {
        if (page < 1) {
            throw new IllegalArgumentException("페이지 번호는 1 이상이어야 합니다. 현재 값: " + page);
        }
        if (size < 1) {
            throw new IllegalArgumentException("페이지 크기는 1 이상이어야 합니다. 현재 값: " + size);
        }
    }

    /**
     * SQL OFFSET 값 계산
     */
    public int getOffset() {
        return (page - 1) * size;
    }

    /**
     * SQL LIMIT 값 반환
     */
    public int getLimit() {
        return size;
    }

    /**
     * 기본 페이지 요청 생성 (1페이지, 10개씩)
     */
    public static PageRequest ofDefault() {
        return new PageRequest(1, 10);
    }

    /**
     * 페이지 요청 생성
     */
    public static PageRequest of(int page, int size) {
        return new PageRequest(page, size);
    }
} 