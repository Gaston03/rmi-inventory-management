package ma.supmti.config;

import ma.supmti.services.ProductService;
import ma.supmti.services.UserService;

import java.rmi.NotBoundException;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;

public class RemoteListenner {
    private final ProductService productService;
    private final UserService userService;

    public RemoteListenner() throws RemoteException, NotBoundException {
         Registry registry = LocateRegistry.getRegistry("localhost", 8000);

         productService = (ProductService) registry.lookup("ProductService");
         userService = (UserService) registry.lookup("UserService");
    }

    public ProductService getProductService() {
        return productService;
    }

    public UserService getUserService() {
        return userService;
    }

}
