package org.leon.astonhome.repository;

import org.leon.astonhome.connect.DBConnection;
import org.leon.astonhome.entity.Purchase;
import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;

public class PurchaseRepository {

    private static final String SQL_INSERT = "INSERT INTO purchases(game_id, player_id, purchase_date) VALUES (?, ?, ?) RETURNING purchase_id, purchase_date";
    private static final String SQL_SELECT_BY_ID = "SELECT * FROM purchases WHERE purchase_id = ?";
    private static final String SQL_UPDATE = "UPDATE purchases SET game_id = ?, player_id = ?, purchase_date = ? WHERE purchase_id = ?";
    private static final String SQL_DELETE = "DELETE FROM purchases WHERE purchase_id = ?";
    private static final String SQL_SELECT_ALL = "SELECT * FROM purchases";

    private String formatTimestamp(Timestamp timestamp) {
        if (timestamp == null) {
            return null;
        }
        return new SimpleDateFormat("yyyy-MM-dd").format(new java.util.Date(timestamp.getTime()));
    }

    private java.sql.Date parseDate(String date) {
        if (date == null || date.isEmpty()) {
            return null;
        }
        try {
            return new java.sql.Date(new SimpleDateFormat("yyyy-MM-dd").parse(date).getTime());
        } catch (ParseException e) {
            throw new IllegalArgumentException("Invalid date format: " + date);
        }
    }

    public Purchase create(Purchase purchase) {
        return DBConnection.executePreparedStatement(SQL_INSERT,
                stmt -> fillPreparedStatementForPurchase(stmt, purchase, false),
                stmt -> {
                    try (ResultSet rs = stmt.executeQuery()) {
                        if (rs.next()) {
                            purchase.setPurchaseId(rs.getInt("purchase_id"));
                            purchase.setPurchaseDate(formatTimestamp(rs.getTimestamp("purchase_date")));
                        }
                    }
                    return purchase;
                });
    }

    public Purchase getById(int id) {
        return DBConnection.executePreparedStatement(SQL_SELECT_BY_ID,
                stmt -> stmt.setInt(1, id),
                stmt -> {
                    try (ResultSet rs = stmt.executeQuery()) {
                        if (rs.next()) {
                            return mapResultSetToPurchase(rs);
                        }
                    }
                    return null;
                });
    }

    public boolean update(Purchase purchase) {
        return DBConnection.executePreparedStatement(SQL_UPDATE,
                stmt -> fillPreparedStatementForPurchase(stmt, purchase, true),
                stmt -> stmt.executeUpdate() > 0);
    }

    public boolean delete(int id) {
        return DBConnection.executePreparedStatement(SQL_DELETE,
                stmt -> stmt.setInt(1, id),
                stmt -> stmt.executeUpdate() > 0);
    }

    public List<Purchase> getAll() {
        return DBConnection.executePreparedStatement(SQL_SELECT_ALL,
                stmt -> {},
                stmt -> {
                    try (ResultSet rs = stmt.executeQuery()) {
                        List<Purchase> purchases = new ArrayList<>();
                        while (rs.next()) {
                            purchases.add(mapResultSetToPurchase(rs));
                        }
                        return purchases;
                    }
                });
    }

    private void fillPreparedStatementForPurchase(PreparedStatement stmt, Purchase purchase, boolean isUpdate) throws SQLException {
        stmt.setInt(1, purchase.getGameId());
        stmt.setInt(2, purchase.getPlayerId());
        stmt.setDate(3, parseDate(purchase.getPurchaseDate()));

        if (isUpdate) {
            stmt.setInt(4, purchase.getPurchaseId());
        }
    }

    private Purchase mapResultSetToPurchase(ResultSet rs) throws SQLException {
        Purchase purchase = new Purchase();
        purchase.setPurchaseId(rs.getInt("purchase_id"));
        purchase.setGameId(rs.getInt("game_id"));
        purchase.setPlayerId(rs.getInt("player_id"));
        purchase.setPurchaseDate(formatTimestamp(rs.getTimestamp("purchase_date")));

        return purchase;
    }
}