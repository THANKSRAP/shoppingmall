package com.example.shoppingmall.user.dao;

import com.example.shoppingmall.user.domain.User;
import org.apache.ibatis.annotations.Param;

public interface UserDao {

    User selectUser(String id);

    void insert(User user);

    User findById(Long id);

    User findByEmail(String email);

    // 로그인용 메서드 추가 - 이메일과 비밀번호로 사용자 조회
    // Param 어노테이션 제거 - XML에서 Map으로 처리
    User findByEmailAndPassword(@Param("email") String email, @Param("password") String password);


    void update(User user);

    void delete(Long id);
}