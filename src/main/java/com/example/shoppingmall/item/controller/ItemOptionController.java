package com.example.shoppingmall.item.controller;

import com.example.shoppingmall.item.domain.dto.ItemOptionDto;
import com.example.shoppingmall.item.service.ItemOptionService;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/item-option")
public class ItemOptionController {

    private final ItemOptionService itemOptionService;

    public ItemOptionController(ItemOptionService itemOptionService) {
        this.itemOptionService = itemOptionService;
    }


    // 특정 상품의 옵션들 조회
    @GetMapping("/{itemId}")
    public List<ItemOptionDto> getItemOptions(@PathVariable Long itemId) {
        return itemOptionService.getItemOptionsWithInventory(itemId);
    }

    // 특정 상품의 색생 옵션 조회
    @GetMapping("/colors/{itemId}")
    public List<ItemOptionDto> getColorOptions(@PathVariable Long itemId) {
        return itemOptionService.getColorOptions(itemId);
    }

    // 특정 상품의 사이즈 옵션 조회
    @GetMapping("/sizes/{itemId}")
    public List<ItemOptionDto> getSizeOptions(@PathVariable Long itemId) {
        return itemOptionService.getSizeOptions(itemId);
    }
}