package ma.supmti.services;

import ma.supmti.daos.UserDAO;
import ma.supmti.dtos.UserDTO;

import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;
import java.sql.SQLException;

public class UserServiceImpl extends UnicastRemoteObject implements UserService {
    private final UserDAO dao;

    public UserServiceImpl() throws RemoteException {
        dao = new UserDAO();
    }

    /**
     * @param user
     * @return boolean
     * @throws RemoteException
     */
    @Override
    public boolean register(UserDTO user) throws RemoteException, SQLException {
        return dao.register(user);
    }

    /**
     * @param user
     * @return boolean
     * @throws RemoteException
     */
    @Override
    public boolean login(UserDTO user) throws RemoteException, SQLException {
        return dao.login(user);
    }
}
