package com.example.shoppingmall.itemquestion.service;

import java.util.List;

import org.springframework.stereotype.Service;

import com.example.shoppingmall.common.dto.PageRequest;
import com.example.shoppingmall.itemquestion.dao.ItemQuestionDao;
import com.example.shoppingmall.itemquestion.domain.ItemQuestion;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Service
@RequiredArgsConstructor
@Slf4j
public class ItemQuestionService {

    private final ItemQuestionDao itemQuestionDao;

    /**
     * 모든 상품 문의 조회
     */
    public List<ItemQuestion> getAllItemQuestions() {
        return itemQuestionDao.findAll();
    }

    /**
     * ID로 상품 문의 조회
     */
    public ItemQuestion getQuestionById(Long id) {
        if (id == null) {
            throw new IllegalArgumentException("상품 문의 ID는 null일 수 없습니다.");
        }

        ItemQuestion question = itemQuestionDao.findById(id);
        if (question == null) {
            throw new RuntimeException("상품 문의를 찾을 수 없습니다. ID: " + id);
        }
        return question;
    }

    /**
     * 상품 문의 등록
     */
    public void createQuestion(ItemQuestion question) {
        if (question == null) {
            throw new IllegalArgumentException("상품 문의는 null일 수 없습니다.");
        }
        
        if (question.isSecret()) {
            log.info("비밀글로 설정됨: {}", question.getTitle());
        }
        
        itemQuestionDao.insert(question);
        log.info("상품 문의 등록 완료: ID={}, 제목={}", question.getItemQuestionId(), question.getTitle());
    }

    /**
     * 상품 문의 수정
     */
    public int updateQuestion(ItemQuestion question) {
        if (question == null || question.getItemQuestionId() == null) {
            throw new IllegalArgumentException("상품 문의와 ID는 null일 수 없습니다.");
        }
        
        int result = itemQuestionDao.update(question);
        log.info("상품 문의 수정 완료: ID={}, 결과={}", question.getItemQuestionId(), result);
        return result;
    }

    /**
     * 상품 문의 삭제
     */
    public void deleteQuestion(Long id) {
        if (id == null) {
            throw new IllegalArgumentException("삭제할 상품 문의 ID는 null일 수 없습니다.");
        }
        
        itemQuestionDao.delete(id);
        log.info("상품 문의 삭제 완료: ID={}", id);
    }

    /**
     * 페이징 목록 조회
     */
    public List<ItemQuestion> getPage(PageRequest pageRequest) {
        if (pageRequest == null) {
            throw new IllegalArgumentException("페이지 요청은 null일 수 없습니다.");
        }
        
        return itemQuestionDao.findPage(pageRequest);
    }

    /**
     * 전체 개수 조회
     */
    public int getTotalCount() {
        return itemQuestionDao.count();
    }

    /**
     * 키워드 검색
     */
    public List<ItemQuestion> search(String keyword, int page, int pageSize) {
        if (keyword == null || keyword.trim().isEmpty()) {
            throw new IllegalArgumentException("검색 키워드는 null이거나 빈 문자열일 수 없습니다.");
        }
        
        int offset = (page - 1) * pageSize;
        return itemQuestionDao.findByKeyword(keyword, pageSize, offset);
    }

    /**
     * 키워드 검색 결과 개수
     */
    public int countSearch(String keyword) {
        if (keyword == null || keyword.trim().isEmpty()) {
            throw new IllegalArgumentException("검색 키워드는 null이거나 빈 문자열일 수 없습니다.");
        }
        
        return itemQuestionDao.countByKeyword(keyword);
    }
}
