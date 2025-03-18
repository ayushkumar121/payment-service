package com.mobieslow.paymentservice.dao;

import com.mobieslow.paymentservice.models.Wallet;

import java.sql.SQLException;
import java.util.Optional;

public interface WalletDao {
    Optional<Wallet> getWallet(long walletId) throws SQLException, InterruptedException;
    void transferFunds(long sourceWalletId, long targetWalletId, double amount) throws SQLException, InterruptedException;
}
