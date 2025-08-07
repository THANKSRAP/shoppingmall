package com.example.shoppingmall.admin.service;

import com.example.shoppingmall.admin.dao.AdminLoginStatusDao;
import com.example.shoppingmall.admin.dto.LoginHistoryDto;
import com.example.shoppingmall.admin.dto.LoginStatusSearchDto;
import com.example.shoppingmall.common.dto.PageResultDto;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class AdminLoginStatusService {

    private final AdminLoginStatusDao adminLoginStatusDao;

    @Autowired
    public AdminLoginStatusService(AdminLoginStatusDao adminLoginStatusDao) {
        this.adminLoginStatusDao = adminLoginStatusDao;
    }

    public PageResultDto<LoginHistoryDto> getLoginHistories(LoginStatusSearchDto searchDto) {
        List<LoginHistoryDto> histories = adminLoginStatusDao.selectLoginHistories(searchDto);
        int totalCount = adminLoginStatusDao.countLoginHistories(searchDto);

        // 번호 설정 (최신순으로 정렬되어 있다고 가정)
        int startRowNumber = totalCount - searchDto.getOffset();
        for (int i = 0; i < histories.size(); i++) {
            histories.get(i).setRowNumber(startRowNumber - i);
        }

        return new PageResultDto<>(histories, totalCount, searchDto.getPageSize());
    }

    public int getActiveUserCount() {
        return adminLoginStatusDao.countActiveUsers();
    }
}