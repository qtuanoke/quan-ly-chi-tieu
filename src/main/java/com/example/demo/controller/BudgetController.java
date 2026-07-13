package com.example.demo.controller;

import com.example.demo.dto.BudgetSummaryDTO;
import com.example.demo.repository.CategoryRepository;
import com.example.demo.service.BudgetService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.ui.Model;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.math.BigDecimal;
import java.time.LocalDate;

@Controller
@RequestMapping("/budgets")
@RequiredArgsConstructor
public class BudgetController {
    private final BudgetService budgetService;
    private final CategoryRepository categoryRepository;

    @GetMapping
    public String viewBudgets(
            @RequestParam(required = false) Integer month,
            @RequestParam(required = false) Integer year,
            Model model) {

        LocalDate now = LocalDate.now();
        int m = (month != null) ? month : now.getMonthValue();
        int y = (year != null) ? year : now.getYear();

        BudgetSummaryDTO summary = budgetService.getBudgetSummary(m, y);

        model.addAttribute("budgetStatuses", summary.getBudgetStatuses());
        model.addAttribute("totalBudget", summary.getTotalBudget());
        model.addAttribute("totalSpent", summary.getTotalSpent());
        model.addAttribute("categories", categoryRepository.findAll());
        model.addAttribute("selectedMonth", m);
        model.addAttribute("selectedYear", y);
        return "budgets/list";
    }

    @PostMapping
    public String createOrUpdateBudget(
            @RequestParam Long categoryId,
            @RequestParam BigDecimal amount,
            @RequestParam int month,
            @RequestParam int year,
            RedirectAttributes redirectAttributes) {

        try {
            budgetService.setBudget(categoryId, amount, month, year);
        } catch (IllegalArgumentException e) {
            redirectAttributes.addFlashAttribute("error", e.getMessage());
        }
        return "redirect:/budgets?month=" + month + "&year=" + year;
    }
}
