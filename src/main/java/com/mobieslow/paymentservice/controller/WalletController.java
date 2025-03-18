package com.mobieslow.paymentservice.controller;

import com.mobieslow.paymentservice.dao.WalletDao;
import com.mobieslow.paymentservice.models.Wallet;
import com.mobieslow.paymentservice.utils.ApiResponse;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.sql.SQLException;

@RestController
@RequestMapping("/wallet")
public class WalletController {
    private final WalletDao walletDao;

    public WalletController(WalletDao walletDao) {
        this.walletDao = walletDao;
    }

    @PostMapping("/{walletId}")
    public ApiResponse<Wallet> get(@PathVariable("walletId") Long walletId) throws SQLException, InterruptedException {
        return walletDao.getWallet(walletId)
                .map(ApiResponse::success)
                .orElseGet(() -> ApiResponse.badRequest("Wallet not found"));
    }
}
