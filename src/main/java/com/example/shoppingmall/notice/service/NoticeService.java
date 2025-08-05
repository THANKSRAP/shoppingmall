package com.example.shoppingmall.notice.service;

import java.util.List;

import org.springframework.stereotype.Service;

import com.example.shoppingmall.common.dto.PageRequest;
import com.example.shoppingmall.notice.dao.NoticeDao;
import com.example.shoppingmall.notice.domain.Notice;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Service
@RequiredArgsConstructor
@Slf4j
public class NoticeService {
    
    private final NoticeDao noticeDao;

    /**
     * 모든 공지사항 조회
     */
    public List<Notice> getAllNotices() {
        return noticeDao.findAll();
    }

    /**
     * ID로 특정 공지사항 조회
     */
    public Notice getNoticeById(Long id) {
        if (id == null) {
            throw new IllegalArgumentException("공지사항 ID는 null일 수 없습니다.");
        }

        Notice notice = noticeDao.findById(id);
        if (notice == null) {
            throw new RuntimeException("공지사항을 찾을 수 없습니다. ID: " + id);
        }

        return notice;
    }

    /**
     * 공지사항 생성
     */
    public void createNotice(Notice notice) {
        if (notice == null) {
            throw new IllegalArgumentException("공지사항은 null일 수 없습니다.");
        }
        
        noticeDao.insert(notice);
        log.info("공지사항 생성 완료: ID={}, 제목={}", notice.getNoticeId(), notice.getTitle());
    }

    /**
     * 공지사항 수정
     */
    public int updateNotice(Notice notice) {
        if (notice == null || notice.getNoticeId() == null) {
            throw new IllegalArgumentException("공지사항과 ID는 null일 수 없습니다.");
        }
        
        int result = noticeDao.update(notice);
        log.info("공지사항 수정 완료: ID={}, 결과={}", notice.getNoticeId(), result);
        return result;
    }

    /**
     * 공지사항 삭제
     */
    public void deleteNotice(Long id) {
        if (id == null) {
            throw new IllegalArgumentException("삭제할 공지사항 ID는 null일 수 없습니다.");
        }
        
        noticeDao.delete(id);
        log.info("공지사항 삭제 완료: ID={}", id);
    }

    /**
     * 조회수 증가
     */
    public int increaseViewCount(Long noticeId) {
        if (noticeId == null) {
            throw new IllegalArgumentException("공지사항 ID는 null일 수 없습니다.");
        }
        
        int result = noticeDao.increaseViewCount(noticeId);
        log.info("조회수 증가 완료: ID={}, 결과={}", noticeId, result);
        return result;
    }

    /**
     * 페이징 목록 조회
     */
    public List<Notice> getPage(PageRequest pageRequest) {
        if (pageRequest == null) {
            throw new IllegalArgumentException("페이지 요청은 null일 수 없습니다.");
        }
        
        return noticeDao.findPage(pageRequest);
    }

    /**
     * 전체 개수 조회
     */
    public int getTotalCount() {
        return noticeDao.count();
    }
}
