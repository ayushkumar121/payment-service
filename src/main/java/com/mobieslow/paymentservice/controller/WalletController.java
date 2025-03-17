package com.mobieslow.paymentservice.controller;

import com.mobieslow.paymentservice.dao.WalletDao;
import com.mobieslow.paymentservice.models.Wallet;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Optional;

@RestController
@RequestMapping("/wallet")
public class WalletController {
    private final WalletDao walletDao;

    public WalletController(WalletDao walletDao) {
        this.walletDao = walletDao;
    }

    @PostMapping("/{walletId}")
    public ResponseEntity<Wallet> get(@PathVariable("walletId") Long walletId) {
        return walletDao.getWallet(walletId)
                .map(wallet -> new ResponseEntity<>(wallet, HttpStatus.OK))
                .orElseGet(() -> new ResponseEntity<>(HttpStatus.NOT_FOUND));
    }
}
