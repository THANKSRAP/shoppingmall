package com.example.shoppingmall.common.dto;

import java.util.List;

import lombok.Getter;

/**
 * 페이징 결과를 위한 DTO
 * 데이터, 전체 개수, 페이지 정보를 포함합니다.
 */
@Getter
public class PageResult<T> {
    private final List<T> data;
    private final int totalCount;
    private final int totalPages;
    private final int pageSize;
    private final int currentPage;

    public PageResult(List<T> data, int totalCount, int pageSize, int currentPage) {
        this.data = data;
        this.totalCount = totalCount;
        this.pageSize = pageSize;
        this.currentPage = currentPage;
        this.totalPages = (int) Math.ceil((double) totalCount / pageSize);
    }

    /**
     * 기본 생성자 (현재 페이지 1로 설정)
     */
    public PageResult(List<T> data, int totalCount, int pageSize) {
        this(data, totalCount, pageSize, 1);
    }

    /**
     * 데이터가 비어있는지 확인
     */
    public boolean isEmpty() {
        return data == null || data.isEmpty();
    }

    /**
     * 데이터가 있는지 확인
     */
    public boolean hasData() {
        return !isEmpty();
    }

    /**
     * 첫 번째 페이지인지 확인
     */
    public boolean isFirstPage() {
        return currentPage == 1;
    }

    /**
     * 마지막 페이지인지 확인
     */
    public boolean isLastPage() {
        return currentPage >= totalPages;
    }

    /**
     * 이전 페이지가 있는지 확인
     */
    public boolean hasPreviousPage() {
        return currentPage > 1;
    }

    /**
     * 다음 페이지가 있는지 확인
     */
    public boolean hasNextPage() {
        return currentPage < totalPages;
    }

    /**
     * 이전 페이지 번호 반환
     */
    public int getPreviousPage() {
        return hasPreviousPage() ? currentPage - 1 : 1;
    }

    /**
     * 다음 페이지 번호 반환
     */
    public int getNextPage() {
        return hasNextPage() ? currentPage + 1 : totalPages;
    }

    /**
     * 페이지 시작 인덱스 계산 (1부터 시작)
     */
    public int getStartIndex() {
        return (currentPage - 1) * pageSize + 1;
    }

    /**
     * 페이지 끝 인덱스 계산
     */
    public int getEndIndex() {
        return Math.min(currentPage * pageSize, totalCount);
    }
}