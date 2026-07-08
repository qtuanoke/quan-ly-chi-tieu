package com.example.demo.controller;

import common.TransactionType;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import com.example.demo.dto.CategoryForm;
import com.example.demo.service.CategoryService;
import jakarta.validation.Valid;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
@RequestMapping("/categories")
public class CategoryController {
    private final CategoryService categoryService;

    public CategoryController(CategoryService categoryService) {
        this.categoryService = categoryService;
    }

    @GetMapping
    public String list(Model model) {
        model.addAttribute("categories", categoryService.getAll());
        return "category/list";
    }

    @GetMapping("/new")
    public String createForm(Model model) {
        model.addAttribute("categoryForm", new CategoryForm());
        model.addAttribute("types", TransactionType.values());
        return "category/form";
    }

    @GetMapping("/{id}/edit")
    public String editForm(@PathVariable Long id, Model model) {
        var category = categoryService.getById(id);

        CategoryForm form = new CategoryForm();
        form.setId(category.getId());
        form.setName(category.getName());
        form.setType(category.getType());
        form.setIcon(category.getIcon());
        form.setColor(category.getColor());

        model.addAttribute("categoryForm", form);
        model.addAttribute("types", TransactionType.values());
        return "category/form";
    }

    @PostMapping
    public String save(@Valid @ModelAttribute("categoryForm") CategoryForm form,
                       BindingResult bindingResult,
                       Model model,
                       RedirectAttributes redirectAttributes) {
        if (bindingResult.hasErrors()) {
            model.addAttribute("types", TransactionType.values());
            return "category/form";
        }
        boolean isNew = form.getId() == null;
        categoryService.save(form);
        redirectAttributes.addFlashAttribute("successMessage",
                isNew ? "Đã thêm danh mục thành công" : "Đã cập nhật danh mục thành công");
        return "redirect:/categories";
    }

    @PostMapping("/{id}/delete")
    public String delete(@PathVariable Long id, Model model, RedirectAttributes redirectAttributes) {
        categoryService.delete(id);
        redirectAttributes.addFlashAttribute("successMessage", "Đã xóa danh mục thành công");
        return "redirect:/categories";
    }
}
