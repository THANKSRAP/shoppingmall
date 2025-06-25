package com.example.shoppingmall;

import com.example.shoppingmall.item.domain.dto.ItemDto;
import com.example.shoppingmall.item.service.ItemService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import java.util.List;

@Controller
public class HomeController {

    @Autowired
    private ItemService itemService;

    @GetMapping("/")
    public String home(Model model) {
        List<ItemDto> bestSellers = itemService.getBestSellers();
        List<ItemDto> newReleases = itemService.getNewItems();

        bestSellers.forEach(item -> {
            ItemDto summary = itemService.getItemWithReviewSummary(item.getItemId());
            if (summary != null) {
                item.setAverageRating(summary.getAverageRating());
                item.setReviewCount(summary.getReviewCount());
            }
        });

        newReleases.forEach(item -> {
            ItemDto summary = itemService.getItemWithReviewSummary(item.getItemId());
            if (summary != null) {
                item.setAverageRating(summary.getAverageRating());
                item.setReviewCount(summary.getReviewCount());
            }
        });

        model.addAttribute("bestSellers", bestSellers);
        model.addAttribute("newReleases", newReleases);

        return "home";
    }
}
