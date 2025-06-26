package com.example.shoppingmall.itemquestion.dao;

import com.example.shoppingmall.itemquestion.domain.ItemQuestion;
import com.example.shoppingmall.notice.domain.dto.PageRequest;
import org.apache.ibatis.annotations.Param;

import java.util.List;

public interface ItemQuestionDao {
    //전체조회
    List<ItemQuestion> findAll();

    //단건조회
    ItemQuestion findById(Long id);

    //등록
    void insert(ItemQuestion itemQuestion);

    //수정
    int update(ItemQuestion itemQuestion);

    //삭제
    void delete(Long id);

    //페이징 목록 조회
    List<ItemQuestion> findPage(PageRequest pageRequest);

    //전체 공지사항 수 조회
    int count();

    //작성자 이름, 상품 이름, 문의 유형, 문의 제목 등으로 문의 조회
    List<ItemQuestion> findByKeyword(@Param("keyword") String keyword, @Param("limit") int limit, @Param("offset") int offset);

    int countByKeyword(@Param("keyword") String keyword);


}
