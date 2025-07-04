package com.example.shoppingmall.notice.domain.dto;

public class PageRequest {
    private final int page;
    private final int size;

    public PageRequest(int page, int size){
        if (page < 1 || size <1){
            throw new IllegalArgumentException("page와 size는 1 이상이어야 합니다.");
        }
        this.page = page;
        this.size = size;
    }
    public int getOffset(){
        return (page - 1) * size;
    }
    public int getLimit(){
        return size;
    }
    public int getPage() {
        return page;
    }

    public int getSize() {
        return size;
    }
}
