package ma.supmti.ui;

import ma.supmti.config.RemoteListenner;
import ma.supmti.dtos.ProductDTO;
import ma.supmti.models.Product;

import javax.swing.*;
import javax.swing.event.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.rmi.NotBoundException;
import java.rmi.RemoteException;
import java.sql.SQLException;
import java.util.List;

public class HomeScreen extends JFrame {
    private JList<Product> productList;
    private List<Product> products;
    private JTable productTable;
    private DefaultTableModel tableModel;
    private JTextField searchField;
    private final RemoteListenner listenner = new RemoteListenner();

    public HomeScreen() throws NotBoundException, RemoteException, SQLException {
        setTitle("Product Management System");
        setSize(800, 800);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);

        createUI();
    }

    private void createUI() throws SQLException, RemoteException {
        // Main layout
        setLayout(new BorderLayout(10, 10));

        JPanel menuPanel = new JPanel(new BorderLayout());

        // Search panel
        JPanel searchPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        searchField = new JTextField(20);

//        JButton searchButton = new JButton("Search");
//        searchButton.addActionListener(e -> {
//            try {
//                searchProducts();
//            } catch (SQLException | RemoteException ex) {
//                throw new RuntimeException(ex);
//            }
//        });

        searchField.getDocument().addDocumentListener(new DocumentListener() {
            @Override
            public void insertUpdate(DocumentEvent e) {
                try {
                    searchProducts();
                } catch (SQLException | RemoteException ex) {
                    throw new RuntimeException(ex);
                }
            }

            @Override
            public void removeUpdate(DocumentEvent e) {
                try {
                    searchProducts();
                } catch (SQLException | RemoteException ex) {
                    throw new RuntimeException(ex);
                }
            }

            @Override
            public void changedUpdate(DocumentEvent e) {
                try {
                    searchProducts();
                } catch (SQLException | RemoteException ex) {
                    throw new RuntimeException(ex);
                }
            }
        });

        searchPanel.add(new JLabel("Search: "));
        searchPanel.add(searchField);
//        searchPanel.add(searchButton);
        add(searchPanel, BorderLayout.NORTH);

        // Create right panel for logout button
        JPanel logoutPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        JButton logoutButton = new JButton("Logout");
        logoutButton.addActionListener(e -> {
            try {
                logout();
            } catch (NotBoundException | RemoteException ex) {
                throw new RuntimeException(ex);
            }
        });
        logoutPanel.add(logoutButton);

        // Add both panels to the search panel
        menuPanel.add(searchPanel, BorderLayout.WEST);
        menuPanel.add(logoutPanel, BorderLayout.EAST);

        add(menuPanel, BorderLayout.NORTH);

        // Table setup
        String[] columnNames = {"ID", "Name", "Category", "Quantity", "Price"};
        tableModel = new DefaultTableModel(columnNames, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false; // Make table read-only
            }
        };

        productTable = new JTable(tableModel);
        productTable.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);

        // Set column widths
        productTable.getColumnModel().getColumn(0).setPreferredWidth(50);
        productTable.getColumnModel().getColumn(1).setPreferredWidth(150);
        productTable.getColumnModel().getColumn(2).setPreferredWidth(150);
        productTable.getColumnModel().getColumn(3).setPreferredWidth(50);
        productTable.getColumnModel().getColumn(3).setPreferredWidth(100);

        // Add table to scroll pane
        JScrollPane scrollPane = new JScrollPane(productTable);
        add(scrollPane, BorderLayout.CENTER);

        // Buttons panel
        JPanel buttonPanel = getButtonPanel();
        add(buttonPanel, BorderLayout.SOUTH);

        refreshTable();
    }

    private void logout() throws NotBoundException, RemoteException {
        int result = JOptionPane.showConfirmDialog(this,
                "Are you sure you want to logout?",
                "Confirm Logout",
                JOptionPane.YES_NO_OPTION);

        if (result == JOptionPane.YES_OPTION) {
            dispose(); // Close current window
            new AuthScreen().setVisible(true); // Show login screen
        }
    }

    private JPanel getButtonPanel() {
        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 10, 10));
        JButton addButton = new JButton("Add Product");
        JButton updateButton = new JButton("Update Product");
        JButton deleteButton = new JButton("Delete Product");

        addButton.addActionListener(e -> addProduct());
        updateButton.addActionListener(e -> updateProduct());
        deleteButton.addActionListener(e -> {
            try {
                deleteProduct();
            } catch (SQLException | RemoteException ex) {
                throw new RuntimeException(ex);
            }
        });

        buttonPanel.add(addButton);
        buttonPanel.add(updateButton);
        buttonPanel.add(deleteButton);
        return buttonPanel;
    }

    private void refreshTable() throws SQLException, RemoteException {
        tableModel.setRowCount(0); // Clear table
        
        products = listenner.getProductService().findAllProducts();
        for (Product product : products) {
            Object[] row = {
                    product.getId(),
                    product.getName(),
                    product.getCategory(),
                    product.getQuantity(),
                    String.format("%.2f", product.getPrice()),
            };

            tableModel.addRow(row);
        }
    }

    private void searchProducts() throws SQLException, RemoteException {
        String searchTerm = searchField.getText();

        SwingWorker<Void, Void> worker = new SwingWorker<Void, Void>() {
            @Override
            protected Void doInBackground() throws Exception {
                tableModel.setRowCount(0);

                List<Product> searchedProducts = listenner.getProductService().findProductsByCategory(searchTerm);
                for (Product product : searchedProducts) {
                    Object[] row = {
                            product.getId(),
                            product.getName(),
                            product.getCategory(),
                            product.getQuantity(),
                            String.format("%.2f", product.getPrice()),
                    };

                    tableModel.addRow(row);
                }

                return null;
            }
        };

        worker.execute();

    }

    private void addProduct() {
        JTextField nameField = new JTextField();
        JTextField categoryField = new JTextField();
        JTextField quantityField = new JTextField();
        JTextField priceField = new JTextField();

        Object[] fields = {
                "Name: ", nameField,
                "Category: ", categoryField,
                "Quantity: ", quantityField,
                "Price: ", priceField,
        };

        int result = JOptionPane.showConfirmDialog(this, fields, "Add Product",
                JOptionPane.OK_CANCEL_OPTION, JOptionPane.PLAIN_MESSAGE);

        if (result == JOptionPane.OK_OPTION) {
            try {
                String name = nameField.getText();
                String category = categoryField.getText();
                Long quantity = Long.parseLong(quantityField.getText());
                Double price = Double.parseDouble(priceField.getText());
                boolean isCreationOk = listenner.getProductService().createProduct(new ProductDTO(name, category, price, quantity));
                
                if (isCreationOk) {
                    refreshTable();
                } else {
                    JOptionPane.showMessageDialog(this, "Failed to create product. Please try again !",
                            "Error", JOptionPane.ERROR_MESSAGE);
                }
            } catch (NumberFormatException ex) {
                JOptionPane.showMessageDialog(this, "Invalid price format!",
                        "Error", JOptionPane.ERROR_MESSAGE);
            } catch (RemoteException | SQLException e) {
                JOptionPane.showMessageDialog(this, "Failed to create product. Please try again !",
                        "Error", JOptionPane.ERROR_MESSAGE);
                throw new RuntimeException(e);
            }
        }
    }

    private void updateProduct() {
        int selectedRow = productTable.getSelectedRow();
        if (selectedRow == -1) {
            JOptionPane.showMessageDialog(this, "Please select a product to update!",
                    "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }

        Product selectedProduct = products.get(selectedRow);

        JTextField nameField = new JTextField(selectedProduct.getName());
        JTextField categoryField = new JTextField(selectedProduct.getCategory());
        JTextField quantityField = new JTextField(String.valueOf(selectedProduct.getQuantity()));
        JTextField priceField = new JTextField(String.valueOf(selectedProduct.getPrice()));

        Object[] fields = {
                "Name: ", nameField,
                "Category: ", categoryField,
                "Quantity: ", quantityField,
                "Price: ", priceField,
        };

        int result = JOptionPane.showConfirmDialog(this, fields, "Update Product",
                JOptionPane.OK_CANCEL_OPTION, JOptionPane.PLAIN_MESSAGE);

        if (result == JOptionPane.OK_OPTION) {
            try {
                String name = nameField.getText();
                String category = categoryField.getText();
                Long quantity = Long.parseLong(quantityField.getText());
                Double price = Double.parseDouble(priceField.getText());

                boolean isUpdateOk = listenner.getProductService().updateProduct(new ProductDTO(name, category, price, quantity), selectedProduct.getId());

                if (isUpdateOk) {
                    refreshTable();
                } else {
                    JOptionPane.showMessageDialog(this, "Failed to update product. Please try again !",
                            "Error", JOptionPane.ERROR_MESSAGE);
                }
            } catch (NumberFormatException ex) {
                JOptionPane.showMessageDialog(this, "Invalid price format!",
                        "Error", JOptionPane.ERROR_MESSAGE);
            } catch (RemoteException | SQLException ex) {
                JOptionPane.showMessageDialog(this, "Failed to update product. Please try again !",
                        "Error", JOptionPane.ERROR_MESSAGE);
            }
        }
    }

    private void deleteProduct() throws SQLException, RemoteException {
        int selectedRow = productTable.getSelectedRow();
        if (selectedRow == -1) {
            JOptionPane.showMessageDialog(this, "Please select a product to delete!",
                    "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }

        int result = JOptionPane.showConfirmDialog(this,
                "Are you sure you want to delete this product?",
                "Confirm Delete", JOptionPane.YES_NO_OPTION);

        if (result == JOptionPane.YES_OPTION) {
            System.out.println("Selected Row: " + selectedRow);
            Product selectedProduct = products.get(selectedRow);

            boolean isDeletionOk = listenner.getProductService().deleteProduct(selectedProduct.getId());

            if (isDeletionOk) {
                refreshTable();
            } else {
                JOptionPane.showMessageDialog(this, "Failed to delete product. Please try again !",
                        "Error", JOptionPane.ERROR_MESSAGE);
            }
        }
    }
}
