package com.example.shoppingmall.itemquestion.controller;

import com.example.shoppingmall.itemquestion.domain.ItemQuestion;
import com.example.shoppingmall.itemquestion.service.ItemQuestionService;
import com.example.shoppingmall.common.dto.PageRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import java.util.List;

@Controller
@RequestMapping("/itemquestion")
@RequiredArgsConstructor
public class ItemQuestionController {

    private final ItemQuestionService itemQuestionService;

    // 공지사항 목록 조회 + 검색 + 페이징
    @GetMapping
    public String list(
            @RequestParam(value = "keyword", required = false) String keyword,
            @RequestParam(defaultValue = "1") int page,
            @RequestParam(defaultValue = "10") int size,
            Model model
    ) {
        List<ItemQuestion> questions;
        int totalCount;

        if (keyword != null && !keyword.trim().isEmpty()) {
            questions = itemQuestionService.search(keyword, page, size);
            totalCount = itemQuestionService.countSearch(keyword);
            model.addAttribute("keyword", keyword);
        } else {
            PageRequest pageRequest = new PageRequest(page, size);
            questions = itemQuestionService.getPage(pageRequest);
            totalCount = itemQuestionService.getTotalCount();
        }
        int totalPages = (int) Math.ceil((double) totalCount / size);

        model.addAttribute("itemQuestions", questions);
        model.addAttribute("currentPage", page);
        model.addAttribute("totalPages", totalPages);

        return "itemquestion/list";
    }


    //상세조회
    @GetMapping("/{id}")
    public String detailItemQuestion(Model model, @PathVariable("id") Long id, HttpSession session){
        ItemQuestion question = itemQuestionService.getItemQuestionById(id);
        Long userId = (Long)session.getAttribute("userId");

        // 비밀글 접근 권한 검사 - 본인이 작성한 글만 볼 수 있음
        if(question.isSecret()) {
            if(userId == null || !userId.equals(question.getUserId())) {
                model.addAttribute("errorMessage", "비밀글은 작성자만 볼 수 있습니다.");
                return "redirect:/itemquestion";
            }
        }

        model.addAttribute("itemQuestion", question);
        return "itemquestion/detail";
    }
    //작성 폼 이동
    @GetMapping("/new")
    public String createdItemQuestionForm(Model model, HttpSession session){
        Long userId = (Long)session.getAttribute("userId");
        if (userId == null) {
            return "redirect:/user/loginForm";
        }
        model.addAttribute("itemQuestion", new ItemQuestion());
        return "itemquestion/form";
    }

    //작성 처리
    @PostMapping("/new")
    public String createdItemQuestion(ItemQuestion itemQuestion, HttpSession session){
        Long userId = (Long)session.getAttribute("userId");
        itemQuestion.setUserId(userId);
        itemQuestionService.createItemQuestion(itemQuestion);
        return "redirect:/itemquestion";
    }
    //수정 폼 이동
    @GetMapping("/{id}/edit")
    public String updateItemQuestion(Model model,@PathVariable("id") Long id, HttpSession session){
        Long userId = (Long)session.getAttribute("userId");
        if (userId == null) {
            return "redirect:/user/loginForm";
        }

        ItemQuestion question = itemQuestionService.getItemQuestionById(id);
        model.addAttribute("itemQuestion", question);
        return "itemquestion/form";
    }
    //수정 처리
    @PostMapping("/{id}/edit")
    public String updateItemQuestion(@PathVariable("id") Long id, @ModelAttribute ItemQuestion itemQuestion){
        itemQuestion.setItemQuestionId(id);
        itemQuestionService.updateItemQuestion(itemQuestion);
        return "redirect:/itemquestion/" + id;
    }

    @PostMapping("/{id}/delete")
    public String deleteItemQuestion(@PathVariable("id") Long id, HttpServletRequest request, HttpSession session){
        Long userId = (Long)session.getAttribute("userId");
        if (userId == null) {
            return "redirect:/user/loginForm";
        }

        ItemQuestion itemQuestion = itemQuestionService.getItemQuestionById(id);
        if (!userId.equals(itemQuestion.getUserId())){
            String currentUri = request.getRequestURI();
            String redirectUri = currentUri.replaceAll("/delete$", "");
            return "redirect:" + redirectUri;
        }

        itemQuestionService.deleteItemQuestion(id);
        return "redirect:/itemquestion";
    }
}
