package com.mobieslow.paymentservice.models;

import lombok.Data;

@Data
public class Order {
    public enum Status {
        PENDING,
        SUCCESS,
        FAILED
    }

    public enum PaymentInstrument {
        WALLET,
        CARD
    }

    private String id;
    private Double amount;
    private String merchantId;
    private String customerId;
    private Status status;
    private PaymentInstrument paymentInstrument;
    private String redirectUrl;
}

