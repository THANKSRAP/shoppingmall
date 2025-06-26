package com.example.shoppingmall.cart.controller;


import com.example.shoppingmall.cart.domain.CartDto;
import com.example.shoppingmall.cart.service.CartService;
import com.example.shoppingmall.item.dao.ItemDao;
import com.example.shoppingmall.item.domain.Item;
import com.example.shoppingmall.order.domain.request.OrderPageRequestDto;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;


import javax.servlet.http.HttpSession;
import java.math.BigDecimal;
import java.util.Collections;
import java.util.List;
import java.util.Map;


@Controller
@RequestMapping("/cart")
public class CartController {


    @Autowired
    private CartService cartService;

    @Autowired
    private ItemDao itemDao;

    private final ObjectMapper objectMapper = new ObjectMapper();

    // ✅ [1] HTML 장바구니 페이지
    @GetMapping
    public String cartPage(Model model, HttpSession session) {
        Long userId = (Long)session.getAttribute("userId");
        if (userId == null) {
            return "redirect:user/loginForm";
        }

        List<CartDto> cartList = cartService.getCartByUserId(userId);


        int totalCount = cartList.stream().mapToInt(CartDto::getQuantity).sum();
        model.addAttribute("cartList", cartList);
        model.addAttribute("cartItemCount", totalCount);

        return "cart/cart";
    }


    // ✅ [2] 수량 변경 API (PATCH)
    @PatchMapping("/item/{id}")
    @ResponseBody
    public ResponseEntity<String> updateQuantity(@PathVariable("id") Long cartId,
                                                 @RequestBody Map<String, Object> payload) {
        System.out.println("✅ PATCH 요청 받음: cartId=" + cartId);
        int quantity = (int) payload.get("quantity");
        cartService.updateQuantity(cartId, quantity);
        return ResponseEntity.ok("업데이트 성공");
    }


    // ✅ [3] 선택 삭제 API (DELETE)
    @DeleteMapping("/items")
    @ResponseBody
    public ResponseEntity<Void> deleteSelectedItems(@RequestBody Map<String, List<Long>> body) {
        List<Long> cartItemIds = body.get("cartItemIds");
        cartService.deleteByCartIds(cartItemIds);
        return ResponseEntity.ok().build();
    }


    // ✅ [4] 전체 삭제 API (DELETE)
    @DeleteMapping("/all")
    @ResponseBody
    public ResponseEntity<Void> deleteAllItems(HttpSession session) {
        Long userId = (Long)session.getAttribute("userId");
        cartService.deleteAllByUserId(userId);
        return ResponseEntity.ok().build();
    }

    @DeleteMapping("/item/{cartId}")
    @ResponseBody
    public ResponseEntity<Void> deleteCartItem(@PathVariable("cartId") Long cartId) {
        cartService.deleteByCartId(cartId); // 서비스 계층에서 단일 삭제 메서드 호출
        return ResponseEntity.ok().build();
    }


    @PostMapping("/order")
    public String handleOrder(@RequestBody Map<String, Object> orderData, HttpSession session) {
        session.setAttribute("orderData", orderData);
        return "redirect:/order/";
    }


    @PostMapping("/wishlist")
    @ResponseBody
    public ResponseEntity<String> addToWishlist(@RequestBody Map<String, Object> data, HttpSession session) {

        System.out.println("💬 받은 데이터: " + data);
        System.out.println("✅ itemId: " + data.get("itemId"));

        try {
            Long userId = (Long)session.getAttribute("userId");
            Long itemId = Long.parseLong(data.get("itemId").toString());
            System.out.println("🔥 itemId: " + itemId);

            cartService.addToWishlist(userId, itemId);
            return ResponseEntity.ok("success");
        } catch (Exception e) {
            e.printStackTrace(); // 콘솔에 전체 에러 출력
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("서버 오류 발생");
        }
    }

    @PostMapping("/add")
    public String addToCart(@RequestParam("itemId") Long itemId,
                            @RequestParam("itemOptionId") Long itemOptionId,
                            @RequestParam("quantity") int quantity,
                            HttpSession session) {

        Long userId = (Long)session.getAttribute("userId");

        CartDto cartDto = new CartDto();
        cartDto.setUserId(userId);
        cartDto.setItemId(itemId);
        cartDto.setItemOptionId(itemOptionId);
        cartDto.setQuantity(quantity);

        cartService.insertCart(cartDto);

        return "redirect:/cart";
    }

    @PostMapping("/order/create")
    public String createOrderFromItem(@RequestParam("itemId") Long itemId,
                                      @RequestParam("itemOptionId") Long itemOptionId,
                                      @RequestParam("quantity") int quantity,
                                      HttpSession session) {
        try {
            Long userId = (Long)session.getAttribute("userId");

            Item item = itemDao.findById(itemId);
            System.out.println("itemId: "+item.getItemId());

            CartDto cartDto = new CartDto();
            cartDto.setUserId(userId);
            cartDto.setItemId(itemId);
            cartDto.setItemOptionId(itemOptionId);
            cartDto.setQuantity(quantity);

            Long cartId = cartService.insertCart(cartDto);
            System.out.println(cartDto);
            OrderPageRequestDto.CartItem cartItem = new OrderPageRequestDto.CartItem();
            cartItem.setCartId(cartId);
            cartItem.setPrice(item.getPrice().multiply(new BigDecimal(cartDto.getQuantity()))); // 가격 * 수량 계산

            OrderPageRequestDto orderPageRequestDto = new OrderPageRequestDto();
            orderPageRequestDto.setItemsPrice(cartItem.getPrice());
            orderPageRequestDto.setDeliveryFee(orderPageRequestDto.getItemsPrice().compareTo(new BigDecimal(100000)) >= 0 ? BigDecimal.ZERO : new BigDecimal(3000)); // 배송비 계산

            orderPageRequestDto.setCarts(Collections.singletonList(cartItem));
            System.out.println(cartDto);
            String json = objectMapper.writeValueAsString(orderPageRequestDto);
            session.setAttribute("orderData", json);

            return "redirect:/order";

        } catch (Exception e) {
            e.printStackTrace();
            return "error";
        }
    }
}
