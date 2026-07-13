package com.example.demo.service;

import com.example.demo.dto.CategoryStat;
import com.example.demo.entity.Transaction;
import com.example.demo.entity.Wallet;
import com.example.demo.repository.TransactionRepository;
import com.example.demo.repository.WalletRepository;
import common.TransactionType;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.YearMonth;
import java.util.List;

@Service
public class DashboardService {
    private final TransactionRepository transactionRepository;
    private final WalletRepository walletRepository;

    public DashboardService(TransactionRepository transactionRepository,
                            WalletRepository walletRepository) {
        this.transactionRepository = transactionRepository;
        this.walletRepository = walletRepository;
    }

    public DashboardData getDashboardData() {
        YearMonth currentMonth = YearMonth.now();
        LocalDate fromDate = currentMonth.atDay(1);
        LocalDate toDate = currentMonth.atEndOfMonth();

        BigDecimal totalIncome = transactionRepository
                .sumAmountByTypeAndDateRange(TransactionType.INCOME, fromDate, toDate);
        BigDecimal totalExpense = transactionRepository
                .sumAmountByTypeAndDateRange(TransactionType.EXPENSE, fromDate, toDate);
        BigDecimal netBalance = totalIncome.subtract(totalExpense);

        List<Wallet> wallets = walletRepository.findAllByOrderByNameAsc();
        BigDecimal totalWalletBalance = wallets.stream()
                .map(Wallet::getBalance)
                .reduce(BigDecimal.ZERO, BigDecimal::add);

        List<CategoryStat> topExpenseCategories = transactionRepository
                .findTopExpenseCategories(fromDate, toDate);
        if (topExpenseCategories.size() > 5) {
            topExpenseCategories = topExpenseCategories.subList(0, 5);
        }

        List<Transaction> recentTransactions = transactionRepository.findRecent(5);

        return new DashboardData(
                totalIncome, totalExpense, netBalance,
                wallets, totalWalletBalance,
                topExpenseCategories, recentTransactions
        );
    }

    // Inner class để gom toàn bộ dữ liệu dashboard, tránh Controller phải gọi nhiều method rời rạc
    public static class DashboardData {
        private final BigDecimal totalIncome;
        private final BigDecimal totalExpense;
        private final BigDecimal netBalance;
        private final List<Wallet> wallets;
        private final BigDecimal totalWalletBalance;
        private final List<CategoryStat> topExpenseCategories;
        private final List<Transaction> recentTransactions;

        public DashboardData(BigDecimal totalIncome, BigDecimal totalExpense, BigDecimal netBalance,
                             List<Wallet> wallets, BigDecimal totalWalletBalance,
                             List<CategoryStat> topExpenseCategories, List<Transaction> recentTransactions) {
            this.totalIncome = totalIncome;
            this.totalExpense = totalExpense;
            this.netBalance = netBalance;
            this.wallets = wallets;
            this.totalWalletBalance = totalWalletBalance;
            this.topExpenseCategories = topExpenseCategories;
            this.recentTransactions = recentTransactions;
        }

        public BigDecimal getTotalIncome() { return totalIncome; }
        public BigDecimal getTotalExpense() { return totalExpense; }
        public BigDecimal getNetBalance() { return netBalance; }
        public List<Wallet> getWallets() { return wallets; }
        public BigDecimal getTotalWalletBalance() { return totalWalletBalance; }
        public List<CategoryStat> getTopExpenseCategories() { return topExpenseCategories; }
        public List<Transaction> getRecentTransactions() { return recentTransactions; }
    }
}
