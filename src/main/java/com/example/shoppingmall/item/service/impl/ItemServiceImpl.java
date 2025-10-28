package com.example.shoppingmall.item.service.impl;

import com.example.shoppingmall.item.dao.ItemDao;
import com.example.shoppingmall.item.domain.Item;
import com.example.shoppingmall.item.domain.dto.ItemDto;
import com.example.shoppingmall.item.service.ItemService;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class ItemServiceImpl implements ItemService {

    private final ItemDao itemDao;

    public ItemServiceImpl(ItemDao itemDao) {
        this.itemDao = itemDao;
    }

    public Item toEntity(ItemDto itemDto) {
        Item item = new Item();
        item.setItemId(itemDto.getItemId()); // Auto Incresement 되는 값
        item.setCategoryId(itemDto.getCategoryId());
        item.setName(itemDto.getName());
        item.setImage(itemDto.getImage());
        item.setAverageRating(itemDto.getAverageRating());
        item.setReviewCount(itemDto.getReviewCount());
        item.setImage(itemDto.getImage());
        item.setDescription(itemDto.getDescription());
        item.setPrice(itemDto.getPrice());
        item.setStatus(itemDto.getStatus());
        item.setGrade(itemDto.getGrade());
        item.setManufactureCountry(itemDto.getManufactureCountry());
        item.setIsBestSeller(itemDto.getIsBestSeller());
        // item.setCreatedAt(itemDto.getCreatedAt());
        // item.setUpdatedAt(itemDto.getUpdatedAt());
        return item;
    }

    public ItemDto toDto(Item item) {
        ItemDto itemDto = new ItemDto();
        itemDto.setItemId(item.getItemId()); // Auto Incresement 되는 값
        itemDto.setCategoryId(item.getCategoryId());
        itemDto.setName(item.getName());
        itemDto.setImage(item.getImage());
        itemDto.setAverageRating(item.getAverageRating());
        itemDto.setReviewCount(item.getReviewCount());
        itemDto.setDescription(item.getDescription());
        itemDto.setPrice(item.getPrice());
        itemDto.setStatus(item.getStatus());
        itemDto.setGrade(item.getGrade());
        itemDto.setManufactureCountry(item.getManufactureCountry());
        itemDto.setIsBestSeller(item.getIsBestSeller());
        // itemDto.setCreatedAt(item.getCreatedAt());
        // itemDto.setUpdatedAt(item.getUpdatedAt());
        return itemDto;
    }

    @Override
    public List<ItemDto> getAllItems() {
        return itemDao.findAll().stream()
                .map(this::toDto)
                .collect(Collectors.toList());
    }

    @Override
    public ItemDto getItemById(Long id) {
        Item item = itemDao.findById(id);
        if (item == null) {
            throw new IllegalArgumentException("해당 ID의 상품을 찾을 수 없습니다: " + id);
        }
        return this.toDto(item);
    }

    @Override
    public void createItem(ItemDto itemDto) {
        Item item = this.toEntity(itemDto);
        itemDao.insert(item);
    }

    @Override
    public void updateItem(ItemDto itemDto) {
        Item item = this.toEntity(itemDto);
        itemDao.update(item);
    }

    @Override
    public void deleteItemById(Long id) {
        itemDao.deleteById(id);
    }

    @Override
    public List<ItemDto> getBestSellers() {
        return itemDao.findBestSellers().stream()
                .map(this::toDto)
                .collect(Collectors.toList());
    }

    @Override
    public List<ItemDto> getNewItems() {
        return itemDao.findNewItems().stream()
                .map(this::toDto)
                .collect(Collectors.toList());
    }

    @Override
    public List<ItemDto> searchItemsByName(String name) {
        return itemDao.searchItemsByName(name).stream()
                .map(this::toDto)
                .collect(Collectors.toList());
    }

    @Override
    public List<ItemDto> getItemsByCategory(Long majorId, Long middleId, Long minorId) {
        List<Item> items = itemDao.findItemsByCategory(majorId, middleId, minorId);
        return items.stream().map(this::toDto).collect(Collectors.toList());
    }

    @Override
    public ItemDto getItemWithReviewSummary(Long itemId) {
        return itemDao.selectItemWithReviewSummary(itemId);
    }

    @Override
    public List<ItemDto> getBestSellersWithReviewSummary() {
        return itemDao.findBestSellersWithReviewSummary();
    }

    @Override
    public List<ItemDto> getNewItemsWithReviewSummary() {
        return itemDao.findNewItemsWithReviewSummary();
    }


}
