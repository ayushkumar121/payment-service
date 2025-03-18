package com.mobieslow.paymentservice.service;

import com.mobieslow.paymentservice.utils.Result;
import org.springframework.stereotype.Service;

@Service
public class PayoutService {
    public record PayoutResponse() {}

    public Result<PayoutResponse, String> payout(String walletId, Double amount) throws Exception {
        return Result.err("not implemented");
    }
}