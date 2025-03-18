package com.mobieslow.paymentservice.models;

import lombok.Data;

@Data
public class Ledger {
    private Long walletId;
    private Double creditAmount;
    private Double debitAmount;
}
