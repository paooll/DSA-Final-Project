package hotel;

import javax.swing.*;
import java.awt.*;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class ReservationsFrame extends JFrame {
    private User user;
    private MainMenuFrame mainMenuFrame; // Add reference to MainMenuFrame

    public ReservationsFrame(User user, MainMenuFrame mainMenuFrame) {
        if (user == null) {
            throw new IllegalArgumentException("User cannot be null");
        }
        this.user = user;
        this.mainMenuFrame = mainMenuFrame; // Set MainMenuFrame reference

        setTitle("My Reservations");
        setExtendedState(JFrame.MAXIMIZED_BOTH);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);

        // Main panel setup
        JPanel mainPanel = new JPanel(new BorderLayout(10, 10));
        mainPanel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));
        add(mainPanel);

        // Title label
        JLabel titleLabel = new JLabel("My Reservations", JLabel.CENTER);
        titleLabel.setFont(new Font("Serif", Font.BOLD, 28));
        titleLabel.setForeground(new Color(0, 102, 204));
        mainPanel.add(titleLabel, BorderLayout.NORTH);

        // Retrieve and display the user's reservations
        List<Booking> userBookings = getUserBookings();
        DefaultListModel<String> listModel = new DefaultListModel<>();
        if (userBookings.isEmpty()) {
            listModel.addElement("No reservations found.");
        } else {
            for (Booking booking : userBookings) {
                listModel.addElement(booking.toString());
            }
        }

        // Reservation list styling with custom font
        JList<String> reservationList = new JList<>(listModel);
        reservationList.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        reservationList.setBackground(new Color(240, 248, 255));
        reservationList.setBorder(BorderFactory.createLineBorder(new Color(0, 102, 204), 1));
        reservationList.setCellRenderer(new DefaultListCellRenderer() {
            @Override
            public Component getListCellRendererComponent(JList<?> list, Object value, int index, boolean isSelected, boolean cellHasFocus) {
                JLabel renderer = (JLabel) super.getListCellRendererComponent(list, value, index, isSelected, cellHasFocus);
                renderer.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
                renderer.setOpaque(true);
                renderer.setBackground(isSelected ? new Color(135, 206, 250) : new Color(240, 248, 255));
                renderer.setForeground(Color.DARK_GRAY);
                renderer.setFont(new Font("Verdana", Font.PLAIN, 16)); // Set custom font here
                return renderer;
            }
        });

        JScrollPane scrollPane = new JScrollPane(reservationList);
        scrollPane.setBorder(BorderFactory.createTitledBorder("Your Reservations"));
        mainPanel.add(scrollPane, BorderLayout.CENTER);

        // Back button panel
        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 10, 10));
        JButton backButton = new JButton("Back");
        backButton.addActionListener(e -> goBackToMainMenu()); // Set action to go back to MainMenuFrame
        buttonPanel.add(backButton);

        mainPanel.add(buttonPanel, BorderLayout.SOUTH);

        setVisible(true);
    }

    private List<Booking> getUserBookings() {
        List<Booking> userBookings = new ArrayList<>();
        String sql = "SELECT room_number, check_in_date, check_out_date, total_price FROM bookings WHERE username = ?";

        try (Connection conn = DriverManager.getConnection("jdbc:mysql://localhost:3306/hotelmanagement", "root", "root");
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, user.getUsername()); // Assuming the User object has a getUsername() method
            ResultSet rs = stmt.executeQuery();

            while (rs.next()) {
                String roomNumber = rs.getString("room_number");
                Date checkInDate = rs.getDate("check_in_date");
                Date checkOutDate = rs.getDate("check_out_date");
                double totalPrice = rs.getDouble("total_price");

                Booking booking = new Booking(user, roomNumber, checkInDate, checkOutDate, totalPrice);
                userBookings.add(booking);
            }
        } catch (SQLException e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(this, "Error retrieving reservations from database.", "Database Error", JOptionPane.ERROR_MESSAGE);
        }

        return userBookings;
    }



    private void goBackToMainMenu() {
        this.dispose(); // Close ReservationsFrame
        mainMenuFrame.setVisible(true); // Show MainMenuFrame
    }
}
