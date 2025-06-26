package com.example.shoppingmall.user.dao;

import com.example.shoppingmall.user.domain.dto.WishlistDto;
import com.example.shoppingmall.user.domain.dto.WishlistSearchDto;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

@Mapper
public interface WishlistDao {
    List<WishlistDto> selectWishlist(WishlistSearchDto searchDto);
    int countWishlist(Long userId);

    // 특정 상품+옵션 조합 삭제
    void deleteWishlistByItemAndOption(@Param("userId") Long userId,
                                       @Param("itemId") Long itemId,
                                       @Param("itemOptionId") Long itemOptionId);

    void deleteAllWishlist(Long userId);

    void insertWishlistItem(@Param("userId") Long userId,
                            @Param("itemId") Long itemId,
                            @Param("itemOptionId") Long itemOptionId);

    boolean existsWishlistItem(@Param("userId") Long userId,
                               @Param("itemId") Long itemId,
                               @Param("itemOptionId") Long itemOptionId);
}