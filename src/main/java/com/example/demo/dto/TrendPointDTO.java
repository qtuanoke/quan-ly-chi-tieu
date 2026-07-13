package com.example.demo.dto;

import java.math.BigDecimal;

public class TrendPointDTO {
    private String label;
    private BigDecimal income;
    private BigDecimal expense;

    public TrendPointDTO(String label, BigDecimal income, BigDecimal expense) {
        this.label = label; this.income = income; this.expense = expense;
    }
    public String getLabel() { return label; }
    public BigDecimal getIncome() { return income; }
    public BigDecimal getExpense() { return expense; }
}
