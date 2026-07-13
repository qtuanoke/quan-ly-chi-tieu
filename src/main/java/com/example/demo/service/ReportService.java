package com.example.demo.service;

import com.example.demo.dto.CategoryBreakdownDTO;
import com.example.demo.dto.ReportResponseDTO;
import com.example.demo.dto.TrendPointDTO;
import com.example.demo.repository.CategoryTotalProjection;
import com.example.demo.repository.DailyTotalProjection;
import com.example.demo.repository.TransactionRepository;
import common.TransactionType;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
public class ReportService {
    private final TransactionRepository transactionRepository;

    public ReportService(TransactionRepository transactionRepository) {
        this.transactionRepository = transactionRepository;
    }

    public ReportResponseDTO getWeeklyReport(LocalDate anyDateInWeek) {
        LocalDate start = anyDateInWeek.with(DayOfWeek.MONDAY);
        LocalDate end = start.plusDays(6);
        String label = start.format(DateTimeFormatter.ofPattern("dd/MM"))
                + " – " + end.format(DateTimeFormatter.ofPattern("dd/MM/yyyy"));
        return buildReport(start, end, label, false);
    }

    public ReportResponseDTO getMonthlyReport(int month, int year) {
        LocalDate start = LocalDate.of(year, month, 1);
        LocalDate end = start.withDayOfMonth(start.lengthOfMonth());
        String label = "Tháng " + month + "/" + year;
        return buildReport(start, end, label, true);
    }

    private ReportResponseDTO buildReport(LocalDate start, LocalDate end, String rangeLabel, boolean monthly) {
        BigDecimal totalIncome = transactionRepository.sumByTypeAndDateRange(TransactionType.INCOME, start, end);
        BigDecimal totalExpense = transactionRepository.sumByTypeAndDateRange(TransactionType.EXPENSE, start, end);

        List<TrendPointDTO> trend = monthly ? weeklyBuckets(start, end) : dailyBuckets(start, end);

        List<CategoryTotalProjection> rows = transactionRepository
                .findCategoryTotals(TransactionType.EXPENSE, start, end);
        BigDecimal base = totalExpense.compareTo(BigDecimal.ZERO) == 0 ? BigDecimal.ONE : totalExpense;

        List<CategoryBreakdownDTO> categories = rows.stream()
                .map(r -> new CategoryBreakdownDTO(r.getCategoryName(), r.getTotal(),
                        r.getTotal().multiply(BigDecimal.valueOf(100))
                                .divide(base, 1, RoundingMode.HALF_UP)))
                .collect(Collectors.toList());

        return new ReportResponseDTO(start, end, rangeLabel, totalIncome, totalExpense, trend, categories);
    }

    private List<TrendPointDTO> dailyBuckets(LocalDate start, LocalDate end) {
        Map<LocalDate, BigDecimal> income = toMap(transactionRepository.findDailyTotals(TransactionType.INCOME, start, end));
        Map<LocalDate, BigDecimal> expense = toMap(transactionRepository.findDailyTotals(TransactionType.EXPENSE, start, end));

        List<TrendPointDTO> points = new ArrayList<>();
        for (LocalDate d = start; !d.isAfter(end); d = d.plusDays(1)) {
            points.add(new TrendPointDTO(vnDayLabel(d.getDayOfWeek()),
                    income.getOrDefault(d, BigDecimal.ZERO),
                    expense.getOrDefault(d, BigDecimal.ZERO)));
        }
        return points;
    }

    private List<TrendPointDTO> weeklyBuckets(LocalDate start, LocalDate end) {
        Map<LocalDate, BigDecimal> income = toMap(transactionRepository.findDailyTotals(TransactionType.INCOME, start, end));
        Map<LocalDate, BigDecimal> expense = toMap(transactionRepository.findDailyTotals(TransactionType.EXPENSE, start, end));

        List<TrendPointDTO> points = new ArrayList<>();
        LocalDate cursor = start;
        int week = 1;
        while (!cursor.isAfter(end)) {
            LocalDate weekEnd = cursor.plusDays(6).isAfter(end) ? end : cursor.plusDays(6);
            BigDecimal inc = BigDecimal.ZERO, exp = BigDecimal.ZERO;
            for (LocalDate d = cursor; !d.isAfter(weekEnd); d = d.plusDays(1)) {
                inc = inc.add(income.getOrDefault(d, BigDecimal.ZERO));
                exp = exp.add(expense.getOrDefault(d, BigDecimal.ZERO));
            }
            points.add(new TrendPointDTO("Tuần " + week, inc, exp));
            cursor = weekEnd.plusDays(1);
            week++;
        }
        return points;
    }

    private Map<LocalDate, BigDecimal> toMap(List<DailyTotalProjection> rows) {
        return rows.stream().collect(Collectors.toMap(DailyTotalProjection::getDate, DailyTotalProjection::getTotal));
    }

    private String vnDayLabel(DayOfWeek d) {
        return switch (d) {
            case MONDAY -> "T2"; case TUESDAY -> "T3"; case WEDNESDAY -> "T4";
            case THURSDAY -> "T5"; case FRIDAY -> "T6"; case SATURDAY -> "T7"; default -> "CN";
        };
    }
}
