package com.example.shoppingmall.notice.service;

import com.example.shoppingmall.notice.dao.NoticeDao;
import com.example.shoppingmall.notice.domain.Notice;
import com.example.shoppingmall.notice.domain.dto.PageRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import java.util.List;

@Service
@RequiredArgsConstructor
public class NoticeService {
    private final NoticeDao noticeDao;

    //모든 공지사항을 조회하는 메서드
    public List<Notice> getAllNotices(){
        return noticeDao.findAll();
    }

    //ID로 특정 공지사항을 조회하는 메서드(공지사항 존재하지 않으면 예외 던짐
    public Notice getNoticeById(Long id){
        if (id == null){
            throw new RuntimeException("공지사항 ID는 null일 수 없습니다.");
        }

        Notice notice = noticeDao.findById(id);
        if(notice == null){
            throw new RuntimeException("공지사항을 찾을 수 없습니다. ID" + id);
        }

        return notice;
    }
    //변경된 공지사항 정보를 데이터베이스에 저장합니다.
    public void createdNotice(Notice notice){
        noticeDao.insert(notice);
    }
    //기존 공지사항을 수정하는 메서드
    public int updateNotice(Notice notice) {
        return noticeDao.update(notice);
    }
    //공지사항 삭제하는 메서드
    public Notice deleteNotice(Long id) {
        return noticeDao.delete(id);
    }
    //조회수 증가 메소드
    public int increaseViewCount(Integer noticeId){
        return noticeDao.increaseViewCount(noticeId);
    }
    //페이징 목록 조회
    public List<Notice> getPage(PageRequest pageRequest){
        return noticeDao.findPage(pageRequest);
    }
    public int getTotalCount(){
        return noticeDao.count();
    }
}
