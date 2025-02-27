package org.leon.astonhome.repository;

import org.leon.astonhome.connect.DBConnection;
import org.leon.astonhome.entity.Game;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class GameRepository {

    private static final String SQL_INSERT = "INSERT INTO games(title, developer_id, release_year, genre) VALUES (?, ?, ?, ?) RETURNING game_id";
    private static final String SQL_SELECT_BY_ID = "SELECT * FROM games WHERE game_id = ?";
    private static final String SQL_UPDATE = "UPDATE games SET title = ?, developer_id = ?, release_year = ?, genre = ? WHERE game_id = ?";
    private static final String SQL_DELETE = "DELETE FROM games WHERE game_id = ?";
    private static final String SQL_SELECT_ALL = "SELECT * FROM games";

    public Game create(Game game) {
        return DBConnection.executePreparedStatement(SQL_INSERT,
                stmt -> fillPreparedStatementForGame(stmt, game, false),
                stmt -> {
                    try (ResultSet rs = stmt.executeQuery()) {
                        if (rs.next()) {
                            game.setGameId(rs.getInt("game_id"));
                        }
                    }
                    return game;
                });
    }

    public Game getById(int id) {
        return DBConnection.executePreparedStatement(SQL_SELECT_BY_ID,
                stmt -> stmt.setInt(1, id),
                stmt -> {
                    try (ResultSet rs = stmt.executeQuery()) {
                        return rs.next() ? mapResultSetToGame(rs) : null;
                    }
                });
    }

    public boolean update(Game game) {
        return DBConnection.executePreparedStatement(SQL_UPDATE,
                stmt -> fillPreparedStatementForGame(stmt, game, true),
                stmt -> stmt.executeUpdate() > 0);
    }

    public boolean delete(int id) {
        return DBConnection.executePreparedStatement(SQL_DELETE,
                stmt -> stmt.setInt(1, id),
                stmt -> stmt.executeUpdate() > 0);
    }

    public List<Game> getAll() {
        return DBConnection.executePreparedStatement(SQL_SELECT_ALL,
                stmt -> {},
                stmt -> {
                    try (ResultSet rs = stmt.executeQuery()) {
                        List<Game> games = new ArrayList<>();
                        while (rs.next()) {
                            games.add(mapResultSetToGame(rs));
                        }
                        return games;
                    }
                });
    }

    private void fillPreparedStatementForGame(PreparedStatement stmt, Game game, boolean isUpdate) throws SQLException {
        stmt.setString(1, game.getTitle());
        stmt.setObject(2, game.getDeveloperId(), java.sql.Types.INTEGER);
        stmt.setObject(3, game.getReleaseYear(), java.sql.Types.INTEGER);
        stmt.setString(4, game.getGenre());
        if (isUpdate) {
            stmt.setInt(5, game.getGameId());
        }
    }

    private Game mapResultSetToGame(ResultSet rs) throws SQLException {
        Game game = new Game();
        game.setGameId(rs.getInt("game_id"));
        game.setTitle(rs.getString("title"));
        game.setDeveloperId((Integer) rs.getObject("developer_id"));
        game.setReleaseYear((Integer) rs.getObject("release_year"));
        game.setGenre(rs.getString("genre"));
        return game;
    }
}