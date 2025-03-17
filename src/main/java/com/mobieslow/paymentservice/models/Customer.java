package com.mobieslow.paymentservice.models;

import lombok.Data;

@Data
public class Customer {
    private String id;
    private String name;
    private String email;
    private String mobile;
    private Long walletId;
}
