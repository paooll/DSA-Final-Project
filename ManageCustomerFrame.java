package hotel;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class ManageCustomerFrame extends JFrame {
    private static final long serialVersionUID = 1L;
    private JPanel contentPane;
    private User admin;
    private JTable customerTable;


    public ManageCustomerFrame(User admin) {
        this.admin = admin;

        setTitle("Manage Customers - Admin Dashboard");
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setExtendedState(JFrame.MAXIMIZED_BOTH); 
        setBounds(100, 100, 1000, 600);
        setLocationRelativeTo(null); 

        contentPane = new JPanel();
        contentPane.setBorder(new EmptyBorder(20, 20, 20, 20));
        contentPane.setLayout(new BorderLayout(10, 10));
        setContentPane(contentPane);


        JLabel titleLabel = new JLabel("Manage Customers", JLabel.CENTER);
        titleLabel.setFont(new Font("Times New Roman", Font.BOLD, 36));
        titleLabel.setForeground(new Color(52, 73, 94));
        contentPane.add(titleLabel, BorderLayout.NORTH);


        customerTable = new JTable();
        customerTable.getTableHeader().setFont(new Font("Times New Roman", Font.BOLD, 16));
        customerTable.setFont(new Font("Times New Roman", Font.PLAIN, 14));
        JScrollPane scrollPane = new JScrollPane(customerTable);
        scrollPane.setBorder(BorderFactory.createLineBorder(new Color(189, 195, 199), 2));
        contentPane.add(scrollPane, BorderLayout.CENTER);


        JPanel buttonPanel = new JPanel();
        buttonPanel.setLayout(new FlowLayout(FlowLayout.CENTER, 15, 10));
        
        JButton addButton = createButton("", "images/add.png");
        JButton editButton = createButton("", "images/edit.png");
        JButton deleteButton = createButton("", "images/delete.png");
        JButton backButton = createButton("", "images/back.png");
        
        buttonPanel.add(addButton);
        buttonPanel.add(editButton);
        buttonPanel.add(deleteButton);
        buttonPanel.add(backButton);
        contentPane.add(buttonPanel, BorderLayout.SOUTH);


        addButton.addActionListener(e -> addCustomer());
        editButton.addActionListener(e -> editCustomer());
        deleteButton.addActionListener(e -> deleteCustomer());
        backButton.addActionListener(e -> goToAdminMainMenu());
        

        loadCustomerData();

        setVisible(true);
    }

    // Helper method to create buttons with icons
    private JButton createButton(String text, String iconPath) {
        JButton button = new JButton(text);
        button.setFont(new Font("SansSerif", Font.PLAIN, 16));
        button.setBackground(new Color(46, 204, 113));
        button.setForeground(Color.WHITE);
        button.setFocusPainted(false);
        button.setBorder(BorderFactory.createEmptyBorder(10, 20, 10, 20));
        button.setIcon(new ImageIcon(new ImageIcon(iconPath).getImage().getScaledInstance(18, 18, Image.SCALE_SMOOTH)));
        return button;
    }

    // Method to load customer data from the database
    private void loadCustomerData() {
        String url = "jdbc:mysql://localhost:3306/hotelmanagement"; // Replace with your database URL
        String username = "root"; // Replace with your database username
        String password = "root"; // Replace with your database password

        try (Connection connection = DriverManager.getConnection(url, username, password)) {
            String query = "SELECT id, first_name, last_name, username, email, phone FROM users";
            Statement statement = connection.createStatement();
            ResultSet resultSet = statement.executeQuery(query);

            // Column names
            List<String> columnNames = new ArrayList<>();
            columnNames.add("ID");
            columnNames.add("First Name");
            columnNames.add("Last Name");
            columnNames.add("Username");
            columnNames.add("Email");
            columnNames.add("Phone");

            // Rows
            List<Object[]> data = new ArrayList<>();
            while (resultSet.next()) {
                Object[] row = new Object[6];
                row[0] = resultSet.getInt("id");
                row[1] = resultSet.getString("first_name");
                row[2] = resultSet.getString("last_name");
                row[3] = resultSet.getString("username");
                row[4] = resultSet.getString("email");
                row[5] = resultSet.getString("phone");
                data.add(row);
            }

            // Populate the table
            DefaultTableModel model = new DefaultTableModel(data.toArray(new Object[0][0]), columnNames.toArray());
            customerTable.setModel(model);
        } catch (SQLException e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(this, "Error loading customer data: " + e.getMessage(),
                    "Database Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    // Method to add a customer
    private void addCustomer() {
        String url = "jdbc:mysql://localhost:3306/hotelmanagement";
        String username = "root";
        String password = "root";

        // Input fields
        JTextField firstNameField = new JTextField();
        JTextField lastNameField = new JTextField();
        JTextField userNameField = new JTextField();
        JTextField emailField = new JTextField();
        JTextField phoneField = new JTextField();
        JPasswordField passwordField = new JPasswordField();
        JPasswordField confirmPasswordField = new JPasswordField();

        // Input panel
        JPanel inputPanel = new JPanel(new GridLayout(7, 2, 5, 5));
        inputPanel.add(new JLabel("First Name:"));
        inputPanel.add(firstNameField);
        inputPanel.add(new JLabel("Last Name:"));
        inputPanel.add(lastNameField);
        inputPanel.add(new JLabel("Username:"));
        inputPanel.add(userNameField);
        inputPanel.add(new JLabel("Email:"));
        inputPanel.add(emailField);
        inputPanel.add(new JLabel("Phone:"));
        inputPanel.add(phoneField);
        inputPanel.add(new JLabel("Password:"));
        inputPanel.add(passwordField);
        inputPanel.add(new JLabel("Confirm Password:"));
        inputPanel.add(confirmPasswordField);

        // Show dialog
        int result = JOptionPane.showConfirmDialog(this, inputPanel, "Add Customer", JOptionPane.OK_CANCEL_OPTION);
        if (result != JOptionPane.OK_OPTION) {
            return; // User canceled
        }

        // Retrieve input
        String firstName = firstNameField.getText();
        String lastName = lastNameField.getText();
        String userName = userNameField.getText();
        String email = emailField.getText();
        String phone = phoneField.getText();
        String passwordInput = new String(passwordField.getPassword());
        String confirmPassword = new String(confirmPasswordField.getPassword());

        // Validate input
        if (firstName.isEmpty() || lastName.isEmpty() || userName.isEmpty() || email.isEmpty() || phone.isEmpty() ||
                passwordInput.isEmpty() || confirmPassword.isEmpty()) {
            JOptionPane.showMessageDialog(this, "All fields are required.", "Validation Error", JOptionPane.ERROR_MESSAGE);
            return;
        }

        if (!passwordInput.equals(confirmPassword)) {
            JOptionPane.showMessageDialog(this, "Passwords do not match.", "Validation Error", JOptionPane.ERROR_MESSAGE);
            return;
        }

        // Insert into database
        try (Connection connection = DriverManager.getConnection(url, username, password)) {
            String query = "INSERT INTO users (first_name, last_name, username, email, phone, password) VALUES (?, ?, ?, ?, ?, ?)";
            PreparedStatement preparedStatement = connection.prepareStatement(query);
            preparedStatement.setString(1, firstName);
            preparedStatement.setString(2, lastName);
            preparedStatement.setString(3, userName);
            preparedStatement.setString(4, email);
            preparedStatement.setString(5, phone);
            preparedStatement.setString(6, passwordInput); // Storing the password
            preparedStatement.executeUpdate();
            JOptionPane.showMessageDialog(this, "Customer added successfully!");
            loadCustomerData();
        } catch (SQLException e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(this, "Error adding customer: " + e.getMessage(),
                    "Database Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    // Method to edit a customer
    private void editCustomer() {
        int selectedRow = customerTable.getSelectedRow();
        if (selectedRow == -1) {
            JOptionPane.showMessageDialog(this, "Please select a customer to edit.", "No Selection", JOptionPane.WARNING_MESSAGE);
            return;
        }

        // Retrieve current values from the selected row
        String id = customerTable.getValueAt(selectedRow, 0).toString();
        String currentFirstName = customerTable.getValueAt(selectedRow, 1).toString();
        String currentLastName = customerTable.getValueAt(selectedRow, 2).toString();
        String currentUsername = customerTable.getValueAt(selectedRow, 3).toString();
        String currentEmail = customerTable.getValueAt(selectedRow, 4).toString();
        String currentPhone = customerTable.getValueAt(selectedRow, 5).toString();

        // Input fields pre-filled with current values
        JTextField firstNameField = new JTextField(currentFirstName);
        JTextField lastNameField = new JTextField(currentLastName);
        JTextField userNameField = new JTextField(currentUsername);
        JTextField emailField = new JTextField(currentEmail);
        JTextField phoneField = new JTextField(currentPhone);
        JPasswordField passwordField = new JPasswordField();
        JPasswordField confirmPasswordField = new JPasswordField();

        // Input panel
        JPanel inputPanel = new JPanel(new GridLayout(7, 2, 5, 5));
        inputPanel.add(new JLabel("First Name:"));
        inputPanel.add(firstNameField);
        inputPanel.add(new JLabel("Last Name:"));
        inputPanel.add(lastNameField);
        inputPanel.add(new JLabel("Username:"));
        inputPanel.add(userNameField);
        inputPanel.add(new JLabel("Email:"));
        inputPanel.add(emailField);
        inputPanel.add(new JLabel("Phone:"));
        inputPanel.add(phoneField);
        inputPanel.add(new JLabel("New Password (optional):"));
        inputPanel.add(passwordField);
        inputPanel.add(new JLabel("Confirm Password:"));
        inputPanel.add(confirmPasswordField);

        // Show dialog
        int result = JOptionPane.showConfirmDialog(this, inputPanel, "Edit Customer", JOptionPane.OK_CANCEL_OPTION);
        if (result != JOptionPane.OK_OPTION) {
            return; // User canceled
        }

        // Retrieve updated input
        String newFirstName = firstNameField.getText();
        String newLastName = lastNameField.getText();
        String newUsername = userNameField.getText();
        String newEmail = emailField.getText();
        String newPhone = phoneField.getText();
        String newPassword = new String(passwordField.getPassword());
        String confirmPassword = new String(confirmPasswordField.getPassword());

        // Validate input
        if (newFirstName.isEmpty() || newLastName.isEmpty() || newUsername.isEmpty() || newEmail.isEmpty() || newPhone.isEmpty()) {
            JOptionPane.showMessageDialog(this, "All fields except password are required.", "Validation Error", JOptionPane.ERROR_MESSAGE);
            return;
        }

        if (!newPassword.isEmpty() && !newPassword.equals(confirmPassword)) {
            JOptionPane.showMessageDialog(this, "Passwords do not match.", "Validation Error", JOptionPane.ERROR_MESSAGE);
            return;
        }

        String url = "jdbc:mysql://localhost:3306/hotelmanagement";
        String dbUsername = "root";
        String dbPassword = "root";

        try (Connection connection = DriverManager.getConnection(url, dbUsername, dbPassword)) {
            String query = "UPDATE users SET first_name = ?, last_name = ?, username = ?, email = ?, phone = ?" +
                    (newPassword.isEmpty() ? "" : ", password = ?") + " WHERE id = ?";
            PreparedStatement preparedStatement = connection.prepareStatement(query);

            preparedStatement.setString(1, newFirstName);
            preparedStatement.setString(2, newLastName);
            preparedStatement.setString(3, newUsername);
            preparedStatement.setString(4, newEmail);
            preparedStatement.setString(5, newPhone);
            if (!newPassword.isEmpty()) {
                preparedStatement.setString(6, newPassword);
                preparedStatement.setInt(7, Integer.parseInt(id));
            } else {
                preparedStatement.setInt(6, Integer.parseInt(id));
            }

            preparedStatement.executeUpdate();
            JOptionPane.showMessageDialog(this, "Customer updated successfully!");
            loadCustomerData();
        } catch (SQLException e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(this, "Error updating customer: " + e.getMessage(),
                    "Database Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    // Method to delete a customer
    private void deleteCustomer() {
        int selectedRow = customerTable.getSelectedRow();
        if (selectedRow == -1) {
            JOptionPane.showMessageDialog(this, "Please select a customer to delete.", "No Selection", JOptionPane.WARNING_MESSAGE);
            return;
        }

        String id = customerTable.getValueAt(selectedRow, 0).toString();
        int confirm = JOptionPane.showConfirmDialog(this, "Are you sure you want to delete this customer?", "Confirm Delete",
                JOptionPane.YES_NO_OPTION);

        if (confirm != JOptionPane.YES_OPTION) {
            return;
        }

        String url = "jdbc:mysql://localhost:3306/hotelmanagement";
        String username = "root";
        String password = "root";

        try (Connection connection = DriverManager.getConnection(url, username, password)) {
            String query = "DELETE FROM users WHERE id = ?";
            PreparedStatement preparedStatement = connection.prepareStatement(query);
            preparedStatement.setInt(1, Integer.parseInt(id));
            preparedStatement.executeUpdate();
            JOptionPane.showMessageDialog(this, "Customer deleted successfully!");
            loadCustomerData();
        } catch (SQLException e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(this, "Error deleting customer: " + e.getMessage(),
                    "Database Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    // Method to navigate to Admin Main Menu
    private void goToAdminMainMenu() {
        new AdminMainMenuFrame(admin); // Open the admin main menu
        dispose(); // Close current frame
    }

    // Main method for testing
    public static void main(String[] args) {
        User admin = new User("AdminUser", "adminPassword");
        EventQueue.invokeLater(() -> new ManageCustomerFrame(admin));
    }
}
