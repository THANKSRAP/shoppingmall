package com.example.shoppingmall.item.dao;


import com.example.shoppingmall.item.domain.Item;
import com.example.shoppingmall.item.domain.dto.ItemDto;
import org.apache.ibatis.annotations.Param;

import java.util.List;

public interface ItemDao {
    List<Item> findAll();

    Item findById(Long id);

    void insert(Item item);

    void update(Item item);

    void deleteById(Long id);

    List<Item> findBestSellers();

    List<Item> findNewItems();

    List<Item> searchItemsByName(String name);

    List<Item> findItemsByCategory(@Param("majorId") Long majorId,
                                   @Param("middleId") Long middleId,
                                   @Param("minorId") Long minorId);

    ItemDto selectItemWithReviewSummary(Long itemId);

}