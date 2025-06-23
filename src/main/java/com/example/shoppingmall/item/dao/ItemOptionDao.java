package com.example.shoppingmall.item.dao;

import com.example.shoppingmall.item.domain.dto.ItemOptionDto;
import java.util.List;

public interface ItemOptionDao {
    // 특정 상품의 옵션들 조회
    List<ItemOptionDto> findItemOptionsWithInventory(Long itemId);

    // 특정 상품의 색상 옵션만 조회
    List<ItemOptionDto> findColorOptions(Long itemId);

    // 특정 상품의 사이즈 옵션만 조회
    List<ItemOptionDto> findSizeOptions(Long itemId);
}