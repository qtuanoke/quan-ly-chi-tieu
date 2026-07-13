package com.example.demo.dto;

import com.example.demo.entity.Category;

import java.math.BigDecimal;
import java.math.RoundingMode;

public class BudgetStatusDTO {
    private Long categoryId;
    private String categoryName;
    private BigDecimal budgetAmount;
    private BigDecimal spentAmount;
    private BigDecimal remaining;
    private double percentage;
    private boolean overBudget;

    public BudgetStatusDTO(Category category, BigDecimal budgetAmount, BigDecimal spentAmount) {
        this.categoryId = category.getId();
        this.categoryName = category.getName();
        this.budgetAmount = budgetAmount;
        this.spentAmount = spentAmount;
        this.remaining = budgetAmount.subtract(spentAmount);
        this.percentage = budgetAmount.compareTo(BigDecimal.ZERO) == 0
                ? 0
                : spentAmount.multiply(BigDecimal.valueOf(100))
                  .divide(budgetAmount, 2, RoundingMode.HALF_UP)
                  .doubleValue();
        this.overBudget = spentAmount.compareTo(budgetAmount) > 0;
    }

    public Long getCategoryId() { return categoryId; }
    public String getCategoryName() { return categoryName; }
    public BigDecimal getBudgetAmount() { return budgetAmount; }
    public BigDecimal getSpentAmount() { return spentAmount; }
    public BigDecimal getRemaining() { return remaining; }
    public double getPercentage() { return percentage; }
    public boolean isOverBudget() { return overBudget; }
}
