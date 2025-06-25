package com.example.shoppingmall.item.service;

import com.example.shoppingmall.item.domain.dto.ItemDto;

import java.util.List;

public interface ItemService {
    List<ItemDto> getAllItems();

    ItemDto getItemById(Long id);

    void createItem(ItemDto itemDto);

    void updateItem(ItemDto itemDto);

    void deleteItemById(Long id);

    List<ItemDto> getBestSellers();

    List<ItemDto> getNewItems();

    List<ItemDto> searchItemsByName(String name);

    List<ItemDto> getItemsByCategory(Long majorId, Long middleId, Long minorId);

    ItemDto getItemWithReviewSummary(Long itemId);
}