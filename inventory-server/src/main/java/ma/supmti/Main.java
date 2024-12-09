package ma.supmti;

import ma.supmti.config.DatabaseConnection;
import ma.supmti.services.ProductService;
import ma.supmti.services.ProductServiceImpl;
import ma.supmti.services.UserServiceImpl;

import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;

public class Main {

    public static void main(String[] args) {
        try {
            DatabaseConnection.initializeDatabase();
            ProductService productService = new ProductServiceImpl();

            if (productService.findAllProducts().isEmpty()) {
                DatabaseConnection.insertTestData();
            }

            // RMI setup
            Registry registry = LocateRegistry.createRegistry(1099);

            // Bind the remote objects
            registry.bind("ProductService", new ProductServiceImpl());
            registry.bind("UserService", new UserServiceImpl());

            System.out.println("Server is running...");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}