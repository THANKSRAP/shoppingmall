package com.example.shoppingmall.item.controller;

import com.example.shoppingmall.item.domain.dto.ItemDto;
import com.example.shoppingmall.item.service.ItemService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/item")
public class ItemController {

    private final ItemService itemService;

    public ItemController(ItemService itemService) {
        this.itemService = itemService;
    }

    // 카테고리별 상품 조회
    @GetMapping
    public ResponseEntity<List<ItemDto>> getItemsByCategory(
            @RequestParam(required = false) Long majorId,
            @RequestParam(required = false) Long middleId,
            @RequestParam(required = false) Long minorId
    ) {
        List<ItemDto> items = (majorId == null && middleId == null && minorId == null)
                ? itemService.getAllItems()
                : itemService.getItemsByCategory(majorId, middleId, minorId);
        return ResponseEntity.ok(items);
    }

    // 모든 상품 조회
    @GetMapping("/all")
    public List<ItemDto> getAllItems() {
        return itemService.getAllItems();
    }

    // 상품 이름 검색
    @GetMapping("/search")
    public ResponseEntity<List<ItemDto>> searchItems(@RequestParam(required = false) String name) {
        List<ItemDto> items = (name == null || name.trim().isEmpty())
                ? itemService.getAllItems()
                : itemService.searchItemsByName(name);
        return ResponseEntity.ok(items);
    }

    // 상품 생성
    @PostMapping("/create/{id}")
    public ResponseEntity<Void> createItem(@RequestBody ItemDto itemDto) {
        itemService.createItem(itemDto);
        return ResponseEntity.status(HttpStatus.CREATED).build(); // 201 Created
    }

    // 상품 삭제
    @DeleteMapping("/delete/{id}")
    public ResponseEntity<Void> deleteItem(@PathVariable Long id) {
        itemService.deleteItemById(id);
        return ResponseEntity.noContent().build(); // 204 No Content
    }
}