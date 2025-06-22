package com.example.shoppingmall.item.controller;

import com.example.shoppingmall.item.domain.Category;
import com.example.shoppingmall.item.domain.Item;
import com.example.shoppingmall.item.domain.dto.CategoryDto;
import com.example.shoppingmall.item.domain.dto.ItemDto;
import com.example.shoppingmall.item.service.CategoryService;
import com.example.shoppingmall.item.service.ItemService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Controller
@RequestMapping("/item")
public class ItemController {

    private final ItemService itemService;
    private final CategoryService categoryService;

    public ItemController(ItemService itemService, CategoryService categoryService) {
        this.itemService = itemService;
        this.categoryService = categoryService;
    }

    @GetMapping
    public String list(Model model) {
        List<ItemDto> items = itemService.getAllItems();
        model.addAttribute("items", items);

        List<CategoryDto> majorCategories = categoryService.getCategoriesByDepth(1);
        List<CategoryDto> middleCategories = categoryService.getCategoriesByDepth(2);
        List<CategoryDto> minorCategories = categoryService.getCategoriesByDepth(3);

        model.addAttribute("majorCategories", majorCategories);
        model.addAttribute("middleCategories", middleCategories);
        model.addAttribute("minorCategories", minorCategories);

        return "item/list";
    }

    @GetMapping("/filter")
    public String filterItemsByCategory(
            @RequestParam(required = false) Long majorId,
            @RequestParam(required = false) Long middleId,
            @RequestParam(required = false) Long minorId,
            Model model) {

        List<ItemDto> items = itemService.getItemsByCategory(majorId, middleId, minorId);
        model.addAttribute("items", items);

        // 카테고리 리스트도 다시 전달
        model.addAttribute("majorCategories", categoryService.getCategoriesByDepth(1));
        model.addAttribute("middleCategories", categoryService.getCategoriesByDepth(2));
        model.addAttribute("minorCategories", categoryService.getCategoriesByDepth(3));

        return "item/list";
    }

    @GetMapping("/{id}")
    public String detail(@PathVariable Long id, Model model) {
        ItemDto itemDto = itemService.getItemById(id);
        CategoryDto categoryDto = categoryService.getCategoryByItemId(id);

        model.addAttribute("item", itemDto);
        model.addAttribute("category", categoryDto);

        return "item/detail";
    }

    @PostMapping
    public String create(@ModelAttribute ItemDto itemDto) {
        itemService.createItem(itemDto);

        return "redirect:/item";
    }

    @PostMapping("/{id}/delete")
    public String delete(@PathVariable Long id) {
        itemService.deleteItemById(id);

        return "redirect:/item";
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
