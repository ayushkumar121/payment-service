package com.mobieslow.paymentservice.dao.jbdc;

import com.mobieslow.paymentservice.dao.OrderDao;
import com.mobieslow.paymentservice.models.Order;
import com.mobieslow.paymentservice.service.JdbcService;
import org.springframework.stereotype.Repository;

import java.sql.*;
import java.util.Optional;

@Repository
public class JdbcOrderDao implements OrderDao {
    private final JdbcService jdbcService;

    final String getOrderQuery = "SELECT * from orders where id=?";
    final String saveOrderQuery = "INSERT INTO orders (id, amount, customer_id, merchant_id, status) " +
            "VALUES (?, ?, ?, ?, ?)" +
            "ON CONFLICT (id) DO UPDATE" +
            "   SET status = excluded.status," +
            "       customer_id = excluded.customer_id," +
            "       merchant_id = excluded.merchant_id," +
            "       amount = excluded.amount," +
            "       updated_at = current_timestamp";

    public JdbcOrderDao(JdbcService jdbcService) {
        this.jdbcService = jdbcService;
    }

    @Override
    public Optional<Order> get(String orderId) throws SQLException, InterruptedException {
        Connection conn = null;
        try {
            conn = jdbcService.getConnection();

            try (PreparedStatement stmt = conn.prepareStatement(getOrderQuery)) {
                stmt.setString(1, orderId);
                ResultSet rs = stmt.executeQuery();
                if (rs.next()) {
                    return Optional.of(orderRowMapper(rs));
                }
            }

        } finally {
            jdbcService.releaseConnection(conn);
        }

        return Optional.empty();
    }

    @Override
    public void save(Order order) throws SQLException, InterruptedException {
        Connection conn = null;
        try {
            conn = jdbcService.getConnection();

            try (PreparedStatement stmt = conn.prepareStatement(saveOrderQuery)) {
                stmt.setString(1, order.getId());
                stmt.setDouble(2, order.getAmount());
                stmt.setString(3, order.getCustomerId());
                stmt.setString(4, order.getMerchantId());
                stmt.setObject(5, order.getStatus(), java.sql.Types.OTHER);
                stmt.execute();
            }

        } finally {
            jdbcService.releaseConnection(conn);
        }
    }

    private Order orderRowMapper(ResultSet rs) throws SQLException {
        Order order = new Order();
        order.setId(rs.getString("id"));
        order.setAmount(rs.getDouble("amount"));
        return order;
    }
}
