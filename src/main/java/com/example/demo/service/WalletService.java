package com.example.demo.service;

import com.example.demo.dto.WalletForm;
import org.springframework.stereotype.Service;
import com.example.demo.entity.Wallet;
import com.example.demo.repository.WalletRepository;

import java.math.BigDecimal;
import java.util.List;

@Service
public class WalletService {
    private final WalletRepository walletRepository;

    public WalletService(WalletRepository walletRepository) {
        this.walletRepository = walletRepository;
    }

    public List<Wallet> getAll() {
        return walletRepository.findAllByOrderByNameAsc();
    }

    public Wallet getById(Long id) {
        return walletRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Không tìm thấy ví với id: " + id));
    }

    public Wallet save(WalletForm form) {
        Wallet wallet;
        if (form.getId() != null) {
            wallet = getById(form.getId());
        } else {
            wallet = new Wallet();
        }

        wallet.setName(form.getName());
        wallet.setBalance(form.getBalance());
        wallet.setNote(form.getNote());

        return walletRepository.save(wallet);
    }

    public void delete(Long id) {
        walletRepository.deleteById(id);
    }

    // Sẽ dùng ở Phase 5 khi xử lý Transaction
    public void adjustBalance(Long walletId, BigDecimal amount) {
        Wallet wallet = getById(walletId);
        wallet.setBalance(wallet.getBalance().add(amount));
        walletRepository.save(wallet);
    }
}
