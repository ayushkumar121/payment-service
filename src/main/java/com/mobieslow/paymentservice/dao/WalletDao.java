package com.mobieslow.paymentservice.dao;

import com.mobieslow.paymentservice.models.Wallet;

import java.util.Optional;

public interface WalletDao {
    Optional<Wallet> getWallet(long walletId);
    void transferFunds(long sourceWalletId, long targetWalletId, double amount);
}
