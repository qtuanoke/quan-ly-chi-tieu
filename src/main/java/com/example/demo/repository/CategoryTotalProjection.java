package com.example.demo.repository;

import java.math.BigDecimal;

public interface CategoryTotalProjection {
    String getCategoryName();
    BigDecimal getTotal();
}
