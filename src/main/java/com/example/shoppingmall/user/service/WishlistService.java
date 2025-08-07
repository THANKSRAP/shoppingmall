package com.example.shoppingmall.user.service;

import com.example.shoppingmall.common.dto.PageResultDto;
import com.example.shoppingmall.user.dao.WishlistDao;
import com.example.shoppingmall.user.domain.dto.WishlistDto;
import com.example.shoppingmall.user.domain.dto.WishlistSearchDto;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class WishlistService {

    private final WishlistDao wishlistDao;

    public PageResultDto<WishlistDto> getWishlistPage(WishlistSearchDto searchDto) {
        List<WishlistDto> wishlist = wishlistDao.selectWishlist(searchDto);
        int totalCount = wishlistDao.countWishlist(searchDto.getUserId());

        return new PageResultDto<>(wishlist, totalCount, searchDto.getPageSize());
    }

    /**
     * 관심목록에 상품 추가
     */
    public void addToWishlist(Long userId, Long itemId) {
        wishlistDao.insertWishlistItem(userId, itemId, null);
    }

    /**
     * 관심목록에 이미 있는 상품인지 확인
     */
    public boolean isItemInWishlist(Long userId, Long itemId) {
        return wishlistDao.existsWishlistItem(userId, itemId, null);
    }

    /**
     * 특정 상품들을 삭제
     */
    public void deleteWishlistItems(List<WishlistDto> wishlistItems, Long userId) {
        for (WishlistDto item : wishlistItems) {
            wishlistDao.deleteWishlistByItemAndOption(userId, item.getItemId(), null);
        }
    }

    /**
     * 단일 상품 삭제
     */
    public void deleteWishlistByItem(Long userId, Long itemId) {
        wishlistDao.deleteWishlistByItemAndOption(userId, itemId, null);
    }

    public void deleteAllWishlist(Long userId) {
        wishlistDao.deleteAllWishlist(userId);
    }
}
