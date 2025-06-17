package com.example.shoppingmall.item.dao;


import com.example.shoppingmall.item.domain.Item;

import java.util.List;

public interface ItemDao {
    List<Item> findAll();
    Item findById(Long id);
    void insert(Item item);
    void update(Item item);
    void delete(Long id);
}