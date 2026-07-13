package com.example.demo.repository;

import com.example.demo.dto.CategoryStat;
import com.example.demo.entity.Transaction;
import common.TransactionType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

public interface TransactionRepository extends JpaRepository<Transaction, Long> {
    @Query("SELECT t FROM Transaction t " +
            "JOIN FETCH t.category " +
            "JOIN FETCH t.wallet " +
            "ORDER BY t.transactionDate DESC, t.id DESC")
    List<Transaction> findAllWithDetails();

    @Query("SELECT t FROM Transaction t " +
            "JOIN FETCH t.category " +
            "JOIN FETCH t.wallet " +
            "ORDER BY t.transactionDate DESC, t.id DESC " +
            "LIMIT :limit")
    List<Transaction> findRecent(@Param("limit") int limit);

    @Query("SELECT COALESCE(SUM(t.amount), 0) FROM Transaction t " +
            "WHERE t.type = :type " +
            "AND t.transactionDate BETWEEN :fromDate AND :toDate")
    BigDecimal sumAmountByTypeAndDateRange(@Param("type") TransactionType type,
                                           @Param("fromDate") LocalDate fromDate,
                                           @Param("toDate") LocalDate toDate);

    @Query("SELECT new com.example.demo.dto.CategoryStat(c.name, c.icon, c.color, SUM(t.amount)) " +
            "FROM Transaction t JOIN t.category c " +
            "WHERE t.type = 'EXPENSE' " +
            "AND t.transactionDate BETWEEN :fromDate AND :toDate " +
            "GROUP BY c.name, c.icon, c.color " +
            "ORDER BY SUM(t.amount) DESC")
    List<CategoryStat> findTopExpenseCategories(@Param("fromDate") LocalDate fromDate,
                                                @Param("toDate") LocalDate toDate);

    @Query("SELECT t FROM Transaction t " +
            "JOIN FETCH t.category c " +
            "JOIN FETCH t.wallet w " +
            "WHERE (:fromDate IS NULL OR t.transactionDate >= :fromDate) " +
            "AND (:toDate IS NULL OR t.transactionDate <= :toDate) " +
            "AND (:categoryId IS NULL OR c.id = :categoryId) " +
            "AND (:walletId IS NULL OR w.id = :walletId) " +
            "AND (:type IS NULL OR t.type = :type) " +
            "ORDER BY t.transactionDate DESC, t.id DESC")
    List<Transaction> search(@Param("fromDate") LocalDate fromDate,
                             @Param("toDate") LocalDate toDate,
                             @Param("categoryId") Long categoryId,
                             @Param("walletId") Long walletId,
                             @Param("type") TransactionType type);

    @Query("SELECT new com.example.demo.dto.CategoryStat(c.name, c.icon, c.color, SUM(t.amount)) " +
            "FROM Transaction t JOIN t.category c " +
            "WHERE t.type = :type " +
            "AND (:fromDate IS NULL OR t.transactionDate >= :fromDate) " +
            "AND (:toDate IS NULL OR t.transactionDate <= :toDate) " +
            "GROUP BY c.name, c.icon, c.color " +
            "ORDER BY SUM(t.amount) DESC")
    List<CategoryStat> reportByCategory(@Param("type") TransactionType type,
                                        @Param("fromDate") LocalDate fromDate,
                                        @Param("toDate") LocalDate toDate);

    @Query("SELECT COALESCE(SUM(t.amount), 0) FROM Transaction t " +
            "WHERE t.category.id = :categoryId " +
            "AND t.type = 'EXPENSE' " +
            "AND MONTH(t.transactionDate) = :month " +
            "AND YEAR(t.transactionDate) = :year")
    BigDecimal sumExpenseByCategoryAndMonth(
            @Param("categoryId") Long categoryId,
            @Param("month") Integer month,
            @Param("year") Integer year);
}
