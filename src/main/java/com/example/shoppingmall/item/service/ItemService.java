package com.example.shoppingmall.item.service;

import com.example.shoppingmall.item.dao.ItemDao;
import com.example.shoppingmall.item.domain.Item;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ItemService {

    private final ItemDao itemDao;

    public ItemService(ItemDao itemDao) {
        this.itemDao = itemDao;
    }

    public List<Item> getAllItems() {
        return itemDao.findAll();
    }

    public Item getItemById(Long id) {
        return itemDao.findById(id);
    }

    public void createItem(Item item) {
        itemDao.insert(item);
    }

    public void updateItem(Item item) {
        itemDao.update(item);
    }

    public void deleteItem(Long id) {
        itemDao.delete(id);
    }
}
