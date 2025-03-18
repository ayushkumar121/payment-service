package com.mobieslow.paymentservice.dao.jbdc;

import com.mobieslow.paymentservice.dao.CustomerDao;
import com.mobieslow.paymentservice.models.Customer;
import com.mobieslow.paymentservice.service.JdbcService;
import org.springframework.stereotype.Repository;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Optional;

@Repository
public class JdbcCustomerDao implements CustomerDao {
    private final JdbcService jdbcService;

    final String getCustomerQuery = "SELECT * from customers where id=?";
    final String saveCustomerQuery = "INSERT INTO customers (id, name, email, mobile, wallet_id) " +
            "VALUES (?, ?, ?, ?, ?)" +
            "ON CONFLICT (id) DO UPDATE" +
            "   SET name = excluded.name," +
            "       email = excluded.email," +
            "       mobile = excluded.mobile," +
            "       wallet_id = excluded.wallet_id," +
            "       updated_at = current_timestamp";

    public JdbcCustomerDao(JdbcService jdbcService) {
        this.jdbcService = jdbcService;
    }

    @Override
    public Optional<Customer> get(String customerId) throws InterruptedException, SQLException {
        Connection conn = null;
        try {
            conn = jdbcService.getConnection();

            try (PreparedStatement stmt = conn.prepareStatement(getCustomerQuery)) {
                stmt.setString(1, customerId);
                ResultSet rs = stmt.executeQuery();
                if (rs.next()) {
                    return Optional.of(customerRowMapper(rs));
                }
            }

        } finally {
            jdbcService.releaseConnection(conn);
        }

        return Optional.empty();
    }

    @Override
    public void save(Customer customer) throws SQLException, InterruptedException {
        Connection conn = null;
        try {
            conn = jdbcService.getConnection();

            try (PreparedStatement stmt = conn.prepareStatement(saveCustomerQuery)) {
                stmt.setString(1, customer.getId());
                stmt.setString(2, customer.getName());
                stmt.setString(3, customer.getEmail());
                stmt.setString(4, customer.getMobile());
                stmt.setLong(5, customer.getWalletId());
                stmt.execute();
            }

        } finally {
            jdbcService.releaseConnection(conn);
        }
    }

    private Customer customerRowMapper(ResultSet rs) throws SQLException {
        Customer customer = new Customer();
        customer.setId(rs.getString("id"));
        customer.setName(rs.getString("name"));
        customer.setEmail(rs.getString("email"));
        customer.setMobile(rs.getString("mobile"));
        customer.setWalletId(rs.getLong("wallet_id"));
        return customer;
    }
}
