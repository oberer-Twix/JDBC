package persistence;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class ConnectionManager {

    private static Connection connection;

    public static Connection getInstance() throws SQLException {
        if (connection == null) {
            connection = DriverManager.getConnection("jdbc:postgresql://ifpostgres02:5432/hablecker_individuell", "hablecker_thomas", "17122003");
        }
        return connection;
    }

    public static void closeConnection() throws SQLException {
        connection.close();
    }
}
