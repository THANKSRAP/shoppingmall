package com.example.shoppingmall.cart.dao;

import com.example.shoppingmall.cart.domain.CartDto;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;


import java.util.List;


@Mapper
public interface CartDao {


    //해당 유저의 장바구니 목록 반환
    List<CartDto> selectCartByUserId(@Param("userId")Long userId) throws Exception;
    //해당 유저의 장바구니에 해당 물품이 있는지 확인
    //CartDto selectByUserIdAndItemId(@Param("userId")int userId, @Param("itemId")int itemId) throws Exception;
    //장바구니에 상품 추가
    int insert(CartDto cart) throws Exception;
    //기존 상품의 수량&금액 수정
    int updateItemQuantity(CartDto cart) throws Exception;
    //단일 항목 삭제
    int deleteByCartId(Long cartId) throws Exception;
    //여러 항목 일괄 삭제
    void deleteByCartIds(List<Long> cartIds) throws Exception;
    //전체 삭제 (유저 기준)
    int deleteAllByUserId(Long userId) throws Exception;
    //해당 유저의 장바구니 상품 수 집계
    int countByUserId(Long userId) throws Exception;
    // ✅ 새로 추가한 메서드 (수량만 바꾸는)
    void updateQuantity(@Param("cartId") Long cartId, @Param("quantity") int quantity) throws Exception;
    //관심상품에 추가
    void addToWishlist(@Param("userId") Long userId, @Param("itemId") Long itemId) throws Exception;
    //장바구니 추가
    void insertCart(CartDto cartDto);
    CartDto selectExistingCartItem(CartDto cartDto);

    List<CartDto> selectCartsByIds(@Param("list") List<Long> cartIds);
}
