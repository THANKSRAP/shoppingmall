package com.example.shoppingmall.item.controller;

import com.example.shoppingmall.item.domain.Item;
import com.example.shoppingmall.item.service.ItemService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Controller
@RequestMapping("/item")
public class ItemController {

    private final ItemService itemService;

    public ItemController(ItemService itemService) {
        this.itemService = itemService;
    }

    @GetMapping
    public String list(Model model) {
        List<Item> items = itemService.getAllItems();
        model.addAttribute("items", items);
        return "item/list";
    }

    @GetMapping("/{id}")
    public String detail(@PathVariable Long id, Model model) {
        Item item = itemService.getItemById(id);
        model.addAttribute("item", item);
        return "item/detail";
    }

    @PostMapping
    public String create(Item item) {
        itemService.createItem(item);
        return "redirect:/item";
    }

    @PostMapping("/{id}/delete")
    public String delete(@PathVariable Long id) {
        itemService.deleteItem(id);
        return "redirect:/item";
    }


}
