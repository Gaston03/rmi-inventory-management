package ma.supmti;

import ma.supmti.dtos.UserDTO;
import ma.supmti.config.RemoteListenner;
import ma.supmti.models.Product;
import ma.supmti.utils.ProductUtils;

import java.rmi.NotBoundException;
import java.rmi.RemoteException;
import java.sql.SQLException;
import java.util.Scanner;

public class Main {
    private static final RemoteListenner listener;

    static {
        try {
            listener = new RemoteListenner();
        } catch (RemoteException | NotBoundException e) {
            throw new RuntimeException(e);
        }
    }

    public static void main(String[] args) {

        try (Scanner sc = new Scanner(System.in)) {

            while (true) {
                System.out.println("========= WELCOME ==========");

                System.out.println("1. Login");
                System.out.println("2. Register");
                System.out.println("3. Exit");

                System.out.print("Select one option: ");

                int response = sc.nextInt();
                sc.nextLine();

                switch (response) {
                    case 1:
                        clearConsole();
                        System.out.println("========= LOGIN ==========");
                        System.out.print("Enter your username: ");
                        String username = sc.nextLine();

                        System.out.print("Enter your password: ");
                        String password = sc.nextLine();

                        UserDTO user = new UserDTO(username, password);
                        boolean isUserLoggedIn = listener.getUserService().login(user);

                        if (!isUserLoggedIn) {
                            System.out.println(" Invalid credentials. Please try again");
                        } else {
                            dashboard(sc, username);
                        }

                        break;
                    case 2:
                        clearConsole();
                        System.out.println("========= REGISTRATION ==========");
                        System.out.println("Enter your username: ");
                        String newUsername = sc.nextLine();

                        System.out.println("Enter your password: ");
                        String newPassword = sc.nextLine();

                        UserDTO newUser = new UserDTO(newUsername, newPassword);
                        boolean isUserCreated = listener.getUserService().register(newUser);

                        if (!isUserCreated) {
                            System.out.println(" REGISTRATION FAILED ");
                        } else {
                            System.out.println(" USER REGISTERED SUCCESSFULLY ");
                        }

                        break;
                    case 3:
                        System.out.println("Existing...");
                        sc.close();
                        return;
                    default:
                        System.out.println("Invalid choice. Please try again.");
                }
            }

        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    private static void clearConsole() {
        for (int i = 0; i < 50; i++) {
            System.out.println();
        }
    }

    private static void dashboard(Scanner sc, String username) throws RemoteException, SQLException {

        clearConsole();
        while (true) {
            System.out.println(" =========== WELCOME TO THE DASHBOARD, " + username + " ===========");

            System.out.println("1. Add new product");
            System.out.println("2. Update a product");
            System.out.println("3. Delete a product");
            System.out.println("4. Get a product");
            System.out.println("5. List of products");
            System.out.println("6. Search products");
            System.out.println("7. Logout");

            System.out.print("Select your option: ");

            int choice = sc.nextInt();
//            sc.nextLine();

            switch (choice) {
                case 1:
                    ProductUtils.promptToCreateNewProduct(sc, listener);
                    break;
                case 2:
                    ProductUtils.promptToUpdateProduct(sc, listener);
                    break;
                case 3:
                    ProductUtils.promptToDeleteProduct(sc, listener);
                    break;
                case 4:
                    ProductUtils.promptToGetAProduct(sc, listener);
                    break;
                case 5:
                    ProductUtils.promptToListProducts(listener);
                    break;
                case 6:
                    ProductUtils.promptToSearchProducts(sc, listener);
                    break;
                case 7:
                    System.out.println("Logging out...");
                    clearConsole();
                    return;
                default:
                    System.out.println(" Wrong selection. Please try again ");

            }
        }

    }
}