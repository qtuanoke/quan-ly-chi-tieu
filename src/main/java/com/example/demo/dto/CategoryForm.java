package com.example.demo.dto;

import common.TransactionType;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public class CategoryForm {
    private Long id;

    @NotBlank(message = "Tên danh mục không được để trống")
    private String name;

    @NotNull(message = "Vui lòng chọn loại danh mục")
    private TransactionType type;

    private String icon;
    private String color;

    // Getters và Setters
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public String getName() { return name; }
    public void setName(String name) { this.name = name; }

    public TransactionType getType() { return type; }
    public void setType(TransactionType type) { this.type = type; }

    public String getIcon() { return icon; }
    public void setIcon(String icon) { this.icon = icon; }

    public String getColor() { return color; }
    public void setColor(String color) { this.color = color; }
}
