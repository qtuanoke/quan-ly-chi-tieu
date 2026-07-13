package com.example.demo.repository;

import com.example.demo.entity.Budget;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface BudgetRepository extends JpaRepository<Budget, Long> {
    Optional<Budget> findByCategoryIdAndMonthAndYear(Long categoryId, Integer month, Integer year);

    List<Budget> findByMonthAndYear(Integer month, Integer year);
}
