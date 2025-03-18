package com.mobieslow.paymentservice.service;

import com.mobieslow.paymentservice.dao.CustomerDao;
import com.mobieslow.paymentservice.dao.MerchantDao;
import com.mobieslow.paymentservice.dao.OrderDao;
import com.mobieslow.paymentservice.dao.WalletDao;
import com.mobieslow.paymentservice.exceptions.ServiceException;
import com.mobieslow.paymentservice.models.*;
import com.mobieslow.paymentservice.utils.EncryptionUtils;
import com.mobieslow.paymentservice.utils.Result;
import org.springframework.stereotype.Service;

@Service
public class PaymentService {
    private final MerchantDao merchantDao;
    private final CustomerDao customerDao;
    private final WalletDao walletDao;
    private final OrderDao orderDao;

    public PaymentService(MerchantDao merchantDao, CustomerDao customerDao, WalletDao walletDao, OrderDao orderDao) {
        this.merchantDao = merchantDao;
        this.customerDao = customerDao;
        this.walletDao = walletDao;
        this.orderDao = orderDao;
    }

    public record InitiateResponse(
            String orderId,
            TransactionStatus status,
            String redirectUrl
    ) {}

    public InitiateResponse initiate(String mid, String encryptedRequest) throws Exception {
        Merchant merchant = merchantDao.get(mid)
                .orElseThrow(() -> new ServiceException("Invalid merchant"));

        Order order = EncryptionUtils.decryptObject(encryptedRequest, merchant.getSecretKey(), Order.class)
                .orElseThrow(err -> new ServiceException("Invalid order:"+err));

        Customer customer = customerDao.get(order.getCustomerId())
                .orElseThrow(() -> new ServiceException("Invalid customer"));

        orderDao.get(order.getId())
                .ifPresent(o -> { throw new ServiceException("Duplicate order"); });

        order.setStatus(TransactionStatus.PENDING);
        orderDao.save(order);

        if (PaymentInstrument.WALLET.equals(order.getPaymentInstrument())) {
            try {
                walletDao.transferFunds(customer.getWalletId(), merchant.getWalletId(), order.getAmount());
                order.setStatus(TransactionStatus.SUCCESS);
            }  catch (Exception ex) {
                order.setStatus(TransactionStatus.FAILURE);
                throw new ServiceException("Insufficient balance");
            } finally {
                orderDao.save(order);
            }

            return new InitiateResponse(
                    order.getId(),
                    order.getStatus(),
                    null
            );
        } else {
            // external PSP calls
            // return url
            throw new ServiceException("Not implemented");
        }
    }

    public record CheckStatusResponse(String orderId, TransactionStatus status) {}

    public CheckStatusResponse checkStatus(String mid, String encryptedOrderId) throws Exception {
        Merchant merchant = merchantDao.get(mid)
                .orElseThrow(() -> new ServiceException("Invalid merchant"));

        String orderId = EncryptionUtils.decryptObject(encryptedOrderId, merchant.getSecretKey(), String.class)
                .orElseThrow(err -> new ServiceException("Invalid order:"+err));

        Order order = orderDao.get(orderId)
                .orElseThrow(() -> new ServiceException("Invalid order"));

        return new CheckStatusResponse(orderId, order.getStatus());
    }
}
