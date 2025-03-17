package com.mobieslow.paymentservice.models;

import lombok.Data;

@Data
public class Ledger {
    private String walletId;
    private Double creditAmount;
    private Double debitAmount;
}
