package com.example.shoppingmall.item.controller;

import com.example.shoppingmall.item.domain.dto.CategoryDto;
import com.example.shoppingmall.item.domain.dto.ItemDto;
import com.example.shoppingmall.item.domain.dto.ItemOptionDto;
import com.example.shoppingmall.item.service.CategoryService;
import com.example.shoppingmall.item.service.ItemOptionService;
import com.example.shoppingmall.item.service.ItemService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Controller
@RequestMapping("/item")
public class ItemViewController {

    private final ItemService itemService;
    private final CategoryService categoryService;
    private final ItemOptionService itemOptionService;

    public ItemViewController(ItemService itemService, CategoryService categoryService, ItemOptionService itemOptionService) {
        this.itemService = itemService;
        this.categoryService = categoryService;
        this.itemOptionService = itemOptionService;
    }

    // 상품 상세 페이지
    @GetMapping("/{id}")
    public String detail(@PathVariable Long id, Model model) {
        ItemDto item = itemService.getItemById(id);
        CategoryDto category = categoryService.getCategoryByItemId(id);
        List<ItemOptionDto> options = itemOptionService.getItemOptionsWithInventory(id); // 서비스 계층에서 옵션+재고 조회
        List<ItemOptionDto> sizeOptions = itemOptionService.getSizeOptions(id);
        List<ItemOptionDto> colorOptions = itemOptionService.getColorOptions(id);

        model.addAttribute("item", item);
        model.addAttribute("category", category);
        model.addAttribute("options", options);
        model.addAttribute("colorOptions", colorOptions);
        model.addAttribute("sizeOptions", sizeOptions);

        return "item/detail";
    }

    // 상품 목록 페이지
    @GetMapping("/list")
    public String list(Long majorId, Long middleId, Long minorId, Model model) {
        List<ItemDto> items = (majorId == null && middleId == null && minorId == null)
                ? itemService.getAllItems()
                : itemService.getItemsByCategory(majorId, middleId, minorId);
        model.addAttribute("items", items);

        // [추가] 대분류 카테고리 리스트를 Model에 담아 전달
        List<CategoryDto> majorCategories = categoryService.getCategoriesByDepth(1);
        model.addAttribute("majorCategories", majorCategories);

        // 선택값 유지
        model.addAttribute("majorId", majorId);
        model.addAttribute("middleId", middleId);
        model.addAttribute("minorId", minorId);

        return "item/list";
    }

    @GetMapping("/search")
    public String search(@RequestParam(value = "name", required = false) String name, Model model) {
        List<ItemDto> items;

        if (name != null && !name.trim().isEmpty()) {
            items = itemService.searchItemsByName(name);
        } else {
            items = itemService.getAllItems();
        }
        model.addAttribute("items", items);
        model.addAttribute("searchName", name); // 검색어를 뷰에 전달

        return "item/search";
    }


}
