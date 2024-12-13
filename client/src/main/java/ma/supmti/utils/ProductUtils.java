package ma.supmti.utils;

import ma.supmti.dtos.ProductDTO;
import ma.supmti.config.RemoteListenner;
import ma.supmti.models.Product;

import java.rmi.RemoteException;
import java.sql.SQLException;
import java.util.List;
import java.util.Scanner;

public class ProductUtils {
    public static void promptToCreateNewProduct(Scanner sc, RemoteListenner listener) throws RemoteException, SQLException {
        System.out.println("========== NEW PRODUCT ==========");

        System.out.print("- Name: ");
        String name = sc.nextLine();

        System.out.print("- Category: ");
        String category = sc.nextLine();

        System.out.print("- Quantity: ");
        Long quantity = sc.nextLong();

        System.out.print("- Price: ");
        Double price = sc.nextDouble();

        boolean isProductCreated = listener.getProductService().createProduct(new ProductDTO(name, category, price, quantity));

        if (isProductCreated) {
            System.out.println(" Product created successfully !!! ");
        } else {
            System.out.println(" Product creation failed !!! ");
        }
    }

    public static void promptToUpdateProduct(Scanner sc, RemoteListenner listener) throws RemoteException, SQLException {
        System.out.println("========== UPDATE PRODUCT ==========");

        System.out.print("- Product ID: ");
        Long id = sc.nextLong();
        sc.nextLine();

        Product savedProduct = listener.getProductService().findProductById(id);
        String name = savedProduct.getName();
        String category = savedProduct.getCategory();
        Long quantity = savedProduct.getQuantity();
        Double price = savedProduct.getPrice();

        System.out.print("Do you want to update Name " + "(" + savedProduct.getName() + ")" + " ? [Y/N] ");
        String nameChoice = sc.nextLine();

        if (nameChoice.equalsIgnoreCase("y")) {
            System.out.print("- Name: ");
            name = sc.nextLine();
        }

        System.out.print("Do you want to update Category " + "(" + savedProduct.getCategory() + ")" + "  ? [Y/N] ");
        String categoryChoice = sc.nextLine();

        if (categoryChoice.equalsIgnoreCase("y")) {
            System.out.print("- Category: ");
            category = sc.nextLine();
        }

        System.out.print("Do you want to update Quantity " + "(" + savedProduct.getQuantity() + ")" + "  ? [Y/N] ");
        String quantityChoice = sc.nextLine();

        if (quantityChoice.equalsIgnoreCase("y")) {
            System.out.print("- Quantity: ");
            quantity = sc.nextLong();
            sc.nextLine();
        }

        System.out.print("Do you want to update Price " + "(" + savedProduct.getPrice() + ")" + "  ? [Y/N] ");
        String priceChoice = sc.nextLine();

        if (priceChoice.equalsIgnoreCase("y")) {
            System.out.print("- Price: ");
            price = sc.nextDouble();
            sc.nextLine();
        }

        Product product = new Product();

        product.setId(id);
        product.setName(name);
        product.setCategory(category);
        product.setPrice(price);
        product.setQuantity(quantity);

        boolean isProductUpdated = listener.getProductService().updateProduct(new ProductDTO(name, category, price, quantity), id);

        if (isProductUpdated) {
            System.out.println(" Product updated successfully !!! ");
        } else {
            System.out.println(" Product update failed !!! ");
        }
    }

    public static void promptToGetAProduct(Scanner sc, RemoteListenner listener) throws RemoteException, SQLException {
        System.out.println("========== GET PRODUCT ==========");

        System.out.print("- Product ID: ");
        long id = sc.nextLong();

        Product product = listener.getProductService().findProductById(id);

        System.out.println(product.toString());
    }

    public static void promptToDeleteProduct(Scanner sc, RemoteListenner listener) throws RemoteException, SQLException {
        System.out.println("========== DELETE PRODUCT ==========");

        System.out.print("- Product ID: ");
        Long id = sc.nextLong();

        boolean isProductDeleted = listener.getProductService().deleteProduct(id);

        if (isProductDeleted) {
            System.out.println(" Product deleted successfully !!! ");
        } else {
            System.out.println(" Product deletion failed !!! ");
        }
    }

    public static void promptToListProducts(RemoteListenner listener) throws RemoteException, SQLException {
        List<Product> products = listener.getProductService().findAllProducts();

        for (Product product: products) {
            System.out.println(product.toString());
        }
    }

    public static void promptToSearchProducts(Scanner sc, RemoteListenner listener) throws RemoteException, SQLException {
        System.out.println("========== SEARCH PRODUCTS ==========");

        System.out.print("- Category: ");
        String category = sc.nextLine();

        List<Product> products = listener.getProductService().findProductsByCategory(category);

        for (Product product: products) {
            System.out.println(product.toString());
        }
    }
}
