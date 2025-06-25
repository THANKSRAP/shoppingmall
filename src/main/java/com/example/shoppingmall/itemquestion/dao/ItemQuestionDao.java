package com.example.shoppingmall.itemquestion.dao;

import com.example.shoppingmall.itemquestion.domain.ItemQuestion;
import com.example.shoppingmall.notice.domain.dto.PageRequest;

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
    int delete(Long id);

    //페이징 목록 조회
    List<ItemQuestion> findPage(PageRequest pageRequest);

    //전체 공지사항 수 조회
    int count();
}
