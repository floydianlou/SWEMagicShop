import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class main {
    public static void main(String[] args) {
        String url = "jdbc:postgresql://localhost:5432/otariMagicShop";
        String username = "postgres";
        String password = "postgres";

        try (Connection connection = DriverManager.getConnection(url, username, password)) {
            if (connection != null) {
                System.out.println("Successfully connected to PostgreSQL database!");
            }
        } catch (SQLException e) {
            System.out.println("Warning: " + e.getMessage());
        }
    }
}

