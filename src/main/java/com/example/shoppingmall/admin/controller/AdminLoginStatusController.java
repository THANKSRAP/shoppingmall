package com.example.shoppingmall.admin.controller;

import com.example.shoppingmall.admin.dto.LoginHistoryDto;
import com.example.shoppingmall.admin.dto.LoginStatusSearchDto;
import com.example.shoppingmall.admin.service.AdminLoginStatusService;
import com.example.shoppingmall.common.dto.PageResultDto;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
@RequestMapping("/admin")
public class AdminLoginStatusController {

    private final AdminLoginStatusService adminLoginStatusService;

    @Autowired
    public AdminLoginStatusController(AdminLoginStatusService adminLoginStatusService) {
        this.adminLoginStatusService = adminLoginStatusService;
    }

    @GetMapping("/login-status")
    public String loginStatus(
            @RequestParam(defaultValue = "1") int page,
            @RequestParam(required = false) String startDate,
            @RequestParam(required = false) String endDate,
            @RequestParam(required = false) String role,
            @RequestParam(required = false) String name,
            @RequestParam(required = false) String status,
            Model model) {

        int pageSize = 20; // 한 페이지당 20개

        // 검색 조건 설정
        LoginStatusSearchDto searchDto = new LoginStatusSearchDto();
        searchDto.setStartDate(startDate);
        searchDto.setEndDate(endDate);
        searchDto.setRole(role);
        searchDto.setName(name);
        searchDto.setStatus(status);
        searchDto.setPage(page);
        searchDto.setPageSize(pageSize);

        // 데이터 조회
        PageResultDto<LoginHistoryDto> result = adminLoginStatusService.getLoginHistories(searchDto);

        // 페이지네이션 계산
        int totalPages = result.getTotalPages();
        int startPage = Math.max(1, page - 5);
        int endPage = Math.min(totalPages, page + 5);

        // 활성 사용자 수 조회
        int activeUsers = adminLoginStatusService.getActiveUserCount();

        model.addAttribute("loginHistories", result.getData());
        model.addAttribute("currentPage", page);
        model.addAttribute("totalPages", totalPages);
        model.addAttribute("startPage", startPage);
        model.addAttribute("endPage", endPage);
        model.addAttribute("activeUsers", activeUsers);

        return "admin/login-status";
    }
}