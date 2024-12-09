package ma.supmti.services;

import ma.supmti.dtos.UserDTO;
import ma.supmti.models.User;

import java.rmi.Remote;
import java.rmi.RemoteException;
import java.sql.SQLException;
import java.util.Optional;

public interface UserService extends Remote {
    boolean register(UserDTO user) throws RemoteException, SQLException;

    boolean login(UserDTO user) throws RemoteException, SQLException;
}
