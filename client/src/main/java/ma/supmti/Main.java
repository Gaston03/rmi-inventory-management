package ma.supmti;

import ma.supmti.ui.AuthScreen;

import javax.swing.*;
import java.rmi.NotBoundException;
import java.rmi.RemoteException;

public class Main {

    public static void main(String[] args) {

        SwingUtilities.invokeLater(() -> {
            try {
                UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
            } catch (Exception e) {
                e.printStackTrace();
            }

            try {
                new AuthScreen().setVisible(true);
            } catch (NotBoundException | RemoteException e) {
                throw new RuntimeException(e);
            }
        });

    }

}