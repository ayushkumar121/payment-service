package com.mobieslow.paymentservice.dao;

import com.mobieslow.paymentservice.models.Order;

import java.sql.SQLException;
import java.util.Optional;

public interface OrderDao {
    Optional<Order> get(String orderId) throws SQLException, InterruptedException;
    void save(Order order) throws SQLException, InterruptedException;
}
