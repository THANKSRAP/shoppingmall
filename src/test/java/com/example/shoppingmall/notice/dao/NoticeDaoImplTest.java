package com.example.shoppingmall.notice.dao;

import com.example.shoppingmall.notice.domain.Notice;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringRunner;

import java.time.LocalDateTime;
import java.util.List;

import static org.junit.Assert.*;


@RunWith(SpringRunner.class)  // ✅ Spring 테스트 환경을 실행
@ContextConfiguration(locations = {
        "file:src/main/webapp/WEB-INF/spring/root-context.xml"  // ✅ 설정 파일 경로
})
public class NoticeDaoImplTest {

    @Autowired
    private NoticeDao noticeDao;

    // 공지사항 전체 조회
    @Test
    public void testFindAll(){
        List<Notice> noticeList = noticeDao.findAll();

        assertNotNull(noticeList);
        assertTrue(noticeList.size()>0);
        for (Notice notice:noticeList){
            System.out.println(notice);
        }
    }
    // 공지사항 상세조회 테스트
    @Test
    public void testFindById(){
        // 1. 공지사항 데이터 insert해서 저장.
        Notice notice = new Notice();
//        notice.setNoticeId(14L);
        notice.setAdminId(3L);
        notice.setTitle("test");
        notice.setContent("test ing");
        notice.setViewCount(0);
        notice.setStatus("ACTIVE");
        notice.setisPinned(false);

        System.out.println("notice.getNoticeId() = " + notice.getNoticeId());
        noticeDao.insert(notice);
        //위 insert 문 처럼
        Notice notice1 = noticeDao.findById(13l);
        System.out.println("notice.getNoticeId() = " + notice1.getNoticeId());
        assertNotNull(notice1.getNoticeId());
        // 2. 공지사항의 noticeId를 이용해서 findById를 조회

        // 3. assert로 검증(조회 결과가 notnull이 아니고, 내용(title, content 등)이 정확한지)
        assertEquals("test", notice1.getTitle());
        assertEquals("test ing", notice1.getContent());
        // assertEquals(Long.valueOf(2), found.getAdminId());
    }

    //공지사항 등록
    @Test
    public void testInsertNotice(){

        Notice notice = new Notice();
        notice.setAdminId(1L);
        notice.setTitle("테스트 제목6");
        notice.setContent("테스트 내용입니다.1");
        notice.setViewCount(0);
        notice.setStatus("ACTIVE");
//        notice.setPinned(false);
        System.out.println(notice.getNoticeId());

        noticeDao.insert(notice);

        // assertNotNull(notice.getNoticeId());
    }
    // 공지사항 수정
    @Test
    public void testUpdateNotice(){
        Notice notice = noticeDao.findById(13l);
        assertNotNull(notice);
        // 1. 수정할 내용 세팅
        notice.setTitle("Updated Title");
        notice.setContent("Updated Content");



        int rows = noticeDao.update(notice);  // 업데이트 실행
        assertEquals(1, rows); // 1건이 수정되었는지 확인

        // 2. 다시 조회해서 변경 내용 검증
        Notice updated = noticeDao.findById(notice.getNoticeId());
        assertEquals("Updated Title", updated.getTitle());
        assertEquals("Updated Content", updated.getContent());
    }
}