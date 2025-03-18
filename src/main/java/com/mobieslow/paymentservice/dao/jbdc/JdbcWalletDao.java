package com.mobieslow.paymentservice.dao.jbdc;

import com.mobieslow.paymentservice.dao.WalletDao;
import com.mobieslow.paymentservice.models.Wallet;
import com.mobieslow.paymentservice.service.JdbcService;
import org.springframework.stereotype.Repository;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Optional;

@Repository
public class JdbcWalletDao implements WalletDao {
    private final JdbcService jdbcService;

    final String getWalletQuery = "SELECT * from wallets where id=?";
    final String debitQuery = "UPDATE wallets SET balance = balance - ? WHERE id = ? and balance >= ?";
    final String creditQuery = "UPDATE wallets SET balance = balance + ? WHERE id = ?";

    public JdbcWalletDao(JdbcService jdbcService) {
        this.jdbcService = jdbcService;
    }

    @Override
    public Optional<Wallet> getWallet(long walletId) throws SQLException, InterruptedException {
        Connection conn = null;
        try {
            conn = jdbcService.getConnection();

            try (PreparedStatement stmt = conn.prepareStatement(getWalletQuery)) {
                stmt.setLong(1, walletId);
                ResultSet rs = stmt.executeQuery();
                if (rs.next()) {
                    return Optional.of(walletRowMapper(rs));
                }
            }

        } finally {
            jdbcService.releaseConnection(conn);
        }
        return Optional.empty();
    }

    @Override
    public void transferFunds(long sourceWalletId, long targetWalletId, double amount) throws SQLException, InterruptedException {
        assert sourceWalletId != targetWalletId;

        Connection conn = null;
        try {
            conn = jdbcService.getConnection();
            conn.setAutoCommit(false);

            try (PreparedStatement stmt = conn.prepareStatement(debitQuery)) {
                stmt.setDouble(1, amount);
                stmt.setLong(2, sourceWalletId);
                stmt.setDouble(3, amount);
                int rowsAffected = stmt.executeUpdate();
                if (rowsAffected == 0) throw new SQLException("Source wallet not found or not enough balance");
            }

            try (PreparedStatement stmt = conn.prepareStatement(creditQuery)) {
                stmt.setDouble(1, amount);
                stmt.setLong(2, targetWalletId);
                int rowsAffected = stmt.executeUpdate();
                if (rowsAffected == 0) throw new SQLException("Target wallet not found");
            }

            conn.commit();
        } catch (SQLException | InterruptedException ex) {
            if (conn != null) conn.rollback();
            throw ex;
        }  finally {
            if (conn != null) conn.setAutoCommit(true);
            if (conn != null) jdbcService.releaseConnection(conn);
        }
    }

    private Wallet walletRowMapper(ResultSet rs) throws SQLException {
        Wallet wallet = new Wallet();
        wallet.setId(rs.getLong("id"));
        wallet.setBalance(rs.getDouble("balance"));
        wallet.setCurrency(rs.getString("currency"));
        return wallet;
    }
}
