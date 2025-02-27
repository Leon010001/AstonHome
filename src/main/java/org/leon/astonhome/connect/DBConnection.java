package org.leon.astonhome.connect;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.SQLException;

public class DBConnection {
    private static final String URL = "jdbc:postgresql://localhost:5432/postgres";
    private static final String USER = "postgres";
    private static final String PASSWORD = "12345";

    static {
        try {
            Class.forName("org.postgresql.Driver");
        } catch (ClassNotFoundException e) {
            throw new RuntimeException("Не удалось загрузить драйвер PostgreSQL", e);
        }
    }

    private DBConnection() {
        throw new UnsupportedOperationException("Не поддерживается создание экземпляров класса DBConnection");
    }

    public static Connection getConnection() throws SQLException {

        return DriverManager.getConnection(URL, USER, PASSWORD);
    }

    public static <T> T executeWithConnection(SQLFunction<Connection, T> function) {
        try (Connection connection = getConnection()) {
            return function.apply(connection);
        } catch (SQLException e) {
            throw new RuntimeException("Ошибка при выполнении запроса к БД", e);
        }
    }

    public static <R> R executePreparedStatement(String sql, SQLConsumer<PreparedStatement> parameterSetter,
                                                 SQLFunction<PreparedStatement, R> executor) {
        return executeWithConnection(connection -> {
            try (PreparedStatement preparedStatement = connection.prepareStatement(sql)) {
                parameterSetter.accept(preparedStatement);
                return executor.apply(preparedStatement);
            }
        });
    }

}
