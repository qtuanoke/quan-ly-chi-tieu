package com.example.demo.dto;

import java.math.BigDecimal;

public class CategoryBreakdownDTO {
    private String categoryName;
    private BigDecimal amount;
    private BigDecimal percentage;

    public CategoryBreakdownDTO(String categoryName, BigDecimal amount, BigDecimal percentage) {
        this.categoryName = categoryName; this.amount = amount; this.percentage = percentage;
    }
    public String getCategoryName() { return categoryName; }
    public BigDecimal getAmount() { return amount; }
    public BigDecimal getPercentage() { return percentage; }
}
