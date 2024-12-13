package ma.supmti.services;

import ma.supmti.dtos.ProductDTO;
import ma.supmti.models.Product;

import java.rmi.Remote;
import java.rmi.RemoteException;
import java.sql.SQLException;
import java.util.List;

public interface ProductService extends Remote {
    boolean createProduct(ProductDTO product) throws RemoteException, SQLException;

    boolean updateProduct(ProductDTO product, Long id) throws RemoteException, SQLException;

    boolean deleteProduct(Long id) throws RemoteException, SQLException;

    Product findProductById(Long id) throws RemoteException, SQLException;

    List<Product> findProductsByCategory(String category) throws RemoteException, SQLException;

    List<Product> findAllProducts() throws RemoteException, SQLException;
}
