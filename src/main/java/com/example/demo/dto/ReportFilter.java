package com.example.demo.dto;

import common.TransactionType;

import java.time.LocalDate;

public class ReportFilter {
    private LocalDate fromDate;
    private LocalDate toDate;
    private TransactionType type = TransactionType.EXPENSE; // mặc định xem báo cáo Chi

    public LocalDate getFromDate() { return fromDate; }
    public void setFromDate(LocalDate fromDate) { this.fromDate = fromDate; }

    public LocalDate getToDate() { return toDate; }
    public void setToDate(LocalDate toDate) { this.toDate = toDate; }

    public TransactionType getType() { return type; }
    public void setType(TransactionType type) { this.type = type; }
}
