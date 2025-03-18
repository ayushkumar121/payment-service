package com.mobieslow.paymentservice.models;

import lombok.Data;

@Data
public class Order {
    private String id;
    private Double amount;
    private String merchantId;
    private String customerId;
    private TransactionStatus status;
    private PaymentInstrument paymentInstrument;
    private String redirectUrl;
}

