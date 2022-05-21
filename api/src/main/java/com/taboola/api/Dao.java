package com.taboola.api;

import org.springframework.stereotype.Component;

import java.sql.*;
import java.util.HashMap;
import java.util.Map;
import java.util.Properties;

@Component
public class Dao {

    private final String URL = "jdbc:hsqldb:hsql://localhost/xdb";
    private final String USER = "sa";
    private final String PASSWORD = "";
    Connection conn = null;
    PreparedStatement stmt = null;

    Properties prop = new Properties();

    public Map<Integer, Integer> getEventsByTime(Timestamp timestamp) {
        Map<Integer, Integer> result = new HashMap<>();
        try {
            conn = DriverManager.getConnection( URL, USER, PASSWORD);

            System.out.println(prop.getProperty("url"));

            String sql = "SELECT * FROM Events WHERE TIME_BUCKET=?";
            stmt = conn.prepareStatement(sql);

            stmt.setTimestamp(1, timestamp);

            ResultSet set = stmt.executeQuery();
            while (set.next()) {
                result.put(set.getInt(2), set.getInt(4));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            closeConnection(conn, stmt);
        }
        return result;
    }
    public Integer getEventCountsByTimeAndEventId(Timestamp timestamp, Integer eventId) {
        try {
            conn = DriverManager.getConnection( URL, USER, PASSWORD);

            String sql = "SELECT * FROM Events WHERE TIME_BUCKET=? AND EVENT_ID=?";
            stmt = conn.prepareStatement(sql);

            stmt.setTimestamp(1, timestamp);
            stmt.setInt(2, eventId);

            ResultSet set = stmt.executeQuery();
            if (set.next()) {
                return set.getInt(4);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            closeConnection(conn, stmt);
        }
        return null;
    }

    public Map<Timestamp, Integer> getEventsByEventId(Integer eventId) {
        Map<Timestamp, Integer> result = new HashMap<>();
        try {
            conn = DriverManager.getConnection( URL, USER, PASSWORD);

            String sql = "SELECT * FROM Events WHERE event_id=?";
            stmt = conn.prepareStatement(sql);

            stmt.setInt(1, eventId);

            ResultSet set = stmt.executeQuery();
            while (set.next()) {
                result.put(set.getTimestamp(3), set.getInt(4));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            closeConnection(conn, stmt);
        }
        return result;
    }

    private void closeConnection(Connection conn, PreparedStatement stmt) {
        try {
            if (conn != null) {
                assert stmt != null;
                stmt.close();
            }
            if (stmt != null) {
                assert conn != null;
                conn.close();
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }
}

