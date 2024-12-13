package ma.supmti.config;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Objects;

public class DatabaseConnection {
    private static final String URL = "jdbc:postgresql://dpg-ct7oddtumphs73905h10-a.frankfurt-postgres.render.com:5432/gestion_inventaire";
    private static final String USERNAME = "ulysse";
    private static final String PASSWORD = "24wDgfAC5tq0NX4iVFmpaVSNBC9ILaQg";

    public static Connection getConnection() throws SQLException {
        return DriverManager.getConnection(URL, USERNAME, PASSWORD);
    }

    public static void initializeDatabase() {
        try (Connection conn = getConnection();
             Statement stmt = conn.createStatement()) {

            stmt.executeUpdate("CREATE TABLE IF NOT EXISTS products (" +
                    "id SERIAL PRIMARY KEY, " +
                    "name VARCHAR(255) NOT NULL, " +
                    "category VARCHAR(255) NOT NULL, " +
                    "quantity INT NOT NULL, " +
                    "price DECIMAL(10, 2) NOT NULL" +
                    ");");

            stmt.executeUpdate("CREATE TABLE IF NOT EXISTS users (" +
                    "id SERIAL PRIMARY KEY, " +
                    "username VARCHAR(255) UNIQUE NOT NULL, " +
                    "password VARCHAR(255) NOT NULL, " +
                    "salt VARCHAR(255) NOT NULL" +
                    ");");

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public static void insertTestData() {
        try (Connection conn = DriverManager.getConnection(URL, USERNAME, PASSWORD);
             Statement stmt = conn.createStatement();
             BufferedReader br = new BufferedReader(
                     new InputStreamReader(
                             Objects.requireNonNull(
                                     DatabaseConnection.class.getResourceAsStream("/data.sql"))))) {

            StringBuilder sql = new StringBuilder();
            String line;
            while ((line = br.readLine()) != null) {
                sql.append(line).append("\n");
            }

            // Exécute les instructions SQL
            stmt.executeUpdate(sql.toString());
            System.out.println("Test data inserted successfully.");
        } catch (Exception e) {
            e.printStackTrace();
            System.err.println("An error occurred when inserting test data.");
        }
    }
}
