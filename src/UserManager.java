import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class UserManager {
    
    private static final String URL = "jdbc:sqlite:passwords.db";

    public static boolean registerUser(String username, String password) {
        try (Connection conn = DriverManager.getConnection(URL);
             PreparedStatement pstmt = conn.prepareStatement("INSERT INTO users (username, password) VALUES (?, ?)")) {
            pstmt.setString(1, username);
            pstmt.setString(2, password);
            pstmt.executeUpdate();
            return true;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    public static boolean authenticateUser(String username, String password) {
        try (Connection conn = DriverManager.getConnection(URL);
             PreparedStatement pstmt = conn.prepareStatement("SELECT * FROM users WHERE username = ? AND password = ?")) {
            pstmt.setString(1, username);
            pstmt.setString(2, password);
            ResultSet rs = pstmt.executeQuery();
            return rs.next();
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    public static void savePassword(int userId, String site, String password) {
        if (isPasswordExists(userId, site)) {
            System.out.println("Password for this site already exists.");
            return; // Пароль уже существует, ничего не делаем
        }
        
        try (Connection conn = DriverManager.getConnection(URL);
             PreparedStatement pstmt = conn.prepareStatement("INSERT INTO passwords (user_id, site, password) VALUES (?, ?, ?)")) {
            pstmt.setInt(1, userId);
            pstmt.setString(2, site);
            pstmt.setString(3, password);
            pstmt.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public static boolean isPasswordExists(int userId, String site) {
        try (Connection conn = DriverManager.getConnection(URL);
             PreparedStatement pstmt = conn.prepareStatement("SELECT * FROM passwords WHERE user_id = ? AND site = ?")) {
            pstmt.setInt(1, userId);
            pstmt.setString(2, site);
            ResultSet rs = pstmt.executeQuery();
            return rs.next(); // Возвращает true, если запись найдена
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    public static List<String> getUserPasswords(int userId) {
        List<String> passwords = new ArrayList<>();
        try (Connection conn = DriverManager.getConnection(URL);
             PreparedStatement pstmt = conn.prepareStatement("SELECT site, password FROM passwords WHERE user_id = ?")) {
            pstmt.setInt(1, userId);
            ResultSet rs = pstmt.executeQuery();
            while (rs.next()) {
                passwords.add(rs.getString("site") + ": " + rs.getString("password"));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return passwords;
    }
}