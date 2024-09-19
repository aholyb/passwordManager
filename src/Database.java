import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;

public class Database {
    private static final String URL = "jdbc:sqlite:passwords.db";

    public static void initialize() {
        try (Connection conn = DriverManager.getConnection(URL);
             Statement stmt = conn.createStatement()) {
            String sqlUsers = "CREATE TABLE IF NOT EXISTS users (id INTEGER PRIMARY KEY, username TEXT UNIQUE, password TEXT)";
            String sqlPasswords = "CREATE TABLE IF NOT EXISTS passwords (id INTEGER PRIMARY KEY, user_id INTEGER, site TEXT, password TEXT, FOREIGN KEY(user_id) REFERENCES users(id))";
            stmt.execute(sqlUsers);
            stmt.execute(sqlPasswords);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}