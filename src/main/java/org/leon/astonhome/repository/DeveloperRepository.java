package org.leon.astonhome.repository;

import org.leon.astonhome.connect.DBConnection;
import org.leon.astonhome.entity.Developer;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Types;
import java.util.ArrayList;
import java.util.List;

public class DeveloperRepository {

    private static final String SQL_INSERT = "INSERT INTO developers(name, foundation_year) VALUES (?, ?) RETURNING developer_id";
    private static final String SQL_SELECT_BY_ID = "SELECT * FROM developers WHERE developer_id = ?";
    private static final String SQL_UPDATE = "UPDATE developers SET name = ?, foundation_year = ? WHERE developer_id = ?";
    private static final String SQL_DELETE = "DELETE FROM developers WHERE developer_id = ?";
    private static final String SQL_SELECT_ALL = "SELECT * FROM developers";

    public Developer create(Developer developer) {
        return DBConnection.executePreparedStatement(SQL_INSERT,
                stmt -> fillPreparedStatementForDeveloper(stmt, developer, false),
                stmt -> {
                    try (ResultSet rs = stmt.executeQuery()) {
                        if (rs.next()) {
                            developer.setDeveloperId(rs.getInt("developer_id"));
                        }
                    }
                    return developer;
                });
    }

    public Developer getById(int id) {
        return DBConnection.executePreparedStatement(SQL_SELECT_BY_ID,
                stmt -> stmt.setInt(1, id),
                stmt -> {
                    try (ResultSet rs = stmt.executeQuery()) {
                        return rs.next() ? mapResultSetToDeveloper(rs) : null;
                    }
                });
    }

    public boolean update(Developer developer) {
        return DBConnection.executePreparedStatement(SQL_UPDATE,
                stmt -> fillPreparedStatementForDeveloper(stmt, developer, true),
                stmt -> stmt.executeUpdate() > 0);
    }

    public boolean delete(int id) {
        return DBConnection.executePreparedStatement(SQL_DELETE,
                stmt -> stmt.setInt(1, id),
                stmt -> stmt.executeUpdate() > 0);
    }

    public List<Developer> getAll() {
        return DBConnection.executePreparedStatement(SQL_SELECT_ALL,
                stmt -> {},
                stmt -> {
                    try (ResultSet rs = stmt.executeQuery()) {
                        List<Developer> developers = new ArrayList<>();
                        while (rs.next()) {
                            developers.add(mapResultSetToDeveloper(rs));
                        }
                        return developers;
                    }
                });
    }

    private void fillPreparedStatementForDeveloper(PreparedStatement stmt, Developer developer, boolean isUpdate) throws SQLException {
        stmt.setString(1, developer.getName());
        stmt.setObject(2, developer.getFoundationYear(), Types.INTEGER);
        if (isUpdate) {
            stmt.setInt(3, developer.getDeveloperId());
        }
    }

    private Developer mapResultSetToDeveloper(ResultSet rs) throws SQLException {
        Developer developer = new Developer();
        developer.setDeveloperId(rs.getInt("developer_id"));
        developer.setName(rs.getString("name"));
        developer.setFoundationYear((Integer) rs.getObject("foundation_year"));
        return developer;
    }
}
