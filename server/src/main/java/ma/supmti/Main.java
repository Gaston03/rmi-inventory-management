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
            /*
            *  Initialize database by creating [products] and [users] tables
            * */
            DatabaseConnection.initializeDatabase();
            ProductService productService = new ProductServiceImpl();

            /*
            * Check if test products have already been inserted
            * If not, insert them in [Products] database
            * */
            if (productService.findAllProducts().isEmpty()) {
                DatabaseConnection.insertTestData();
            }

            /*
            * Set up RMI registry on port [8000]
            * It's mean that the server will be listening to request on that port
            * */
            Registry registry = LocateRegistry.createRegistry(8000);

            /*
            * Bind the [ProductService] and [UserService] objects
            * So that they be used on client side
            * */
            registry.bind("ProductService", productService);
            registry.bind("UserService", new UserServiceImpl());

            System.out.println("Server is running...");
        } catch (Exception e) {
            System.out.println("Failed to run the server: " + e.getMessage() + "\nCause: " + e.getCause());
        }
    }
}