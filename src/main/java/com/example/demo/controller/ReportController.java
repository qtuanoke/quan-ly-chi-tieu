package com.example.demo.controller;

import com.example.demo.dto.ReportFilter;
import com.example.demo.service.TransactionService;
import common.TransactionType;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;

import java.math.BigDecimal;
@Controller
@RequestMapping("/reports")
public class ReportController {

    private final TransactionService transactionService;

    public ReportController(TransactionService transactionService) {
        this.transactionService = transactionService;
    }

    @GetMapping
    public String report(@ModelAttribute ReportFilter filter, Model model) {
        var stats = transactionService.reportByCategory(filter.getType(), filter.getFromDate(), filter.getToDate());

        BigDecimal total = stats.stream()
                .map(s -> s.getTotalAmount())
                .reduce(BigDecimal.ZERO, BigDecimal::add);

        model.addAttribute("filter", filter);
        model.addAttribute("stats", stats);
        model.addAttribute("total", total);
        model.addAttribute("types", TransactionType.values());
        return "report";
    }
}
