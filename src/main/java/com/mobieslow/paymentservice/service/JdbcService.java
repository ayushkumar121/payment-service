package com.mobieslow.paymentservice.service;

import jakarta.annotation.PostConstruct;
import jakarta.annotation.PreDestroy;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;

@Service
public class JdbcService {
    @Value("${jdbc.datasource.url}")
    private String dataSourceUrl;

    @Value("${jdbc.datasource.username}")
    private String dataSourceUsername;

    @Value("${jdbc.datasource.password}")
    private String dataSourcePassword;

    @Value("${jdbc.datasource.pool.size:1}")
    private int poolSize;

    private BlockingQueue<Connection> connectionPool;

    @PostConstruct
    public void init() {
        connectionPool = new LinkedBlockingQueue<>(poolSize);
        try {
            for (int i = 0; i < poolSize; i++) {
                connectionPool.add(DriverManager.getConnection(dataSourceUrl, dataSourceUsername, dataSourcePassword));
            }
        } catch (Exception ex) {
            throw new RuntimeException("Error initializing connection pool", ex);
        }
    }

    public Connection getConnection() throws InterruptedException {
        return connectionPool.take();
    }

    public void releaseConnection(Connection connection) {
        if (connection != null) {
            connectionPool.offer(connection);
        }
    }

    @PreDestroy
    public void destroy() {
        while (!connectionPool.isEmpty()) {
            try {
                connectionPool.poll().close();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
    }
}
