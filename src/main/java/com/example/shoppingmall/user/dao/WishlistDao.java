
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
    void deleteWishlistItem(@Param("id") Long id, @Param("userId") Long userId);
    void deleteAllWishlist(Long userId);

    // 새로 추가
    void insertWishlistItem(@Param("userId") Long userId, @Param("itemId") Long itemId);
    boolean existsWishlistItem(@Param("userId") Long userId, @Param("itemId") Long itemId);
}