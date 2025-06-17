package com.example.shoppingmall.item.domain.dto;

import com.example.shoppingmall.item.domain.Item;
import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Data
public class ItemDto {
    private Long itemId;
    private Long categoryId;
    private String name;
    private String image;
    private String description;
    private BigDecimal price;
    private String status;
    private String grade;
    private String manufactureCountry;
    private Boolean isBestSeller;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;

    public Item toDomain() {
        Item item = new Item();
        item.setItemId(this.itemId);
        item.setCategoryId(this.categoryId);
        item.setName(this.name);
        item.setImage(this.image);
        item.setDescription(this.description);
        item.setPrice(this.price);
        item.setStatus(this.status);
        item.setGrade(this.grade);
        item.setManufactureCountry(this.manufactureCountry);
        item.setIsBestSeller(this.isBestSeller);
        item.setCreatedAt(this.createdAt);
        item.setUpdatedAt(this.updatedAt);
        return item;
    }

    public static ItemDto fromDto(Item item) {
        ItemDto dto = new ItemDto();
        dto.setItemId(item.getItemId());
        dto.setCategoryId(item.getCategoryId());
        dto.setName(item.getName());
        dto.setImage(item.getImage());
        dto.setDescription(item.getDescription());
        dto.setPrice(item.getPrice());
        dto.setStatus(item.getStatus());
        dto.setGrade(item.getGrade());
        dto.setManufactureCountry(item.getManufactureCountry());
        dto.setIsBestSeller(item.getIsBestSeller());
        dto.setCreatedAt(item.getCreatedAt());
        dto.setUpdatedAt(item.getUpdatedAt());
        return dto;
    }
}
