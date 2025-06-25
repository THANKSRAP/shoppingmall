package com.example.shoppingmall.itemquestion.service;

import com.example.shoppingmall.notice.domain.dto.PageRequest;
import com.example.shoppingmall.itemquestion.dao.ItemQuestionDao;
import com.example.shoppingmall.itemquestion.domain.ItemQuestion;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class ItemQuestionService {

    private final ItemQuestionDao itemQuestionDao;




    //전체 조회
    public List<ItemQuestion> getAllItemQuestionId() {
        return itemQuestionDao.findAll();
    }

    //ID로 조회
    public ItemQuestion getItemQuestionById(Long id){
        if (id == null){
            throw new IllegalArgumentException("상품문의 ID는 null일 수 없습니다.");
        }

        ItemQuestion itemQuestion = itemQuestionDao.findById(id);
        if(itemQuestion == null){
            throw new RuntimeException("상품문의를 찾을 수 없습니다. ID" + id);
        }
        return itemQuestion;
    }
    //등록
    public void createItemQuestion(ItemQuestion itemQuestion){
        // 비밀글 여부 확인 (폼에서 체크박스가 체크되지 않은 경우 기본값 설정)
        if(itemQuestion.isSecret()) {
            System.out.println("비밀글로 설정됨: " + itemQuestion.getTitle());
        }
        itemQuestionDao.insert(itemQuestion);
    }
    //수정
    public int updateItemQuestion(ItemQuestion itemQuestion){
        return itemQuestionDao.update(itemQuestion);
    }
    //삭제
    public void deleteItemQuestion(Long id){
        itemQuestionDao.delete(id);
    }
    //페이징 목록 조회
    public List<ItemQuestion> getPage(PageRequest pageRequest){
        return itemQuestionDao.findPage(pageRequest);
    }
    public int getTotalCount(){
        return itemQuestionDao.count();
    }
}
