package com.mobieslow.paymentservice.dao;

import com.mobieslow.paymentservice.models.Customer;

import java.sql.SQLException;
import java.util.Optional;

public interface CustomerDao {
    Optional<Customer> get(String customerId) throws InterruptedException, SQLException;
    void save(Customer customer) throws SQLException, InterruptedException;
}
