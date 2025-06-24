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
    // ... 나머지 코드는 동일
    private final WishlistDao wishlistDao;

    public PageResult<WishlistDto> getWishlistPage(WishlistSearchDto searchDto) {
        List<WishlistDto> wishlist = wishlistDao.selectWishlist(searchDto);
        int totalCount = wishlistDao.countWishlist(searchDto.getUserId());

        return new PageResult<>(wishlist, totalCount, searchDto.getPageSize());
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