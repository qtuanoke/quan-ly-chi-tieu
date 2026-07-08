package com.example.demo.service;

import com.example.demo.dto.CategoryStat;
import com.example.demo.dto.TransactionFilter;
import com.example.demo.dto.TransactionForm;
import com.example.demo.entity.Category;
import com.example.demo.entity.Transaction;
import com.example.demo.entity.Wallet;
import com.example.demo.repository.CategoryRepository;
import com.example.demo.repository.TransactionRepository;
import com.example.demo.repository.WalletRepository;
import common.TransactionType;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

@Service
public class TransactionService {
    private final TransactionRepository transactionRepository;
    private final CategoryRepository categoryRepository;
    private final WalletRepository walletRepository;
    private final WalletService walletService;

    public TransactionService(TransactionRepository transactionRepository,
                              CategoryRepository categoryRepository,
                              WalletRepository walletRepository,
                              WalletService walletService) {
        this.transactionRepository = transactionRepository;
        this.categoryRepository = categoryRepository;
        this.walletRepository = walletRepository;
        this.walletService = walletService;
    }

    public List<Transaction> getAll() {
        return transactionRepository.findAllWithDetails();
    }

    public Transaction getById(Long id) {
        return transactionRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Không tìm thấy giao dịch với id: " + id));
    }

    @Transactional
    public Transaction save(TransactionForm form) {
        Category category = categoryRepository.findById(form.getCategoryId())
                .orElseThrow(() -> new IllegalArgumentException("Danh mục không tồn tại"));
        Wallet wallet = walletRepository.findById(form.getWalletId())
                .orElseThrow(() -> new IllegalArgumentException("Ví không tồn tại"));

        Transaction transaction;

        if (form.getId() != null) {
            // SỬA: hoàn tác hiệu ứng cũ trước
            transaction = getById(form.getId());
            reverseWalletEffect(transaction);
        } else {
            // THÊM MỚI
            transaction = new Transaction();
        }

        transaction.setAmount(form.getAmount());
        transaction.setType(form.getType());
        transaction.setTransactionDate(form.getTransactionDate());
        transaction.setNote(form.getNote());
        transaction.setCategory(category);
        transaction.setWallet(wallet);

        Transaction saved = transactionRepository.save(transaction);

        // Áp dụng hiệu ứng mới lên ví (có thể là ví mới, amount mới)
        applyWalletEffect(saved);

        return saved;
    }

    @Transactional
    public void delete(Long id) {
        Transaction transaction = getById(id);
        reverseWalletEffect(transaction);
        transactionRepository.delete(transaction);
    }

    private void applyWalletEffect(Transaction transaction) {
        BigDecimal signedAmount = transaction.getType() == TransactionType.INCOME
                ? transaction.getAmount()
                : transaction.getAmount().negate();
        walletService.adjustBalance(transaction.getWallet().getId(), signedAmount);
    }

    private void reverseWalletEffect(Transaction transaction) {
        BigDecimal signedAmount = transaction.getType() == TransactionType.INCOME
                ? transaction.getAmount().negate()
                : transaction.getAmount();
        walletService.adjustBalance(transaction.getWallet().getId(), signedAmount);
    }

    public List<Transaction> search(TransactionFilter filter) {
        return transactionRepository.search(
                filter.getFromDate(),
                filter.getToDate(),
                filter.getCategoryId(),
                filter.getWalletId(),
                filter.getType()
        );
    }
    public List<CategoryStat> reportByCategory(TransactionType type, LocalDate fromDate, LocalDate toDate) {
        return transactionRepository.reportByCategory(type, fromDate, toDate);
    }
}
