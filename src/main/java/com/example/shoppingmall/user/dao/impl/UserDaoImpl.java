package com.example.shoppingmall.user.dao.impl;

import com.example.shoppingmall.user.dao.UserDao;
import com.example.shoppingmall.user.domain.User;
import org.apache.ibatis.session.SqlSession;
import org.springframework.stereotype.Repository;


// ✅ Map 클래스를 사용하기 위해 import 문을 추가
import java.util.HashMap;
import java.util.Map;



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
        // ✅ Map으로 파라미터 전달 (XML에서 #{email}, #{password}로 접근 가능)
        Map<String, Object> params = new HashMap<>();
        params.put("email", email);
        params.put("password", password);
        return sqlSession.selectOne(NAMESPACE + "findByEmailAndPassword", params);
    }

//    public User findByEmailAndPassword(String email, String password) {
//        User user = new User();
//        user.setEmail(email);
//        user.setPassword(password);
//        return sqlSession.selectOne(NAMESPACE + "findByEmailAndPassword", user);
//    }
}