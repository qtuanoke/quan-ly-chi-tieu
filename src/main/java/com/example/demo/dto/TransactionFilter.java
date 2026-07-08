package com.example.demo.dto;

import common.TransactionType;

import java.time.LocalDate;

public class TransactionFilter {
    private LocalDate fromDate;
    private LocalDate toDate;
    private Long categoryId;
    private Long walletId;
    private TransactionType type;

    public LocalDate getFromDate() { return fromDate; }
    public void setFromDate(LocalDate fromDate) { this.fromDate = fromDate; }

    public LocalDate getToDate() { return toDate; }
    public void setToDate(LocalDate toDate) { this.toDate = toDate; }

    public Long getCategoryId() { return categoryId; }
    public void setCategoryId(Long categoryId) { this.categoryId = categoryId; }

    public Long getWalletId() { return walletId; }
    public void setWalletId(Long walletId) { this.walletId = walletId; }

    public TransactionType getType() { return type; }
    public void setType(TransactionType type) { this.type = type; }
}
