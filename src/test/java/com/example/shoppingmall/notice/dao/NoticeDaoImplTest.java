package com.example.shoppingmall.notice.dao;

import com.example.shoppingmall.notice.domain.Notice;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringRunner;

import static org.junit.Assert.*;


@RunWith(SpringRunner.class)  // ✅ Spring 테스트 환경을 실행
@ContextConfiguration(locations = {
        "file:src/main/webapp/WEB-INF/spring/root-context.xml"  // ✅ 설정 파일 경로
})
public class NoticeDaoImplTest {

    @Autowired
    private NoticeDao noticeDao;

    @Test
    public void testInsertNotice(){
        Notice notice = new Notice();
        notice.setAdminId(1L);
        notice.setTitle("테스트 제목");
        notice.setContent("테스트 내용입니다.");
        notice.setViewCount(0);
        notice.setStatus("ACTIVE");
        notice.setPinned(false);
        notice.setStartDate("4/18");
        notice.setEndDate("6/18");

        noticeDao.insert(notice);

        assertNotNull(notice.getNoticeId());

    }
}