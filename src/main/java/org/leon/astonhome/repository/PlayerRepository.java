package org.leon.astonhome.repository;

import org.leon.astonhome.connect.DBConnection;
import org.leon.astonhome.entity.Player;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class PlayerRepository {

    private static final String SQL_INSERT = "INSERT INTO players(name, email) VALUES (?, ?) RETURNING player_id";
    private static final String SQL_SELECT_BY_ID = "SELECT * FROM players WHERE player_id = ?";
    private static final String SQL_UPDATE = "UPDATE players SET name = ?, email = ? WHERE player_id = ?";
    private static final String SQL_DELETE = "DELETE FROM players WHERE player_id = ?";
    private static final String SQL_SELECT_ALL = "SELECT * FROM players";

    public Player create(Player player) {
        return DBConnection.executePreparedStatement(SQL_INSERT,
                stmt -> fillPreparedStatementForPlayer(stmt, player, false),
                stmt -> {
                    try (ResultSet rs = stmt.executeQuery()) {
                        if (rs.next()) {
                            player.setPlayerId(rs.getInt("player_id"));
                        }
                    }
                    return player;
                });
    }

    public Player getById(int id) {
        return DBConnection.executePreparedStatement(SQL_SELECT_BY_ID,
                stmt -> stmt.setInt(1, id),
                stmt -> {
                    try (ResultSet rs = stmt.executeQuery()) {
                        return rs.next() ? mapResultSetToPlayer(rs) : null;
                    }
                });
    }

    public boolean update(Player player) {
        return DBConnection.executePreparedStatement(SQL_UPDATE,
                stmt -> fillPreparedStatementForPlayer(stmt, player, true),
                stmt -> stmt.executeUpdate() > 0);
    }

    public boolean delete(int id) {
        return DBConnection.executePreparedStatement(SQL_DELETE,
                stmt -> stmt.setInt(1, id),
                stmt -> stmt.executeUpdate() > 0);
    }

    public List<Player> getAll() {
        return DBConnection.executePreparedStatement(SQL_SELECT_ALL,
                stmt -> {},
                stmt -> {
                    try (ResultSet rs = stmt.executeQuery()) {
                        List<Player> players = new ArrayList<>();
                        while (rs.next()) {
                            players.add(mapResultSetToPlayer(rs));
                        }
                        return players;
                    }
                });
    }

    private void fillPreparedStatementForPlayer(PreparedStatement stmt, Player player, boolean isUpdate) throws SQLException {
        stmt.setString(1, player.getName());
        stmt.setString(2, player.getEmail());
        if (isUpdate) {
            stmt.setInt(3, player.getPlayerId());
        }
    }

    private Player mapResultSetToPlayer(ResultSet rs) throws SQLException {
        Player player = new Player();
        player.setPlayerId(rs.getInt("player_id"));
        player.setName(rs.getString("name"));
        player.setEmail(rs.getString("email"));
        player.setRegistrationDate(rs.getString("registration_date"));
        return player;
    }
}
