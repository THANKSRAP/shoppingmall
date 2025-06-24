package com.example.shoppingmall.common.dto;

import lombok.Data;
import java.util.List;

@Data
public class PageResult<T> {
    private List<T> data;
    private int totalCount;
    private int totalPages;
    private int pageSize;

    public PageResult(List<T> data, int totalCount, int pageSize) {
        this.data = data;
        this.totalCount = totalCount;
        this.pageSize = pageSize;
        this.totalPages = (int) Math.ceil((double) totalCount / pageSize);
    }
}