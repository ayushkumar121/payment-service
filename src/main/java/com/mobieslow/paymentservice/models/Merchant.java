package com.mobieslow.paymentservice.models;

import lombok.Data;

@Data
public class Merchant {
    private String id;
    private String secretKey;
    private Long walletId;
}
