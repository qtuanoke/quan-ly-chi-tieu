package com.example.demo.service;

import com.example.demo.dto.CategoryForm;
import com.example.demo.entity.Category;
import com.example.demo.repository.CategoryRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class CategoryService {
    private final CategoryRepository categoryRepository;

    public CategoryService(CategoryRepository categoryRepository) {
        this.categoryRepository = categoryRepository;
    }

    public List<Category> getAll() {
        return categoryRepository.findAllByOrderByNameAsc();
    }

    public Category getById(Long id) {
        return categoryRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Không tìm thấy danh mục với id: " + id));
    }

    public Category save(CategoryForm form) {
        Category category;
        if (form.getId() != null) {
            category = getById(form.getId()); // sửa: lấy record cũ ra để update
        } else {
            category = new Category(); // thêm mới
        }

        category.setName(form.getName());
        category.setType(form.getType());
        category.setIcon(form.getIcon());
        category.setColor(form.getColor());

        return categoryRepository.save(category);
    }

    public void delete(Long id) {
        categoryRepository.deleteById(id);
    }
}
