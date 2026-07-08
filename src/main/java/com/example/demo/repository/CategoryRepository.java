package com.example.demo.repository;

import com.example.demo.entity.Category;
import common.TransactionType;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface CategoryRepository extends JpaRepository<Category, Long> {
    List<Category> findByType(TransactionType type);

    List<Category> findAllByOrderByNameAsc();
}
