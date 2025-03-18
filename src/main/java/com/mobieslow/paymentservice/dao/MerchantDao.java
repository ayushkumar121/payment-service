package com.mobieslow.paymentservice.dao;

import com.mobieslow.paymentservice.models.Merchant;

import java.sql.SQLException;
import java.util.Optional;

public interface MerchantDao {
    Optional<Merchant> get(String merchantId) throws InterruptedException, SQLException;
    void save(Merchant merchant) throws SQLException, InterruptedException;
}
