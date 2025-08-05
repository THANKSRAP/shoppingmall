package com.example.shoppingmall.user.service;

import java.time.LocalDateTime;

import org.springframework.stereotype.Service;

import com.example.shoppingmall.user.dao.UserDao;
import com.example.shoppingmall.user.domain.User;
import com.example.shoppingmall.user.exception.LoginFailedException;
import com.example.shoppingmall.user.exception.UserNotFoundException;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Service
@RequiredArgsConstructor
@Slf4j
public class UserService {

    private final UserDao userDao;

    public void createUser(User user) {
        userDao.insert(user);
        log.info("사용자 생성 완료: {}", user.getEmail());
    }

    public User getUserById(Long id) {
        User user = userDao.findById(id);
        if (user == null) {
            throw new UserNotFoundException("사용자를 찾을 수 없습니다. (ID: " + id + ")");
        }
        return user;
    }

    public User getUserByEmail(String email) {
        return userDao.findByEmail(email);
    }

    public boolean isEmailExists(String email) {
        User user = userDao.findByEmail(email);
        return user != null;
    }

    public void updateUser(User user) {
        userDao.update(user);
        log.info("사용자 정보 업데이트 완료: {}", user.getEmail());
    }

    public void deleteUser(Long id) {
        userDao.delete(id);
        log.info("사용자 삭제 완료: {}", id);
    }

    public User login(User user) {
        User foundUser = userDao.findByEmailAndPassword(user.getEmail(), user.getPassword());
        if (foundUser == null) {
            log.warn("로그인 실패: {}", user.getEmail());
            throw new LoginFailedException("이메일 또는 비밀번호가 일치하지 않습니다.");
        }
        log.info("로그인 성공: {}", user.getEmail());
        return foundUser;
    }

    public void updatePassword(Long userId, String newPassword) {
        User user = userDao.findById(userId);
        if (user != null) {
            user.setPassword(newPassword);
            user.setUpdatedAt(LocalDateTime.now());
            userDao.update(user);
            log.info("비밀번호 변경 완료: {}", user.getEmail());
        }
    }
}