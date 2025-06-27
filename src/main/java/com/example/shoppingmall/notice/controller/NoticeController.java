package com.example.shoppingmall.notice.controller;

import com.example.shoppingmall.notice.domain.Notice;
import com.example.shoppingmall.notice.domain.dto.PageRequest;
import com.example.shoppingmall.notice.service.NoticeService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpSession;
import java.time.LocalDateTime;
import java.util.List;

@Controller
@RequestMapping("/notice")
@RequiredArgsConstructor
public class NoticeController {
    private final NoticeService noticeService;

    // 공지사항 목록 조회
    // 페이징 목록 조회
    @GetMapping
    public String ListNotices(
            @RequestParam(defaultValue = "1") int page,
            @RequestParam(defaultValue = "10") int size,
            Model model
    ) {
        PageRequest pageRequest = new PageRequest(page, size);

        List<Notice> notices = noticeService.getPage(pageRequest);
        int totalCount = noticeService.getTotalCount();
        int totalPages = (int) Math.ceil((double)totalCount / size);

        model.addAttribute("notices", notices);
        model.addAttribute("currentPage", page);
        model.addAttribute("totalPages", totalPages);

        return "notice/list";
    }
    //공지사항 상세 조회
    @GetMapping("/{id}")
    public String detailNotice(Model model, @PathVariable Long id){
        Notice notice = noticeService.getNoticeById(id);

        model.addAttribute("notice", notice);
        noticeService.increaseViewCount(id);

        return "notice/detail";
    }
    //공지사항 등록으로 이동
    @GetMapping("/new")
    public String createNotice(Model model, HttpSession session){
        Long userId = (Long)session.getAttribute("userId");
        if (userId == null) {
            return "redirect:/user/loginForm";
        }
        model.addAttribute("notice", new Notice());
        return "notice/form";
    }
    //공지사항 등록
    @PostMapping("/new")
    public String createdNotice(Notice notice){
        notice.setAdminId(1L);
        notice.setViewCount(0);
        notice.setStatus("ACTIVE");
        notice.setisPinned(false);
        notice.setCreatedAt(LocalDateTime.now());
        notice.setUpdatedAt(LocalDateTime.now());

        noticeService.createdNotice(notice);
        return "redirect:/notice";
    }
    //공지사항 수정으로 이동
    @GetMapping("/{id}/edit")
    public String updateNotice(Model model,@PathVariable Long id){
        Notice notice = noticeService.getNoticeById(id);

        model.addAttribute("notice", notice);
        return "notice/form";
    }
    // 공지사항 수정 처리
    @PostMapping("/{id}/edit")
    public String updateNotice(Notice notice,@PathVariable Long id, Model model){
        notice.setNoticeId(id);
        noticeService.updateNotice(notice);
        return "redirect:/notice/" + id;
    }
    // 공지사항 삭제 처리
    @PostMapping("/{id}/delete")
    public String deleteNotice(@PathVariable Long id) {
        noticeService.deleteNotice(id);
        return "redirect:/notice";
    }
}
