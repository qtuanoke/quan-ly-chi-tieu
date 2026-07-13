package com.example.demo.service;

import com.example.demo.dto.BudgetStatusDTO;
import com.example.demo.dto.BudgetSummaryDTO;
import com.example.demo.entity.Budget;
import com.example.demo.entity.Category;
import com.example.demo.repository.BudgetRepository;
import com.example.demo.repository.CategoryRepository;
import com.example.demo.repository.TransactionRepository;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class BudgetService {
    private final BudgetRepository budgetRepository;
    private final TransactionRepository transactionRepository;
    private final CategoryRepository categoryRepository;

    public void setBudget(Long categoryId, BigDecimal amount, int month, int year) {
        if (amount == null || amount.compareTo(BigDecimal.ZERO) <= 0) {
            throw new IllegalArgumentException("Số tiền ngân sách phải lớn hơn 0");
        }

        Budget budget = budgetRepository
                .findByCategoryIdAndMonthAndYear(categoryId, month, year)
                .orElseGet(Budget::new);

        Category category = categoryRepository.findById(categoryId)
                .orElseThrow(() -> new EntityNotFoundException("Category không tồn tại"));

        budget.setCategory(category);
        budget.setAmount(amount);
        budget.setMonth(month);
        budget.setYear(year);
        budgetRepository.save(budget);
    }

    public List<BudgetStatusDTO> getBudgetStatus(int month, int year) {
        List<Budget> budgets = budgetRepository.findByMonthAndYear(month, year);

        return budgets.stream().map(b -> {
            BigDecimal spent = transactionRepository.sumExpenseByCategoryAndMonth(
                    b.getCategory().getId(), month, year);
            return new BudgetStatusDTO(b.getCategory(), b.getAmount(), spent);
        }).collect(Collectors.toList());
    }

    public BudgetSummaryDTO getBudgetSummary(int month, int year) {
        List<BudgetStatusDTO> statuses = getBudgetStatus(month, year);

        BigDecimal totalBudget = statuses.stream()
                .map(BudgetStatusDTO::getBudgetAmount)
                .reduce(BigDecimal.ZERO, BigDecimal::add);

        BigDecimal totalSpent = statuses.stream()
                .map(BudgetStatusDTO::getSpentAmount)
                .reduce(BigDecimal.ZERO, BigDecimal::add);

        return new BudgetSummaryDTO(statuses, totalBudget, totalSpent);
    }
}
