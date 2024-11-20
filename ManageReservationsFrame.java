package hotel;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.sql.*;
import java.util.Calendar;
import java.util.Date;

public class ManageReservationsFrame extends JFrame {
    private User admin;
    private JTable reservationTable;
    private DefaultTableModel tableModel;

    public ManageReservationsFrame(User admin) {
        this.admin = admin;

        setTitle("Manage Reservations - Grandview Hotel");
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setExtendedState(JFrame.MAXIMIZED_BOTH); 
        setBounds(100, 100, 1000, 600);
        setLocationRelativeTo(null); 

        JPanel contentPane = new JPanel();
        contentPane.setBorder(new EmptyBorder(20, 20, 20, 20));
        contentPane.setLayout(new BorderLayout(10, 10));
        setContentPane(contentPane);


        JLabel titleLabel = new JLabel("Manage Reservations", JLabel.CENTER);
        titleLabel.setFont(new Font("Times New Roman", Font.BOLD, 36));
        titleLabel.setForeground(new Color(52, 73, 94));
        contentPane.add(titleLabel, BorderLayout.NORTH);


        String[] columnNames = {"Booking ID", "Username", "Room ID", "Room Number", "Check-In Date", "Check-Out Date", "Total Price"};
        tableModel = new DefaultTableModel(columnNames, 0); 
        reservationTable = new JTable(tableModel);
        reservationTable.getTableHeader().setFont(new Font("Times New Roman", Font.BOLD, 16));
        JScrollPane scrollPane = new JScrollPane(reservationTable);
        scrollPane.setBorder(BorderFactory.createLineBorder(new Color(189, 195, 199), 2));
        contentPane.add(scrollPane, BorderLayout.CENTER);


        JPanel buttonPanel = new JPanel();
        buttonPanel.setLayout(new FlowLayout(FlowLayout.CENTER, 15, 10));
        
        JButton addReservationButton = createButton("", "images/add.png");
        JButton editReservationButton = createButton("", "images/edit.png");
        JButton cancelReservationButton = createButton("", "images/delete.png");
        JButton backButton = createButton("", "images/back.png");
        
        buttonPanel.add(addReservationButton);
        buttonPanel.add(editReservationButton);
        buttonPanel.add(cancelReservationButton);
        buttonPanel.add(backButton);
        contentPane.add(buttonPanel, BorderLayout.SOUTH);


        addReservationButton.addActionListener(e -> addReservation());
        editReservationButton.addActionListener(e -> editReservation());
        cancelReservationButton.addActionListener(e -> deleteReservation());
        backButton.addActionListener(e -> goToAdminMainMenu());


        loadReservationData();

        setVisible(true);
    }


    private JButton createButton(String text, String iconPath) {
        JButton button = new JButton(text);
        button.setFont(new Font("Times New Roman", Font.PLAIN, 16));
        button.setBackground(new Color(46, 204, 113));
        button.setForeground(Color.WHITE);
        button.setFocusPainted(false);
        button.setBorder(BorderFactory.createEmptyBorder(10, 20, 10, 20));
        button.setIcon(new ImageIcon(new ImageIcon(iconPath).getImage().getScaledInstance(18, 18, Image.SCALE_SMOOTH)));
        return button;
    }


    private void loadReservationData() {
        String url = "jdbc:mysql://localhost:3306/hotelmanagement"; 
        String username = "root"; 
        String password = "root"; 

        try (Connection connection = DriverManager.getConnection(url, username, password)) {
            String query = "SELECT booking_id, username, room_id, room_number, check_in_date, check_out_date, total_price FROM bookings";
            Statement statement = connection.createStatement();
            ResultSet resultSet = statement.executeQuery(query);


            tableModel.setRowCount(0);


            while (resultSet.next()) {
                Object[] row = {
                        resultSet.getInt("booking_id"),
                        resultSet.getString("username"),
                        resultSet.getInt("room_id"),
                        resultSet.getString("room_number"),
                        resultSet.getDate("check_in_date"),
                        resultSet.getDate("check_out_date"),
                        resultSet.getDouble("total_price")
                };
                tableModel.addRow(row);
            }
        } catch (SQLException e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(this, "Error loading reservation data: " + e.getMessage(),
                    "Database Error", JOptionPane.ERROR_MESSAGE);
        }
    }


    private void addReservation() {
        JTextField usernameField = new JTextField();
        JTextField roomIdField = new JTextField();
        JTextField roomNumberField = new JTextField();
        JSpinner checkInDateSpinner = new JSpinner(new SpinnerDateModel());
        JSpinner checkOutDateSpinner = new JSpinner(new SpinnerDateModel());
        JTextField totalPriceField = new JTextField();

        JSpinner.DateEditor checkInEditor = new JSpinner.DateEditor(checkInDateSpinner, "yyyy-MM-dd");
        JSpinner.DateEditor checkOutEditor = new JSpinner.DateEditor(checkOutDateSpinner, "yyyy-MM-dd");
        checkInDateSpinner.setEditor(checkInEditor);
        checkOutDateSpinner.setEditor(checkOutEditor);

        JPanel panel = new JPanel(new GridLayout(6, 2, 5, 5));
        panel.add(new JLabel("Username:"));
        panel.add(usernameField);
        panel.add(new JLabel("Room ID:"));
        panel.add(roomIdField);
        panel.add(new JLabel("Room Number:"));
        panel.add(roomNumberField);
        panel.add(new JLabel("Check-In Date:"));
        panel.add(checkInDateSpinner);
        panel.add(new JLabel("Check-Out Date:"));
        panel.add(checkOutDateSpinner);
        panel.add(new JLabel("Total Price:"));
        panel.add(totalPriceField);

        int result = JOptionPane.showConfirmDialog(this, panel, "Add Reservation", JOptionPane.OK_CANCEL_OPTION);
        if (result == JOptionPane.OK_OPTION) {
            try (Connection connection = DriverManager.getConnection("jdbc:mysql://localhost:3306/hotelmanagement", "root", "root")) {
                String query = "INSERT INTO bookings (username, room_id, room_number, check_in_date, check_out_date, total_price) VALUES (?, ?, ?, ?, ?, ?)";
                PreparedStatement preparedStatement = connection.prepareStatement(query);
                preparedStatement.setString(1, usernameField.getText());
                preparedStatement.setInt(2, Integer.parseInt(roomIdField.getText()));
                preparedStatement.setString(3, roomNumberField.getText());
                preparedStatement.setDate(4, new java.sql.Date(((Date) checkInDateSpinner.getValue()).getTime()));
                preparedStatement.setDate(5, new java.sql.Date(((Date) checkOutDateSpinner.getValue()).getTime()));
                preparedStatement.setDouble(6, Double.parseDouble(totalPriceField.getText()));
                preparedStatement.executeUpdate();

                JOptionPane.showMessageDialog(this, "Reservation added successfully.");
                loadReservationData();
            } catch (SQLException e) {
                e.printStackTrace();
                JOptionPane.showMessageDialog(this, "Error adding reservation: " + e.getMessage(), "Database Error", JOptionPane.ERROR_MESSAGE);
            }
        }
    }


    private void editReservation() {
        int selectedRow = reservationTable.getSelectedRow();
        if (selectedRow == -1) {
            JOptionPane.showMessageDialog(this, "Please select a reservation to edit.", "No Selection", JOptionPane.WARNING_MESSAGE);
            return;
        }

        int bookingId = (int) tableModel.getValueAt(selectedRow, 0);
        String username = (String) tableModel.getValueAt(selectedRow, 1);
        int roomId = (int) tableModel.getValueAt(selectedRow, 2);
        String roomNumber = (String) tableModel.getValueAt(selectedRow, 3);
        Date checkInDate = (Date) tableModel.getValueAt(selectedRow, 4);
        Date checkOutDate = (Date) tableModel.getValueAt(selectedRow, 5);
        double totalPrice = (double) tableModel.getValueAt(selectedRow, 6);

        JTextField usernameField = new JTextField(username);
        JTextField roomIdField = new JTextField(String.valueOf(roomId));
        JTextField roomNumberField = new JTextField(roomNumber);
        JSpinner checkInDateSpinner = new JSpinner(new SpinnerDateModel(checkInDate, null, null, Calendar.DAY_OF_MONTH));
        JSpinner checkOutDateSpinner = new JSpinner(new SpinnerDateModel(checkOutDate, null, null, Calendar.DAY_OF_MONTH));
        JTextField totalPriceField = new JTextField(String.valueOf(totalPrice));

        JSpinner.DateEditor checkInEditor = new JSpinner.DateEditor(checkInDateSpinner, "yyyy-MM-dd");
        JSpinner.DateEditor checkOutEditor = new JSpinner.DateEditor(checkOutDateSpinner, "yyyy-MM-dd");
        checkInDateSpinner.setEditor(checkInEditor);
        checkOutDateSpinner.setEditor(checkOutEditor);

        JPanel panel = new JPanel(new GridLayout(6, 2, 5, 5));
        panel.add(new JLabel("Username:"));
        panel.add(usernameField);
        panel.add(new JLabel("Room ID:"));
        panel.add(roomIdField);
        panel.add(new JLabel("Room Number:"));
        panel.add(roomNumberField);
        panel.add(new JLabel("Check-In Date:"));
        panel.add(checkInDateSpinner);
        panel.add(new JLabel("Check-Out Date:"));
        panel.add(checkOutDateSpinner);
        panel.add(new JLabel("Total Price:"));
        panel.add(totalPriceField);

        int result = JOptionPane.showConfirmDialog(this, panel, "Edit Reservation", JOptionPane.OK_CANCEL_OPTION);
        if (result == JOptionPane.OK_OPTION) {
            try (Connection connection = DriverManager.getConnection("jdbc:mysql://localhost:3306/hotelmanagement", "root", "root")) {
                String query = "UPDATE bookings SET username = ?, room_id = ?, room_number = ?, check_in_date = ?, check_out_date = ?, total_price = ? WHERE booking_id = ?";
                PreparedStatement preparedStatement = connection.prepareStatement(query);
                preparedStatement.setString(1, usernameField.getText());
                preparedStatement.setInt(2, Integer.parseInt(roomIdField.getText()));
                preparedStatement.setString(3, roomNumberField.getText());
                preparedStatement.setDate(4, new java.sql.Date(((Date) checkInDateSpinner.getValue()).getTime()));
                preparedStatement.setDate(5, new java.sql.Date(((Date) checkOutDateSpinner.getValue()).getTime()));
                preparedStatement.setDouble(6, Double.parseDouble(totalPriceField.getText()));
                preparedStatement.setInt(7, bookingId);
                preparedStatement.executeUpdate();

                JOptionPane.showMessageDialog(this, "Reservation updated successfully.");
                loadReservationData();
            } catch (SQLException e) {
                e.printStackTrace();
                JOptionPane.showMessageDialog(this, "Error updating reservation: " + e.getMessage(), "Database Error", JOptionPane.ERROR_MESSAGE);
            }
        }
    }


    private void deleteReservation() {
        int selectedRow = reservationTable.getSelectedRow();
        if (selectedRow == -1) {
            JOptionPane.showMessageDialog(this, "Please select a reservation to delete.", "No Selection", JOptionPane.WARNING_MESSAGE);
            return;
        }

        int bookingId = (int) tableModel.getValueAt(selectedRow, 0);
        int confirm = JOptionPane.showConfirmDialog(this, "Are you sure you want to delete this reservation?", "Confirm Deletion", JOptionPane.YES_NO_OPTION);
        if (confirm == JOptionPane.YES_OPTION) {
            try (Connection connection = DriverManager.getConnection("jdbc:mysql://localhost:3306/hotelmanagement", "root", "root")) {
                String query = "DELETE FROM bookings WHERE booking_id = ?";
                PreparedStatement preparedStatement = connection.prepareStatement(query);
                preparedStatement.setInt(1, bookingId);
                preparedStatement.executeUpdate();

                JOptionPane.showMessageDialog(this, "Reservation deleted successfully.");
                loadReservationData();
            } catch (SQLException e) {
                e.printStackTrace();
                JOptionPane.showMessageDialog(this, "Error deleting reservation: " + e.getMessage(), "Database Error", JOptionPane.ERROR_MESSAGE);
            }
        }
    }


    private void goToAdminMainMenu() {
        new AdminMainMenuFrame(admin); 
        dispose();
    }


    public static void main(String[] args) {
        User admin = new User("AdminUser", "adminPassword");
        EventQueue.invokeLater(() -> new ManageReservationsFrame(admin));
    }
}
