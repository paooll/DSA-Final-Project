package hotel;

import java.sql.*;

public class Authentication {
    private static final String DB_URL = "jdbc:mysql://localhost:3306/hotelmanagement";
    private static final String DB_USER = "root";
    private static final String DB_PASSWORD = "root";

    public Authentication() {
        try {
            Class.forName("com.mysql.cj.jdbc.Driver");
            createAdminAccount();
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
    }

    private void createAdminAccount() {
        if (!isUsernameAvailable("admin")) {
            System.out.println("Admin account already exists.");
            return;
        }

        User adminUser = new User("Admin", "User", "admin", "password");
        adminUser.setAdmin(true);
        createAccount(adminUser);
    }

    public boolean validateCredentials(User user) {
        String sql = "SELECT password, is_admin FROM users WHERE username = ?";

        try (Connection conn = DriverManager.getConnection(DB_URL, DB_USER, DB_PASSWORD);
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, user.getUsername());
            ResultSet rs = stmt.executeQuery();

            if (rs.next()) {
                String storedPassword = rs.getString("password");
                boolean isAdmin = rs.getBoolean("is_admin");

                if (storedPassword.equals(user.getPassword())) {
                    user.setAdmin(isAdmin);
                    System.out.println("User validation successful. Is admin: " + user.isAdmin());
                    return true;
                } else {
                    System.out.println("Password does not match for user: " + user.getUsername());
                    return false;
                }
            } else {
                System.out.println("User not found: " + user.getUsername());
                return false;
            }
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    public boolean createAccount(User user) {
        String sqlInsert = "INSERT INTO users (first_name, last_name, username, password, is_admin, email, phone) VALUES (?, ?, ?, ?, ?, ?, ?)";

        try (Connection conn = DriverManager.getConnection(DB_URL, DB_USER, DB_PASSWORD);
             PreparedStatement insertStmt = conn.prepareStatement(sqlInsert)) {

            insertStmt.setString(1, user.getFirstName());
            insertStmt.setString(2, user.getLastName());
            insertStmt.setString(3, user.getUsername());
            insertStmt.setString(4, user.getPassword());
            insertStmt.setBoolean(5, user.isAdmin());
            insertStmt.setString(6, user.getEmail());
            insertStmt.setString(7, user.getPhone());

            insertStmt.executeUpdate();
            System.out.println("Account successfully created for username: " + user.getUsername());
            return true;

        } catch (SQLException e) {
            if (e.getErrorCode() == 1062) { 
                System.out.println("Account creation failed: Username '" + user.getUsername() + "' already exists.");
            } else {
                e.printStackTrace();
            }
            return false;
        }
    }

    private boolean isUsernameAvailable(String username) {
        String query = "SELECT COUNT(*) FROM users WHERE username = ?";
        try (Connection conn = DriverManager.getConnection(DB_URL, DB_USER, DB_PASSWORD);
             PreparedStatement stmt = conn.prepareStatement(query)) {
            stmt.setString(1, username);
            ResultSet rs = stmt.executeQuery();
            if (rs.next()) {
                return rs.getInt(1) == 0; 
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }
}
