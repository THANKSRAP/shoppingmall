package com.example.shoppingmall.cart.controller;


import com.example.shoppingmall.cart.domain.CartDto;
import com.example.shoppingmall.cart.service.CartService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;


import javax.servlet.http.HttpSession;
import java.util.List;
import java.util.Map;


@Controller
@RequestMapping("/cart")
public class CartController {


    @Autowired
    private CartService cartService;


    // ✅ [1] HTML 장바구니 페이지
    @GetMapping
    public String cartPage(Model model) {
        int userId = 1; // 로그인 미적용 상태
        List<CartDto> cartList = cartService.getCartByUserId(userId);


        int totalCount = cartList.stream().mapToInt(CartDto::getQuantity).sum();
        model.addAttribute("cartList", cartList);
        model.addAttribute("cartItemCount", totalCount);

        return "cart/cart";
    }


    // ✅ [2] 수량 변경 API (PATCH)
    @PatchMapping("/item/{id}")
    @ResponseBody
    public ResponseEntity<String> updateQuantity(@PathVariable("id") int cartId,
                                                 @RequestBody Map<String, Object> payload) {
        System.out.println("✅ PATCH 요청 받음: cartId=" + cartId);
        int quantity = (int) payload.get("quantity");
        cartService.updateQuantity(cartId, quantity);
        return ResponseEntity.ok("업데이트 성공");
    }


    // ✅ [3] 선택 삭제 API (DELETE)
    @DeleteMapping("/items")
    @ResponseBody
    public ResponseEntity<Void> deleteSelectedItems(@RequestBody Map<String, List<Integer>> body) {
        List<Integer> cartItemIds = body.get("cartItemIds");
        cartService.deleteByCartIds(cartItemIds);
        return ResponseEntity.ok().build();
    }


    // ✅ [4] 전체 삭제 API (DELETE)
    @DeleteMapping("/all")
    @ResponseBody
    public ResponseEntity<Void> deleteAllItems(HttpSession session) {
        int userId = 1; // 로그인 미적용 상태
        cartService.deleteAllByUserId(userId);
        return ResponseEntity.ok().build();
    }


    @PostMapping("/order")
    public String handleOrder(@RequestBody Map<String, Object> orderData, HttpSession session) {
        System.out.println("🛒 받은 주문 데이터: " + orderData);
        session.setAttribute("orderData", orderData);
        return "redirect:/order/";
    }


    @PostMapping("/wishlist")
    @ResponseBody
    public ResponseEntity<String> addToWishlist(@RequestBody Map<String, Object> data) {

        System.out.println("💬 받은 데이터: " + data);
        System.out.println("✅ itemId: " + data.get("itemId"));
        System.out.println("✅ itemOptionId: " + data.get("itemOptionId"));

        try {
            int userId = Integer.parseInt(data.get("userId").toString());
            int itemId = Integer.parseInt(data.get("itemId").toString());
            int itemOptionId = Integer.parseInt(data.get("itemOptionId").toString());

            System.out.println("🔥 itemId: " + itemId);

            cartService.addToWishlist(userId, itemId, itemOptionId);
            return ResponseEntity.ok("success");
        } catch (Exception e) {
            e.printStackTrace(); // 콘솔에 전체 에러 출력
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("서버 오류 발생");
        }
    }


}
