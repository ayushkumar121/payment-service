package com.mobieslow.paymentservice.service;

import com.mobieslow.paymentservice.dao.OrderDao;
import com.mobieslow.paymentservice.dao.WalletDao;
import com.mobieslow.paymentservice.utils.Context;
import com.mobieslow.paymentservice.utils.Result;
import com.mobieslow.paymentservice.models.Customer;
import com.mobieslow.paymentservice.models.Merchant;
import com.mobieslow.paymentservice.models.Order;
import com.mobieslow.paymentservice.models.Wallet;
import org.springframework.stereotype.Service;

@Service
public class PaymentService {
    private final WalletDao walletDao;
    private final OrderDao orderDao;

    public PaymentService(WalletDao walletDao, OrderDao orderDao) {
        this.walletDao = walletDao;
        this.orderDao = orderDao;
    }

    public record InitiateResponse(
            String orderId,
            Order.Status status,
            String redirectUrl
    ) {}

    public Result<InitiateResponse, String> initiate(Context ctx, Order order) {
        /* Validations
            1. Deduplication of order
            2. ....
            // Insert order into orders table
            // (external calls for )
        */

        if (orderDao.get(order.getId()).isPresent()) {
            return Result.err("duplicate order");
        }

        Customer customer = new Customer();
        Merchant merchant = new Merchant();

        orderDao.save(order);

        if (Order.PaymentInstrument.WALLET.equals(order.getPaymentInstrument())) {
            Wallet userWallet = walletDao.getWallet(customer.getWalletId()).orElseThrow();

            if (userWallet.getBalance() < order.getAmount()) {
                return Result.err("insufficient balance");
            }

            try {
                walletDao.transferFunds(customer.getWalletId(), merchant.getWalletId(), order.getAmount());
                order.setStatus(Order.Status.SUCCESS);
            }  catch (Exception ex) {
                order.setStatus(Order.Status.FAILED);
            } finally {
                orderDao.save(order);
            }

            return Result.ok(new InitiateResponse(
                    order.getId(),
                    order.getStatus(),
                    null
            ));
        } else {
            // external PSP calls
            // return url
            return Result.err("not implemented");
        }
    }

    public record CheckStatusResponse(String orderId, Order.Status status) {}

    public Result<CheckStatusResponse, String> checkStatus(Context ctx, String orderId) {
        return Result.err("not implemented");
    }
}
