
package com.example.shoppingmall.admin.dao.impl;

import com.example.shoppingmall.admin.dao.AdminLoginStatusDao;
import com.example.shoppingmall.admin.dto.LoginHistoryDto;
import com.example.shoppingmall.admin.dto.LoginStatusSearchDto;
import org.apache.ibatis.session.SqlSession;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public class AdminLoginStatusDaoImpl implements AdminLoginStatusDao {

    private final SqlSession sqlSession;
    private static final String NAMESPACE = "AdminLoginStatusMapper.";

    public AdminLoginStatusDaoImpl(SqlSession sqlSession) {
        this.sqlSession = sqlSession;
    }

    @Override
    public List<LoginHistoryDto> selectLoginHistories(LoginStatusSearchDto searchDto) {
        return sqlSession.selectList(NAMESPACE + "selectLoginHistories", searchDto);
    }

    @Override
    public int countLoginHistories(LoginStatusSearchDto searchDto) {
        return sqlSession.selectOne(NAMESPACE + "countLoginHistories", searchDto);
    }

    @Override
    public int countActiveUsers() {
        return sqlSession.selectOne(NAMESPACE + "countActiveUsers");
    }
}