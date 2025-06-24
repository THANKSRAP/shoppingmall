package com.example.shoppingmall.item.service.impl;

import com.example.shoppingmall.item.dao.ItemOptionDao;
import com.example.shoppingmall.item.domain.dto.ItemOptionDto;
import com.example.shoppingmall.item.service.ItemOptionService;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ItemOptionServiceImpl implements ItemOptionService {

    private final ItemOptionDao itemOptionDao;

    public ItemOptionServiceImpl(ItemOptionDao itemOptionDao) {
        this.itemOptionDao = itemOptionDao;
    }

    @Override
    public List<ItemOptionDto> getItemOptionsWithInventory(Long itemId) {
        return itemOptionDao.findItemOptionsWithInventory(itemId);
    }

    @Override
    public List<ItemOptionDto> getColorOptions(Long itemId) {
        return itemOptionDao.findColorOptions(itemId);
    }

    @Override
    public List<ItemOptionDto> getSizeOptions(Long itemId) {
        return itemOptionDao.findSizeOptions(itemId);
    }
}
