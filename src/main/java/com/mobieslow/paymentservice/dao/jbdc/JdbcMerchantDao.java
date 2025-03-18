package com.mobieslow.paymentservice.dao.jbdc;

import com.mobieslow.paymentservice.dao.MerchantDao;
import com.mobieslow.paymentservice.models.Merchant;
import com.mobieslow.paymentservice.service.JdbcService;
import org.springframework.stereotype.Repository;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Optional;

@Repository
public class JdbcMerchantDao implements MerchantDao {
    private final JdbcService jdbcService;

    final String getMerchantQuery = "SELECT * from merchants where id=?";
    final String saveMerchantQuery = "INSERT INTO merchants (id, secret_key, wallet_id) " +
            "VALUES (?, ?, ?)" +
            "ON CONFLICT (id) DO UPDATE" +
            "   SET secret_key = excluded.secret_key," +
            "       wallet_id = excluded.wallet_id," +
            "       updated_at = current_timestamp";

    public JdbcMerchantDao(JdbcService jdbcService) {
        this.jdbcService = jdbcService;
    }

    @Override
    public Optional<Merchant> get(String merchantId) throws InterruptedException, SQLException {
        Connection conn = null;
        try {
            conn = jdbcService.getConnection();

            try (PreparedStatement stmt = conn.prepareStatement(getMerchantQuery)) {
                stmt.setString(1, merchantId);
                ResultSet rs = stmt.executeQuery();
                if (rs.next()) {
                    return Optional.of(merchantRowMapper(rs));
                }
            }

        } finally {
            jdbcService.releaseConnection(conn);
        }

        return Optional.empty();
    }

    @Override
    public void save(Merchant merchant) throws SQLException, InterruptedException {
        Connection conn = null;
        try {
            conn = jdbcService.getConnection();

            try (PreparedStatement stmt = conn.prepareStatement(saveMerchantQuery)) {
                stmt.setString(1, merchant.getId());
                stmt.setString(2, merchant.getSecretKey());
                stmt.setLong(2, merchant.getWalletId());
                stmt.execute();
            }

        } finally {
            jdbcService.releaseConnection(conn);
        }
    }

    private Merchant merchantRowMapper(ResultSet rs) throws SQLException {
        Merchant merchant = new Merchant();
        merchant.setId(rs.getString("id"));
        merchant.setSecretKey(rs.getString("secret_key"));
        merchant.setWalletId(rs.getLong("wallet_id"));
        return merchant;
    }
}
