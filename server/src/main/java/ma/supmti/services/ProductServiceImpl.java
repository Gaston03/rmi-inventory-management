package ma.supmti.services;

import ma.supmti.daos.ProductDAO;
import ma.supmti.dtos.ProductDTO;
import ma.supmti.models.Product;

import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;
import java.sql.SQLException;
import java.util.List;

public class ProductServiceImpl extends UnicastRemoteObject implements ProductService {
    private final ProductDAO dao;

    public ProductServiceImpl() throws RemoteException {
        dao = new ProductDAO();
    }


    /**
     * @param product
     * @return
     * @throws RemoteException
     */
    @Override
    public boolean createProduct(ProductDTO product) throws RemoteException, SQLException {
        return dao.save(product);
    }

    /**
     * @param product
     * @param id
     * @return
     * @throws RemoteException, SQLException
     */
    @Override
    public boolean updateProduct(ProductDTO product, Long id) throws RemoteException, SQLException  {
        return dao.updateProduct(product, id);
    }

    /**
     * @param id
     * @return
     * @throws RemoteException
     */
    @Override
    public boolean deleteProduct(Long id) throws RemoteException, SQLException {
        return dao.deleteProduct(id);
    }

    /**
     * @param id
     * @return
     * @throws RemoteException, SQLException
     */
    @Override
    public Product findProductById(Long id) throws RemoteException, SQLException {
        return dao.findProductById(id);
    }

    /**
     * @param category
     * @return
     * @throws RemoteException, SQLException
     */
    @Override
    public List<Product> findProductsByCategory(String category) throws RemoteException, SQLException {
        return dao.findProductByCategory(category);
    }

    /**
     * @return
     * @throws RemoteException, SQLException
     */
    @Override
    public List<Product> findAllProducts() throws RemoteException, SQLException {
        return dao.findAllProducts();
    }
}
