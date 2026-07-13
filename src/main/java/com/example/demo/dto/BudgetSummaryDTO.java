package com.example.demo.dto;

import java.math.BigDecimal;
import java.util.List;

public class BudgetSummaryDTO {
    private final List<BudgetStatusDTO> budgetStatuses;
    private final BigDecimal totalBudget;
    private final BigDecimal totalSpent;

    public BudgetSummaryDTO(List<BudgetStatusDTO> budgetStatuses, BigDecimal totalBudget, BigDecimal totalSpent) {
        this.budgetStatuses = budgetStatuses;
        this.totalBudget = totalBudget;
        this.totalSpent = totalSpent;
    }

    public List<BudgetStatusDTO> getBudgetStatuses() { return budgetStatuses; }
    public BigDecimal getTotalBudget() { return totalBudget; }
    public BigDecimal getTotalSpent() { return totalSpent; }
}
