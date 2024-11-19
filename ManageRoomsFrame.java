package hotel;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class ManageRoomsFrame extends JFrame {
    private static final long serialVersionUID = 1L;
    private JPanel contentPane;
    private User admin;
    private JTable roomTable;

    public ManageRoomsFrame(User admin) {
        this.admin = admin;

        setTitle("Manage Rooms - Admin Dashboard");
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setExtendedState(JFrame.MAXIMIZED_BOTH);
        setBounds(100, 100, 1000, 600);
        setLocationRelativeTo(null);

        contentPane = new JPanel();
        contentPane.setBorder(new EmptyBorder(20, 20, 20, 20));
        contentPane.setLayout(new BorderLayout(10, 10));
        setContentPane(contentPane);


        JLabel titleLabel = new JLabel("Manage Rooms", JLabel.CENTER);
        titleLabel.setFont(new Font("Times New Roman", Font.BOLD, 36));
        titleLabel.setForeground(new Color(52, 73, 94));
        contentPane.add(titleLabel, BorderLayout.NORTH);


        roomTable = new JTable();
        roomTable.getTableHeader().setFont(new Font("Times New Roman", Font.BOLD, 16));
        roomTable.setFont(new Font("Times New Roman", Font.PLAIN, 14));
        JScrollPane scrollPane = new JScrollPane(roomTable);
        scrollPane.setBorder(BorderFactory.createLineBorder(new Color(189, 195, 199), 2));
        contentPane.add(scrollPane, BorderLayout.CENTER);


        JPanel buttonPanel = new JPanel();
        buttonPanel.setLayout(new FlowLayout(FlowLayout.CENTER, 15, 10));

        JButton toggleAvailabilityButton = createButton("", "images/toggle.png");
        JButton backButton = createButton("", "images/back.png");

        buttonPanel.add(toggleAvailabilityButton);
        buttonPanel.add(backButton);
        contentPane.add(buttonPanel, BorderLayout.SOUTH);


        toggleAvailabilityButton.addActionListener(e -> toggleRoomAvailability());
        backButton.addActionListener(e -> goToAdminMainMenu());


        loadRoomData();

        setVisible(true);
    }


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


    private void loadRoomData() {
        String url = "jdbc:mysql://localhost:3306/hotelmanagement";
        String username = "root";
        String password = "root";

        try (Connection connection = DriverManager.getConnection(url, username, password)) {
            String query = "SELECT room_number, room_type, price, is_available FROM rooms";
            Statement statement = connection.createStatement();
            ResultSet resultSet = statement.executeQuery(query);


            List<String> columnNames = new ArrayList<>();
            columnNames.add("Room Number");
            columnNames.add("Room Type");
            columnNames.add("Price");
            columnNames.add("Availability");


            List<Object[]> data = new ArrayList<>();
            while (resultSet.next()) {
                Object[] row = new Object[4];
                row[0] = resultSet.getString("room_number");
                row[1] = resultSet.getString("room_type");
                row[2] = resultSet.getString("price");
                row[3] = resultSet.getBoolean("is_available") ? "Available" : "Not Available";
                data.add(row);
            }


            DefaultTableModel model = new DefaultTableModel(data.toArray(new Object[0][0]), columnNames.toArray());
            roomTable.setModel(model);
        } catch (SQLException e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(this, "Error loading room data: " + e.getMessage(),
                    "Database Error", JOptionPane.ERROR_MESSAGE);
        }
    }


    private void toggleRoomAvailability() {
        int selectedRow = roomTable.getSelectedRow();
        if (selectedRow == -1) {
            JOptionPane.showMessageDialog(this, "Please select a room to toggle availability.", "No Selection", JOptionPane.WARNING_MESSAGE);
            return;
        }

        String roomNumber = roomTable.getValueAt(selectedRow, 0).toString();
        String currentAvailability = roomTable.getValueAt(selectedRow, 3).toString();
        boolean newAvailability = currentAvailability.equals("Not Available");

        String url = "jdbc:mysql://localhost:3306/hotelmanagement";
        String username = "root";
        String password = "root";

        try (Connection connection = DriverManager.getConnection(url, username, password)) {
            String query = "UPDATE rooms SET is_available = ? WHERE room_number = ?";
            PreparedStatement preparedStatement = connection.prepareStatement(query);
            preparedStatement.setBoolean(1, newAvailability);
            preparedStatement.setString(2, roomNumber);
            preparedStatement.executeUpdate();
            JOptionPane.showMessageDialog(this, "Room availability updated successfully!");
            loadRoomData();
        } catch (SQLException e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(this, "Error updating room availability: " + e.getMessage(),
                    "Database Error", JOptionPane.ERROR_MESSAGE);
        }
    }


    private void goToAdminMainMenu() {
        new AdminMainMenuFrame(admin); 
        dispose(); 
    }


    public static void main(String[] args) {
        User admin = new User("AdminUser", "adminPassword");
        EventQueue.invokeLater(() -> new ManageRoomsFrame(admin));
    }
}
