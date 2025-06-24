package com.example.shoppingmall.admin.dao;

import com.example.shoppingmall.admin.dto.LoginHistoryDto;
import com.example.shoppingmall.admin.dto.LoginStatusSearchDto;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

@Mapper  // 어노테이션은 유지하지만 실제로는 Impl이 동작
public interface AdminLoginStatusDao {
    List<LoginHistoryDto> selectLoginHistories(LoginStatusSearchDto searchDto);
    int countLoginHistories(LoginStatusSearchDto searchDto);
    int countActiveUsers();
}