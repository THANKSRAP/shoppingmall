package com.example.shoppingmall.user.dao.impl;

import com.example.shoppingmall.user.dao.UserDao;
import com.example.shoppingmall.user.domain.User;
import org.apache.ibatis.session.SqlSession;
import org.springframework.stereotype.Repository;

@Repository
public class UserDaoImpl implements UserDao {

    private final SqlSession sqlSession;
    private static final String NAMESPACE = "UserMapper.";

    public UserDaoImpl(SqlSession sqlSession) {
        this.sqlSession = sqlSession;
    }

    @Override
    public User selectUser(String id) {
        return null;
    }

    @Override
    public void insert(User user) {
        sqlSession.insert(NAMESPACE + "insert", user);
    }

    @Override
    public User findByEmail(String email) {
        return sqlSession.selectOne(NAMESPACE + "findByEmail", email);
    }

    @Override
    public User findById(Long id) {
        return sqlSession.selectOne(NAMESPACE + "findById", id);
    }

    @Override
    public void update(User user) {
        sqlSession.update(NAMESPACE + "update", user);
    }

    @Override
    public void delete(Long id) {
        sqlSession.delete(NAMESPACE + "delete", id);
    }

    @Override
    public User findByEmailAndPassword(String email, String password) {
        User user = new User();
        user.setEmail(email);
        user.setPassword(password);
        return sqlSession.selectOne(NAMESPACE + "findByEmailAndPassword", user);
    }
}