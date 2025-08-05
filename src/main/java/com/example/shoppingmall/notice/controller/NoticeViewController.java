package com.example.shoppingmall.notice.controller;

import java.time.LocalDateTime;
import java.util.List;

import javax.servlet.http.HttpSession;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.example.shoppingmall.common.dto.PageRequest;
import com.example.shoppingmall.notice.domain.Notice;
import com.example.shoppingmall.notice.service.NoticeService;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Controller
@RequestMapping("/notice")
@RequiredArgsConstructor
@Slf4j
public class NoticeViewController {
    
    private final NoticeService noticeService;

    /**
     * 공지사항 목록 조회 (페이징)
     */
    @GetMapping
    public String listNotices(
            @RequestParam(defaultValue = "1") int page,
            @RequestParam(defaultValue = "10") int size,
            Model model
    ) {
        try {
            PageRequest pageRequest = PageRequest.of(page, size);
            List<Notice> notices = noticeService.getPage(pageRequest);
            int totalCount = noticeService.getTotalCount();
            int totalPages = (int) Math.ceil((double) totalCount / size);

            model.addAttribute("notices", notices);
            model.addAttribute("currentPage", page);
            model.addAttribute("totalPages", totalPages);
            model.addAttribute("totalCount", totalCount);

            log.info("공지사항 목록 조회 완료: 페이지={}, 크기={}, 총 개수={}", page, size, totalCount);
            return "notice/list";
            
        } catch (Exception e) {
            log.error("공지사항 목록 조회 실패: 페이지={}, 크기={}", page, size, e);
            model.addAttribute("error", "공지사항 목록을 불러오는 중 오류가 발생했습니다.");
            return "notice/list";
        }
    }

    /**
     * 공지사항 상세 조회
     */
    @GetMapping("/{id}")
    public String detailNotice(@PathVariable Long id, Model model) {
        try {
            Notice notice = noticeService.getNoticeById(id);
            noticeService.increaseViewCount(id);
            
            model.addAttribute("notice", notice);
            log.info("공지사항 상세 조회: ID={}", id);
            return "notice/detail";
            
        } catch (Exception e) {
            log.error("공지사항 상세 조회 실패: ID={}", id, e);
            model.addAttribute("error", "공지사항을 찾을 수 없습니다.");
            return "redirect:/notice";
        }
    }

    /**
     * 공지사항 등록 폼
     */
    @GetMapping("/new")
    public String createNoticeForm(HttpSession session, Model model) {
        try {
            Long userId = (Long) session.getAttribute("userId");
            if (userId == null) {
                log.warn("로그인하지 않은 사용자가 공지사항 등록 시도");
                return "redirect:/loginForm";
            }
            
            model.addAttribute("notice", new Notice());
            return "notice/form";
            
        } catch (Exception e) {
            log.error("공지사항 등록 폼 로드 실패", e);
            return "redirect:/notice";
        }
    }

    /**
     * 공지사항 등록 처리
     */
    @PostMapping("/new")
    public String createNotice(@ModelAttribute Notice notice, HttpSession session) {
        try {
            Long userId = (Long) session.getAttribute("userId");
            if (userId == null) {
                log.warn("로그인하지 않은 사용자가 공지사항 등록 시도");
                return "redirect:/loginForm";
            }

            notice.setAdminId(userId);
            notice.setViewCount(0);
            notice.setStatus("ACTIVE");
            notice.setisPinned(false);
            notice.setCreatedAt(LocalDateTime.now());
            notice.setUpdatedAt(LocalDateTime.now());

            noticeService.createNotice(notice);
            log.info("공지사항 등록 완료: 제목={}, 작성자={}", notice.getTitle(), userId);
            return "redirect:/notice";
            
        } catch (Exception e) {
            log.error("공지사항 등록 실패", e);
            return "redirect:/notice/new";
        }
    }

    /**
     * 공지사항 수정 폼
     */
    @GetMapping("/{id}/edit")
    public String updateNoticeForm(@PathVariable Long id, Model model) {
        try {
            Notice notice = noticeService.getNoticeById(id);
            model.addAttribute("notice", notice);
            return "notice/form";
            
        } catch (Exception e) {
            log.error("공지사항 수정 폼 로드 실패: ID={}", id, e);
            return "redirect:/notice";
        }
    }

    /**
     * 공지사항 수정 처리
     */
    @PostMapping("/{id}/edit")
    public String updateNotice(@PathVariable Long id, @ModelAttribute Notice notice) {
        try {
            notice.setNoticeId(id);
            notice.setUpdatedAt(LocalDateTime.now());
            
            noticeService.updateNotice(notice);
            log.info("공지사항 수정 완료: ID={}", id);
            return "redirect:/notice/" + id;
            
        } catch (Exception e) {
            log.error("공지사항 수정 실패: ID={}", id, e);
            return "redirect:/notice/" + id + "/edit";
        }
    }

    /**
     * 공지사항 삭제 처리
     */
    @PostMapping("/{id}/delete")
    public String deleteNotice(@PathVariable Long id) {
        try {
            noticeService.deleteNotice(id);
            log.info("공지사항 삭제 완료: ID={}", id);
            return "redirect:/notice";
            
        } catch (Exception e) {
            log.error("공지사항 삭제 실패: ID={}", id, e);
            return "redirect:/notice/" + id;
        }
    }
} 