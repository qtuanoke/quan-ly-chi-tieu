package com.example.demo.controller;

import com.example.demo.dto.TransactionFilter;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import com.example.demo.dto.TransactionForm;
import com.example.demo.service.CategoryService;
import com.example.demo.service.TransactionService;
import com.example.demo.service.WalletService;
import jakarta.validation.Valid;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import common.TransactionType;

import java.time.LocalDate;

@Controller
@RequestMapping("/transactions")
public class TransactionController {
    private final TransactionService transactionService;
    private final CategoryService categoryService;
    private final WalletService walletService;

    public TransactionController(TransactionService transactionService,
                                 CategoryService categoryService,
                                 WalletService walletService) {
        this.transactionService = transactionService;
        this.categoryService = categoryService;
        this.walletService = walletService;
    }

    @GetMapping("/new")
    public String createForm(Model model) {
        TransactionForm form = new TransactionForm();
        form.setTransactionDate(LocalDate.now());
        model.addAttribute("transactionForm", form);
        addFormAttributes(model);
        return "transaction/form";
    }

    @GetMapping("/{id}/edit")
    public String editForm(@PathVariable Long id, Model model) {
        var t = transactionService.getById(id);

        TransactionForm form = new TransactionForm();
        form.setId(t.getId());
        form.setAmount(t.getAmount());
        form.setType(t.getType());
        form.setTransactionDate(t.getTransactionDate());
        form.setNote(t.getNote());
        form.setCategoryId(t.getCategory().getId());
        form.setWalletId(t.getWallet().getId());

        model.addAttribute("transactionForm", form);
        addFormAttributes(model);
        return "transaction/form";
    }

    @PostMapping
    public String save(@Valid @ModelAttribute("transactionForm") TransactionForm form,
                       BindingResult bindingResult, Model model) {
        if (bindingResult.hasErrors()) {
            addFormAttributes(model);
            return "transaction/form";
        }
        transactionService.save(form);
        return "redirect:/transactions";
    }

    @PostMapping("/{id}/delete")
    public String delete(@PathVariable Long id) {
        transactionService.delete(id);
        return "redirect:/transactions";
    }

    private void addFormAttributes(Model model) {
        model.addAttribute("categories", categoryService.getAll());
        model.addAttribute("wallets", walletService.getAll());
        model.addAttribute("types", TransactionType.values());
    }

    @GetMapping
    public String list(@ModelAttribute TransactionFilter filter, Model model) {
        model.addAttribute("transactions", transactionService.search(filter));
        model.addAttribute("filter", filter);
        model.addAttribute("categories", categoryService.getAll());
        model.addAttribute("wallets", walletService.getAll());
        model.addAttribute("types", TransactionType.values());
        return "transaction/list";
    }
}
