package com.example.shoppingmall.item.service;

import com.example.shoppingmall.item.domain.dto.ItemOptionDto;

import java.util.List;

public interface ItemOptionService {
    List<ItemOptionDto> getItemOptionsWithInventory(Long itemId);

    List<ItemOptionDto> getColorOptions(Long itemId);

    List<ItemOptionDto> getSizeOptions(Long itemId);
}
