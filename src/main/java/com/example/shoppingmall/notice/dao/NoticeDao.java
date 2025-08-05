package com.example.shoppingmall.notice.dao;

import java.util.List;

import com.example.shoppingmall.common.dto.PageRequest;
import com.example.shoppingmall.notice.domain.Notice;

public interface NoticeDao {
    
    /**
     * 모든 공지사항 조회
     */
    List<Notice> findAll();

    /**
     * ID로 공지사항 조회
     */
    Notice findById(Long id);

    /**
     * 공지사항 등록
     */
    void insert(Notice notice);

    /**
     * 공지사항 수정
     */
    int update(Notice notice);

    /**
     * 공지사항 삭제
     */
    void delete(Long id);

    /**
     * 조회수 증가
     */
    int increaseViewCount(Long noticeId);

    /**
     * 페이징 목록 조회
     */
    List<Notice> findPage(PageRequest pageRequest);

    /**
     * 전체 공지사항 수 조회
     */
    int count();
}
