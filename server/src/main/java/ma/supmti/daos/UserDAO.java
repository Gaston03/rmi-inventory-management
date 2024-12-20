package ma.supmti.daos;

import ma.supmti.config.DatabaseConnection;
import ma.supmti.dtos.UserDTO;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Base64;

/*
* The UserDAO class handles database operations related to user authentication and management.
* */
public class UserDAO {

    /**
     * Registers a new user by storing their username, hashed password, and a unique salt.
     * @param user (UserDTO user: Contains the username and plaintext password of the user).
     * @return boolean indicating success or failure.
     * @throws SQLException if the registration fails
    * */
    public boolean register(UserDTO user) throws SQLException {
        /* Generates a salt */
        String salt = generateSalt();

        /* Hashes the password with the salt */
        String hashedPassword = hashPassword(user.password(), salt);
        String sql = "INSERT INTO users (username, password, salt)" +
                "VALUES (?, ?, ?)";

        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            /* Inserts data into the users table */
            stmt.setString(1, user.username());
            stmt.setString(2, hashedPassword);
            stmt.setString(3, salt);

            int affectedRows = stmt.executeUpdate();

            return affectedRows != 0;

        } catch (SQLException e) {
            System.out.println("Failed to register user: " + e.getMessage());
            throw new SQLException(e);
        }
    }

    /**
     * Authenticates a user by verifying their credentials.
     * @param user (UserDTO user: Contains the username and plaintext password).
     * @return boolean indicating whether login is successful
     * @throws SQLException if login fails
    * */
    public boolean login(UserDTO user) throws SQLException {
        String sql = "SELECT * FROM users WHERE username = ?";

        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, user.username());

            /* Fetches the stored hashed password and salt from the users table */
            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    String storedHash = rs.getString("password");
                    String salt = rs.getString("salt");

                    /* Verify the input password against the stored hash */
                    return verifyPassword(user.password(), storedHash, salt);
                }

                return false;
            }
        } catch (SQLException e) {
            System.out.println("Failed to log user in: " + e.getMessage());
            throw new SQLException(e);
        }

    }

    /**
     * Generates a secure random salt for password hashing.
     * @return String containing the Base64-encoded salt.
    * */
    private String generateSalt() {
        SecureRandom random = new SecureRandom();
        byte[] salt = new byte[16];
        random.nextBytes(salt);
        return Base64.getEncoder().encodeToString(salt);
    }

    /**
     * Hashes the password using SHA-256 with the provided salt.
     * @param password (Plaintext password), salt (Salt to be used for hashing)
     * @return String containing the Base64-encoded hashed password.
     * @throws RuntimeException if the hashing algorithm fails
     * */
    private String hashPassword(String password, String salt) {
        try {
            MessageDigest md = MessageDigest.getInstance("SHA-256");
            md.update(salt.getBytes());
            byte[] hashedPassword = md.digest(password.getBytes());
            return Base64.getEncoder().encodeToString(hashedPassword);
        } catch (NoSuchAlgorithmException e) {
            throw new RuntimeException("Password hashing failed", e);
        }
    }

    /**
     * Compares the hash of the input password with the stored hash.
     * @param inputPassword (Password entered by the user), storedHash (Stored hashed password in the db), salt (Salt associated with the user)
     * @return boolean indicating if the passwords match.
    * */
    private boolean verifyPassword(String inputPassword, String storedHash, String salt) {
        String inputHash = hashPassword(inputPassword, salt);
        return inputHash.equals(storedHash);
    }
}
