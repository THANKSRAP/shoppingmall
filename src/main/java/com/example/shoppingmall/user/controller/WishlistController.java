
package com.example.shoppingmall.user.controller;

import com.example.shoppingmall.common.dto.PageResult;
import com.example.shoppingmall.user.dao.UserDao;
import com.example.shoppingmall.user.domain.User;
import com.example.shoppingmall.user.domain.dto.WishlistDto;
import com.example.shoppingmall.user.domain.dto.WishlistSearchDto;
import com.example.shoppingmall.user.service.WishlistService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpSession;
import java.util.List;

@Controller
@RequestMapping("/wishlist")
@RequiredArgsConstructor
public class WishlistController {

    private final WishlistService wishlistService;
    private final UserDao userDao; // UserDao 추가

    @GetMapping
    public String wishlistPage(@RequestParam(defaultValue = "1") int page,
                               HttpSession session, Model model) {

        // 세션에서 이메일을 가져와서 User 정보를 조회
        String email = (String) session.getAttribute("id");
        if (email == null) {
            return "redirect:/user/loginForm";
        }

        // 이메일로 사용자 정보 조회
        User user = userDao.findByEmail(email);
        if (user == null) {
            return "redirect:/user/loginForm";
        }

        WishlistSearchDto searchDto = new WishlistSearchDto();
        searchDto.setUserId(user.getUser_id()); // User 객체에서 userId 가져오기
        searchDto.setPage(page);

        PageResult<WishlistDto> wishlistPage = wishlistService.getWishlistPage(searchDto);

        model.addAttribute("wishlistPage", wishlistPage);
        model.addAttribute("currentPage", page);

        return "user/wishlist";
    }

    @PostMapping("/delete")
    @ResponseBody
    public String deleteWishlistItems(@RequestParam("ids") List<Long> ids,
                                      HttpSession session) {
        String email = (String) session.getAttribute("id");
        if (email == null) {
            return "login_required";
        }

        User user = userDao.findByEmail(email);
        if (user == null) {
            return "login_required";
        }

        wishlistService.deleteWishlistItems(ids, user.getUser_id());
        return "success";
    }

    @PostMapping("/deleteAll")
    @ResponseBody
    public String deleteAllWishlist(HttpSession session) {
        String email = (String) session.getAttribute("id");
        if (email == null) {
            return "login_required";
        }

        User user = userDao.findByEmail(email);
        if (user == null) {
            return "login_required";
        }

        wishlistService.deleteAllWishlist(user.getUser_id());
        return "success";
    }
}