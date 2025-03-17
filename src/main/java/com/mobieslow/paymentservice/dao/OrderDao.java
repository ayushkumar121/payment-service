package com.mobieslow.paymentservice.dao;

import com.mobieslow.paymentservice.models.Order;

import java.util.Optional;

public interface OrderDao {
    Optional<Order> get(String orderId);
    void save(Order order);
}
