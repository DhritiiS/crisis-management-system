package src; 

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class DBConnection {
    private static final String URL =
            "jdbc:mysql://localhost:3306/crisis_management_db";

    private static final String USER =
            System.getenv("CRISIS_DB_USER");

    private static final String PASSWORD =
            System.getenv("CRISIS_DB_PASSWORD");

    public static Connection getConnection() throws SQLException {
        if (USER == null || PASSWORD == null) {
            throw new SQLException(
                "Set CRISIS_DB_USER and CRISIS_DB_PASSWORD first."
            );
        }

        return DriverManager.getConnection(URL, USER, PASSWORD);
    }
}