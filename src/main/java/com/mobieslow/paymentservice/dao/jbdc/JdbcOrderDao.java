package com.mobieslow.paymentservice.dao.jbdc;

import com.mobieslow.paymentservice.dao.OrderDao;
import com.mobieslow.paymentservice.models.Order;
import com.mobieslow.paymentservice.service.JdbcService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Repository;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Optional;

@Repository
public class JdbcOrderDao implements OrderDao {
    private static final Logger logger = LoggerFactory.getLogger(JdbcOrderDao.class);

    private final JdbcService jdbcService;

    final String getOrderQuery = "SELECT * from orders where id=?";

    public JdbcOrderDao(JdbcService jdbcService) {
        this.jdbcService = jdbcService;
    }

    @Override
    public Optional<Order> get(String orderId) {
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

        } catch (Exception ex) {
            logger.error("Unable fetch order: ", ex);
        } finally {
            jdbcService.releaseConnection(conn);
        }

        return Optional.empty();
    }

    @Override
    public void save(Order order) {

    }

    private Order orderRowMapper(ResultSet rs) throws SQLException {
        Order order = new Order();
        order.setId(rs.getString("id"));
        order.setAmount(rs.getDouble("amount"));
        return order;
    }
}
