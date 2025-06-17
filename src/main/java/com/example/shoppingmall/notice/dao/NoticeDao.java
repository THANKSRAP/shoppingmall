package com.example.shoppingmall.notice.dao;

import com.example.shoppingmall.notice.domain.Notice;

import java.util.List;

public interface NoticeDao {
    List<Notice> findAll();

    Notice findAllById(long id);

    void insert(Notice notice);

    void update(Notice notice);

    void delete(long id);
}
