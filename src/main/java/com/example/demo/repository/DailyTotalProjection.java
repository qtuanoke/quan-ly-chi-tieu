package com.example.demo.repository;

import java.math.BigDecimal;
import java.time.LocalDate;

public interface DailyTotalProjection {
    LocalDate getDate();
    BigDecimal getTotal();
}
