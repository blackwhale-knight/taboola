package com.taboola.spark;

import com.zaxxer.hikari.HikariConfig;
import com.zaxxer.hikari.HikariDataSource;

import java.sql.Connection;
import java.sql.SQLException;

public class Config {
    private static final HikariConfig config = new HikariConfig();
    private static final HikariDataSource dataSource;

    static {
        config.setJdbcUrl("jdbc:hsqldb:hsql://localhost/xdb");
        config.setUsername("sa");
        config.setPassword("");
        config.setDriverClassName("org.hsqldb.jdbc.JDBCDriver");
        dataSource = new HikariDataSource(config);
    }

    private Config() {}

    public static Connection getConnection() throws SQLException {
        return dataSource.getConnection();
    }
}
