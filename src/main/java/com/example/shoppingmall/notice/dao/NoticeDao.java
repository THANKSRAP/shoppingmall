package com.example.shoppingmall.notice.dao;

import com.example.shoppingmall.notice.domain.Notice;
import com.example.shoppingmall.notice.domain.dto.PageRequest;
import org.apache.ibatis.annotations.Param;

import java.util.List;

public interface NoticeDao {
    List<Notice> findAll();

    Notice findById(Long id);

    void insert(Notice notice);

    int update(Notice notice);

     void delete(Long id);
    // 조회수 증가 메소드
    int increaseViewCount(Long noticeId);

    // 페이징 목록 조회
    List<Notice> findPage(PageRequest pageRequest);
    // 전체 공지사항 수 조회
    int count();
}
