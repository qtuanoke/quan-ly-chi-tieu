package com.example.demo.controller;

import com.example.demo.dto.ReportResponseDTO;
import com.example.demo.service.ReportService;
import lombok.RequiredArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.time.LocalDate;


@Controller
@RequestMapping("/reports")
@RequiredArgsConstructor
public class ReportController {

    private final ReportService reportService;

    @GetMapping
    public String viewReports(
            @RequestParam(defaultValue = "week") String period,
            @RequestParam(required = false)
            @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate date,
            Model model) {

        LocalDate reference = (date != null) ? date : LocalDate.now();
        boolean monthly = "month".equals(period);

        ReportResponseDTO report = monthly
                ? reportService.getMonthlyReport(reference.getMonthValue(), reference.getYear())
                : reportService.getWeeklyReport(reference);

        LocalDate prevDate = monthly ? reference.minusMonths(1) : reference.minusWeeks(1);
        LocalDate nextDate = monthly ? reference.plusMonths(1) : reference.plusWeeks(1);

        model.addAttribute("report", report);
        model.addAttribute("period", period);
        model.addAttribute("referenceDate", reference);
        model.addAttribute("prevDate", prevDate);
        model.addAttribute("nextDate", nextDate);
        return "report";
    }
}
