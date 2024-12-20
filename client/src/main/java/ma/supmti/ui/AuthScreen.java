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

public class AuthScreen extends JFrame {
    private JTextField usernameField;
    private JPasswordField passwordField;
    private RemoteListenner listener = new RemoteListenner();

    public AuthScreen() throws NotBoundException, RemoteException {
        setTitle("Login");
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

        JButton registerButton = getLoginButton();
        registerButton.setSize(new Dimension(150, 50));

        JLabel loginLabel = getRegisterLabel();

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

    private JLabel getRegisterLabel() {
        JLabel loginLabel = new JLabel("Don't have an account yet ? Click here to register.");

        loginLabel.addMouseListener(new MouseAdapter() {
            /**
             * {@inheritDoc}
             *
             * @param e
             */
            @Override
            public void mouseClicked(MouseEvent e) {
                try {
                    new RegistrationScreen().setVisible(true);
                } catch (NotBoundException | RemoteException ex) {
                    throw new RuntimeException(ex);
                }
                dispose();
            }
        });

        return loginLabel;
    }

    private JButton getLoginButton() {
        JButton loginButton = new JButton("Login");

        loginButton.addActionListener(e -> {
            try {
                boolean isRegistrationOK = login();

                if (isRegistrationOK) {
                    new HomeScreen().setVisible(true);
                    dispose();
                } else {
                    JOptionPane.showMessageDialog(this, "Invalid credentials!", "Error", JOptionPane.ERROR_MESSAGE);
                }
            } catch (SQLException | RemoteException | NotBoundException ex) {
                JOptionPane.showMessageDialog(this, "An error occurred. Please try again!", "Error", JOptionPane.ERROR_MESSAGE);

                throw new RuntimeException(ex);
            }
        });
        return loginButton;
    }

    private boolean login() throws SQLException, RemoteException {
        String username = usernameField.getText();
        String password = new String(passwordField.getPassword());

        return listener.getUserService().login(new UserDTO(username, password));
    }
}
