package com.example.demo.dto;

import java.math.BigDecimal;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.PositiveOrZero;

public class WalletForm {
    private Long id;

    @NotBlank(message = "Tên ví không được để trống")
    private String name;

    @NotNull(message = "Vui lòng nhập số dư ban đầu")
    @PositiveOrZero(message = "Số dư không được âm")
    private BigDecimal balance;

    private String note;

    // Getters và Setters
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public String getName() { return name; }
    public void setName(String name) { this.name = name; }

    public BigDecimal getBalance() { return balance; }
    public void setBalance(BigDecimal balance) { this.balance = balance; }

    public String getNote() { return note; }
    public void setNote(String note) { this.note = note; }
}
