package com.example.shoppingmall.cart.dao;

import com.example.shoppingmall.cart.domain.CartDto;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

@Mapper
public interface CartDao {

    //해당 유저의 장바구니 목록 반환
    List<CartDto> selectCartByUserId(@Param("userId")int userId) throws Exception;
    //해당 유저의 장바구니에 해당 물품이 있는지 확인
    CartDto selectByUserIdAndItemId(@Param("userId")int userId, @Param("itemId")int itemId) throws Exception;
    //장바구니에 상품 추가
    int insert(CartDto cart) throws Exception;
    //기존 상품의 수량&금액 수정
    int updateItemQuantity(CartDto cart) throws Exception;
    //특정 장바구니 항목 제거
    int deleteByCartId(int cartId) throws Exception;
    //여러 항목 일괄 삭제
    void deleteByCartIds(List<Integer> cartIds) throws Exception;
    //장바구니 전체 비우기
    int deleteAllByUserId(int userId) throws Exception;
    //해당 유저의 장바구니 상품 수 집계
    int countByUserId(int userId) throws Exception;
    // ✅ 새로 추가한 메서드 (수량만 바꾸는)
    void updateQuantity(@Param("cartId") int cartId, @Param("quantity") int quantity) throws Exception;


}
