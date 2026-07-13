package com.example.demo.dto;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

public class ReportResponseDTO {
    private LocalDate startDate;
    private LocalDate endDate;
    private String rangeLabel;
    private BigDecimal totalIncome;
    private BigDecimal totalExpense;
    private BigDecimal balance;
    private List<TrendPointDTO> trend;
    private List<CategoryBreakdownDTO> categories;

    public ReportResponseDTO(LocalDate startDate, LocalDate endDate, String rangeLabel,
                             BigDecimal totalIncome, BigDecimal totalExpense,
                             List<TrendPointDTO> trend, List<CategoryBreakdownDTO> categories) {
        this.startDate = startDate; this.endDate = endDate; this.rangeLabel = rangeLabel;
        this.totalIncome = totalIncome; this.totalExpense = totalExpense;
        this.balance = totalIncome.subtract(totalExpense);
        this.trend = trend; this.categories = categories;
    }

    public LocalDate getStartDate() { return startDate; }
    public LocalDate getEndDate() { return endDate; }
    public String getRangeLabel() { return rangeLabel; }
    public BigDecimal getTotalIncome() { return totalIncome; }
    public BigDecimal getTotalExpense() { return totalExpense; }
    public BigDecimal getBalance() { return balance; }
    public List<TrendPointDTO> getTrend() { return trend; }
    public List<CategoryBreakdownDTO> getCategories() { return categories; }
}
