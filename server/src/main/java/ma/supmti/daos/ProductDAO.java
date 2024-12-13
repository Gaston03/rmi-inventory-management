package ma.supmti.daos;

import ma.supmti.config.DatabaseConnection;
import ma.supmti.dtos.ProductDTO;
import ma.supmti.models.Product;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class ProductDAO {

    public boolean save(ProductDTO product) throws SQLException {
        String sql = "INSERT INTO products (name, category, quantity, price) VALUES (?, ?, ?, ?)";

        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, product.name());
            stmt.setString(2, product.category());
            stmt.setLong(3, product.quantity());
            stmt.setDouble(4, product.price());

            int affectedRows = stmt.executeUpdate();

            return affectedRows != 0;

        } catch (SQLException e) {
            throw new SQLException(e);
        }
    }

    public boolean updateProduct(ProductDTO product, Long id) throws SQLException {
        String sql = "UPDATE products " +
                "SET name = ?, " +
                "category = ?, " +
                "quantity = ?, " +
                "price = ? " +
                "WHERE id = ?";

        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, product.name());
            stmt.setString(2, product.category());
            stmt.setLong(3, product.quantity());
            stmt.setDouble(4, product.price());
            stmt.setLong(5, id);

            int affectedRows = stmt.executeUpdate();
            return affectedRows > 0;

        } catch (SQLException e) {
            throw new SQLException(e);
        }
    }

    public boolean deleteProduct(Long id) throws SQLException {
        String sql = "DELETE FROM products WHERE id = ?";

        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setLong(1, id);

            int affectedRows = stmt.executeUpdate();
            return affectedRows > 0;
        } catch (SQLException e) {
            throw new SQLException(e);
        }
    }

    public List<Product> findProductByCategory(String category) throws SQLException {
        String sql = "SELECT * FROM " +
                "products WHERE category ILIKE ? " +
                "ORDER BY id ASC";
        List<Product> products = new ArrayList<>();

        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, "%" + category + "%");

            try(ResultSet rs = stmt.executeQuery()) {
                while (rs.next()) {
                    Product product = new Product();

                    product.setId(rs.getLong("id"));
                    product.setName(rs.getString("name"));
                    product.setCategory(rs.getString("category"));
                    product.setPrice(rs.getDouble("price"));
                    product.setQuantity(rs.getLong("quantity"));

                    products.add(product);
                }
            }
        } catch (SQLException e) {
            throw new SQLException(e);
        }

        return products;
    }

    public List<Product> findAllProducts() throws SQLException {
        String sql = "SELECT * FROM products " +
                "ORDER BY id ASC";
        List<Product> products = new ArrayList<>();

        try (Connection conn = DatabaseConnection.getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {

            while (rs.next()) {
                Product product = new Product();

                product.setId(rs.getLong("id"));
                product.setName(rs.getString("name"));
                product.setCategory(rs.getString("category"));
                product.setPrice(rs.getDouble("price"));
                product.setQuantity(rs.getLong("quantity"));

                products.add(product);
            }

            return products;
        } catch (SQLException e) {
            throw new SQLException(e);
        }

    }

    public Product findProductById(Long id) throws SQLException {
        String sql = "SELECT * FROM products WHERE id = ?";

        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setLong(1, id);

            try(ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    Product product = new Product();

                    product.setId(rs.getLong("id"));
                    product.setName(rs.getString("name"));
                    product.setCategory(rs.getString("category"));
                    product.setPrice(rs.getDouble("price"));
                    product.setQuantity(rs.getLong("quantity"));

                    return product;
                }
            }
        } catch (SQLException e) {
            throw new SQLException(e);
        }

        return null;
    }
}
