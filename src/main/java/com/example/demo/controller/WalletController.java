package com.example.demo.controller;

import com.example.demo.dto.WalletForm;
import com.example.demo.service.WalletService;
import jakarta.validation.Valid;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;

@Controller
@RequestMapping("/wallets")
public class WalletController {
    private final WalletService walletService;

    public WalletController(WalletService walletService) {
        this.walletService = walletService;
    }

    @GetMapping
    public String list(Model model) {
        model.addAttribute("wallets", walletService.getAll());
        return "wallet/list";
    }

    @GetMapping("/new")
    public String createForm(Model model) {
        WalletForm form = new WalletForm();
        form.setBalance(BigDecimal.ZERO);
        model.addAttribute("walletForm", form);
        return "wallet/form";
    }

    @GetMapping("/{id}/edit")
    public String editForm(@PathVariable Long id, Model model) {
        var wallet = walletService.getById(id);

        WalletForm form = new WalletForm();
        form.setId(wallet.getId());
        form.setName(wallet.getName());
        form.setBalance(wallet.getBalance());
        form.setNote(wallet.getNote());

        model.addAttribute("walletForm", form);
        return "wallet/form";
    }

    @PostMapping
    public String save(@Valid @ModelAttribute("walletForm") WalletForm form,
                       BindingResult bindingResult) {
        if (bindingResult.hasErrors()) {
            return "wallet/form";
        }
        walletService.save(form);
        return "redirect:/wallets";
    }

    @PostMapping("/{id}/delete")
    public String delete(@PathVariable Long id, Model model) {

        walletService.delete(id);
        return "redirect:/wallets";
    }
}
