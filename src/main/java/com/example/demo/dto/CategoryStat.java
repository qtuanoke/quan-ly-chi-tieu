package com.example.demo.dto;

import java.math.BigDecimal;

public class CategoryStat {
    private String categoryName;
    private String icon;
    private String color;
    private BigDecimal totalAmount;

    public CategoryStat(String categoryName, String icon, String color, BigDecimal totalAmount) {
        this.categoryName = categoryName;
        this.icon = icon;
        this.color = color;
        this.totalAmount = totalAmount;
    }

    public String getCategoryName() { return categoryName; }
    public String getIcon() { return icon; }
    public String getColor() { return color; }
    public BigDecimal getTotalAmount() { return totalAmount; }
}
