package com.example.shoppingmall.user.service;

import com.example.shoppingmall.user.dao.UserDao;
import com.example.shoppingmall.user.domain.User;
import com.example.shoppingmall.user.exception.LoginFailedException;
import com.example.shoppingmall.user.exception.UserNotFoundException;
import org.springframework.stereotype.Service;

@Service
public class UserService {

    private final UserDao userDao;

    public UserService(UserDao userDao) {
        this.userDao = userDao;
    }

    public void createUser(User user) {
        userDao.insert(user);
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

    // 이메일 중복 체크 메서드
    public boolean isEmailExists(String email) {
        User user = userDao.findByEmail(email);
        return user != null;
    }

    public void updateUser(User user) {
        userDao.update(user);
    }

    public void deleteUser(Long id) {
        userDao.delete(id);
    }

    public User login(User user) {
        User foundUser = userDao.findByEmailAndPassword(user.getEmail(), user.getPassword());
        System.out.println(foundUser);
        if (foundUser == null) {
            throw new LoginFailedException("이메일 또는 비밀번호가 일치하지 않습니다.");
        }
        return foundUser;
    }

    public void updatePassword(Long userId, String newPassword) {
        User user = userDao.findById(userId);
        if (user != null) {
            user.setPassword(newPassword);
            user.setUpdatedAt(LocalDateTime.now());
            userDao.update(user);
        }
    }

}