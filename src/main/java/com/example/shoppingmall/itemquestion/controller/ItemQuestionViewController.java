package com.example.shoppingmall.itemquestion.controller;

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
import com.example.shoppingmall.itemquestion.domain.ItemQuestion;
import com.example.shoppingmall.itemquestion.service.ItemQuestionService;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Controller
@RequestMapping("/itemquestion")
@RequiredArgsConstructor
@Slf4j
public class ItemQuestionViewController {

    private final ItemQuestionService itemQuestionService;

    /**
     * 상품 문의 목록 조회 (페이징 + 검색)
     */
    @GetMapping
    public String list(
            @RequestParam(value = "keyword", required = false) String keyword,
            @RequestParam(defaultValue = "1") int page,
            @RequestParam(defaultValue = "10") int size,
            Model model
    ) {
        try {
            List<ItemQuestion> questions;
            int totalCount;

            if (keyword != null && !keyword.trim().isEmpty()) {
                questions = itemQuestionService.search(keyword, page, size);
                totalCount = itemQuestionService.countSearch(keyword);
                model.addAttribute("keyword", keyword);
                log.info("상품 문의 검색 완료: 키워드={}, 페이지={}, 결과={}개", keyword, page, totalCount);
            } else {
                PageRequest pageRequest = PageRequest.of(page, size);
                questions = itemQuestionService.getPage(pageRequest);
                totalCount = itemQuestionService.getTotalCount();
                log.info("상품 문의 목록 조회 완료: 페이지={}, 결과={}개", page, totalCount);
            }

            int totalPages = (int) Math.ceil((double) totalCount / size);

            model.addAttribute("itemQuestions", questions);
            model.addAttribute("currentPage", page);
            model.addAttribute("totalPages", totalPages);
            model.addAttribute("totalCount", totalCount);

            return "itemquestion/list";
            
        } catch (Exception e) {
            log.error("상품 문의 목록 조회 실패: 페이지={}, 키워드={}", page, keyword, e);
            model.addAttribute("error", "상품 문의 목록을 불러오는 중 오류가 발생했습니다.");
            return "itemquestion/list";
        }
    }

    /**
     * 상품 문의 상세 조회
     */
    @GetMapping("/{id}")
    public String detailItemQuestion(@PathVariable Long id, Model model) {
        try {
            ItemQuestion question = itemQuestionService.getQuestionById(id);
            model.addAttribute("question", question);
            log.info("상품 문의 상세 조회: ID={}", id);
            return "itemquestion/detail";
            
        } catch (Exception e) {
            log.error("상품 문의 상세 조회 실패: ID={}", id, e);
            model.addAttribute("error", "상품 문의를 찾을 수 없습니다.");
            return "redirect:/itemquestion";
        }
    }

    /**
     * 상품 문의 작성 폼
     */
    @GetMapping("/new")
    public String createQuestionForm(HttpSession session, Model model) {
        try {
            Long userId = (Long) session.getAttribute("userId");
            if (userId == null) {
                log.warn("로그인하지 않은 사용자가 상품 문의 작성 시도");
                return "redirect:/loginForm";
            }
            
            model.addAttribute("question", new ItemQuestion());
            return "itemquestion/form";
            
        } catch (Exception e) {
            log.error("상품 문의 작성 폼 로드 실패", e);
            return "redirect:/itemquestion";
        }
    }

    /**
     * 상품 문의 작성 처리
     */
    @PostMapping("/new")
    public String createQuestion(@ModelAttribute ItemQuestion question, HttpSession session) {
        try {
            Long userId = (Long) session.getAttribute("userId");
            if (userId == null) {
                log.warn("로그인하지 않은 사용자가 상품 문의 작성 시도");
                return "redirect:/loginForm";
            }

            question.setUserId(userId);
            itemQuestionService.createQuestion(question);
            log.info("상품 문의 작성 완료: 제목={}, 작성자={}", question.getTitle(), userId);
            return "redirect:/itemquestion";
            
        } catch (Exception e) {
            log.error("상품 문의 작성 실패", e);
            return "redirect:/itemquestion/new";
        }
    }

    /**
     * 상품 문의 수정 폼
     */
    @GetMapping("/{id}/edit")
    public String updateQuestionForm(@PathVariable Long id, Model model, HttpSession session) {
        try {
            Long userId = (Long) session.getAttribute("userId");
            if (userId == null) {
                log.warn("로그인하지 않은 사용자가 상품 문의 수정 시도");
                return "redirect:/loginForm";
            }

            ItemQuestion question = itemQuestionService.getQuestionById(id);
            
            // 작성자 본인만 수정 가능
            if (!question.getUserId().equals(userId)) {
                log.warn("권한 없는 사용자가 상품 문의 수정 시도: 요청자={}, 작성자={}", userId, question.getUserId());
                return "redirect:/itemquestion/" + id;
            }
            
            model.addAttribute("question", question);
            return "itemquestion/form";
            
        } catch (Exception e) {
            log.error("상품 문의 수정 폼 로드 실패: ID={}", id, e);
            return "redirect:/itemquestion";
        }
    }

    /**
     * 상품 문의 수정 처리
     */
    @PostMapping("/{id}/edit")
    public String updateQuestion(@PathVariable Long id, @ModelAttribute ItemQuestion question, HttpSession session) {
        try {
            Long userId = (Long) session.getAttribute("userId");
            if (userId == null) {
                log.warn("로그인하지 않은 사용자가 상품 문의 수정 시도");
                return "redirect:/loginForm";
            }

            question.setItemQuestionId(id);
            question.setUserId(userId);
            
            itemQuestionService.updateQuestion(question);
            log.info("상품 문의 수정 완료: ID={}", id);
            return "redirect:/itemquestion/" + id;
            
        } catch (Exception e) {
            log.error("상품 문의 수정 실패: ID={}", id, e);
            return "redirect:/itemquestion/" + id + "/edit";
        }
    }

    /**
     * 상품 문의 삭제 처리
     */
    @PostMapping("/{id}/delete")
    public String deleteQuestion(@PathVariable Long id, HttpSession session) {
        try {
            Long userId = (Long) session.getAttribute("userId");
            if (userId == null) {
                log.warn("로그인하지 않은 사용자가 상품 문의 삭제 시도");
                return "redirect:/loginForm";
            }

            ItemQuestion question = itemQuestionService.getQuestionById(id);
            
            // 작성자 본인만 삭제 가능
            if (!question.getUserId().equals(userId)) {
                log.warn("권한 없는 사용자가 상품 문의 삭제 시도: 요청자={}, 작성자={}", userId, question.getUserId());
                return "redirect:/itemquestion/" + id;
            }
            
            itemQuestionService.deleteQuestion(id);
            log.info("상품 문의 삭제 완료: ID={}", id);
            return "redirect:/itemquestion";
            
        } catch (Exception e) {
            log.error("상품 문의 삭제 실패: ID={}", id, e);
            return "redirect:/itemquestion/" + id;
        }
    }
} 