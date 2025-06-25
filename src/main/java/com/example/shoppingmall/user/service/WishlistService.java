package com.example.shoppingmall.user.service;

import com.example.shoppingmall.common.dto.PageResult;
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

    public PageResult<WishlistDto> getWishlistPage(WishlistSearchDto searchDto) {
        List<WishlistDto> wishlist = wishlistDao.selectWishlist(searchDto);
        int totalCount = wishlistDao.countWishlist(searchDto.getUserId());

        return new PageResult<>(wishlist, totalCount, searchDto.getPageSize());
    }

    /**
     * 관심목록에 상품 추가
     */
    public void addToWishlist(Long userId, Long itemId) {
        wishlistDao.insertWishlistItem(userId, itemId);
    }

    /**
     * 관심목록에 이미 있는 상품인지 확인
     */
    public boolean isItemInWishlist(Long userId, Long itemId) {
        return wishlistDao.existsWishlistItem(userId, itemId);
    }

    public void deleteWishlistItems(List<Long> ids, Long userId) {
        for (Long id : ids) {
            wishlistDao.deleteWishlistItem(id, userId);
        }
    }

    public void deleteAllWishlist(Long userId) {
        wishlistDao.deleteAllWishlist(userId);
    }
}