package com.example.shoppingmall.itemquestion.dao;

import java.util.List;

import org.apache.ibatis.annotations.Param;

import com.example.shoppingmall.common.dto.PageRequest;
import com.example.shoppingmall.itemquestion.domain.ItemQuestion;

public interface ItemQuestionDao {
    
    /**
     * 모든 상품 문의 조회
     */
    List<ItemQuestion> findAll();

    /**
     * ID로 상품 문의 조회
     */
    ItemQuestion findById(Long id);

    /**
     * 상품 문의 등록
     */
    void insert(ItemQuestion itemQuestion);

    /**
     * 상품 문의 수정
     */
    int update(ItemQuestion itemQuestion);

    /**
     * 상품 문의 삭제
     */
    void delete(Long id);

    /**
     * 페이징 목록 조회
     */
    List<ItemQuestion> findPage(PageRequest pageRequest);

    /**
     * 전체 상품 문의 수 조회
     */
    int count();

    /**
     * 키워드로 상품 문의 검색
     */
    List<ItemQuestion> findByKeyword(@Param("keyword") String keyword, @Param("limit") int limit, @Param("offset") int offset);

    /**
     * 키워드 검색 결과 개수
     */
    int countByKeyword(@Param("keyword") String keyword);
}
