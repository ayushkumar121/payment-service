package com.mobieslow.paymentservice.models;

import lombok.Data;

@Data
public class Wallet {
    private Long id;
    private Double balance;
    private String currency;
}
