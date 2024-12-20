package ma.supmti.ui;

import ma.supmti.config.RemoteListenner;
import ma.supmti.dtos.UserDTO;

import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.rmi.NotBoundException;
import java.rmi.RemoteException;
import java.sql.SQLException;

public class RegistrationScreen extends JFrame {
    private JTextField usernameField;
    private JPasswordField passwordField;
    private RemoteListenner listenner = new RemoteListenner();

    public RegistrationScreen() throws NotBoundException, RemoteException {
        setTitle("Registration");
        setSize(300, 300);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);

        JPanel panel = new JPanel(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();

        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.insets = new Insets(5, 5, 5, 5);

        usernameField = new JTextField();
        usernameField.setSize(new Dimension(150, 50));

        passwordField = new JPasswordField();
        passwordField.setSize(new Dimension(150, 50));

        JButton registerButton = getRegisterButton();
        registerButton.setSize(new Dimension(150, 50));

        JLabel loginLabel = getLoginLabel();

        // First row
        gbc.gridx = 0;
        gbc.gridy = 0;
        panel.add(new JLabel("Username:"), gbc);

        gbc.gridx = 1;
        gbc.gridy = 0;
        panel.add(usernameField, gbc);

        // Second row
        gbc.gridx = 0;
        gbc.gridy = 1;
        panel.add(new JLabel("Password:"), gbc);

        gbc.gridx = 1;
        gbc.gridy = 1;
        panel.add(passwordField, gbc);

        // Third row - merged cells
        gbc.gridx = 0;
        gbc.gridy = 2;
        gbc.gridwidth = 2;  // Span across 2 columns
        gbc.anchor = GridBagConstraints.CENTER;
        panel.add(registerButton, gbc);

        // Fourth row - merged cells
        gbc.gridx = 0;
        gbc.gridy = 3;
        gbc.gridwidth = 2;
        gbc.anchor = GridBagConstraints.CENTER;
        panel.add(loginLabel, gbc);

        add(panel);
    }

    private JLabel getLoginLabel() {
        JLabel loginLabel = new JLabel("Already registered ? Click here to login.");

        loginLabel.addMouseListener(new MouseAdapter() {
            /**
             * {@inheritDoc}
             *
             * @param e
             */
            @Override
            public void mouseClicked(MouseEvent e) {
                try {
                    new AuthScreen().setVisible(true);
                } catch (NotBoundException | RemoteException ex) {
                    throw new RuntimeException(ex);
                }
                dispose();
            }
        });

        return loginLabel;
    }

    private JButton getRegisterButton() {
        JButton registerButton = new JButton("Register");

        registerButton.addActionListener(e -> {
            try {
                boolean isRegistrationOK = register();

                if (isRegistrationOK) {
                    new HomeScreen().setVisible(true);
                    dispose();
                } else {
                    JOptionPane.showMessageDialog(this, "An error occurred. Please try again!", "Error", JOptionPane.ERROR_MESSAGE);
                }
            } catch (SQLException | RemoteException | NotBoundException ex) {
                JOptionPane.showMessageDialog(this, "An error occurred. Please try again!", "Error", JOptionPane.ERROR_MESSAGE);

                throw new RuntimeException(ex);
            }
        });
        return registerButton;
    }

    private boolean register() throws SQLException, RemoteException {
        String username = usernameField.getText();
        String password = new String(passwordField.getPassword());

        if (username.isEmpty() && password.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Empty field. Please type something !", "Error", JOptionPane.ERROR_MESSAGE);
            return false;
        }

        return listenner.getUserService().register(new UserDTO(username, password));
    }
}
