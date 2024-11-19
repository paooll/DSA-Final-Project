package hotel;

import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.sql.*;
import java.util.ArrayList;
import com.toedter.calendar.JDateChooser;

class RoomSearchFrame extends JFrame {
    private ArrayList<Room> searchResults;
    private User user;
    private JList<String> roomList;
    private DefaultListModel<String> listModel;
    private JLabel priceLabel;
    private JDateChooser checkInDateChooser, checkOutDateChooser;

    private static final String DB_URL = "jdbc:mysql://localhost:3306/hotelmanagement";
    private static final String DB_USER = "root";
    private static final String DB_PASSWORD = "root";

    public RoomSearchFrame(User user) {
        this.user = user;

        setTitle("Grandview Hotel - Room Search");
        setExtendedState(JFrame.MAXIMIZED_BOTH);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);

        JPanel mainPanel = new JPanel(new BorderLayout());
        mainPanel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));

        JPanel formPanel = createFormPanel();
        mainPanel.add(formPanel, BorderLayout.WEST);

        JPanel roomCardPanel = createRoomCardPanel();
        mainPanel.add(roomCardPanel, BorderLayout.NORTH);

        listModel = new DefaultListModel<>();
        roomList = new JList<>(listModel);
        roomList.setFont(new Font("Arial", Font.PLAIN, 16));
        JScrollPane scrollPane = new JScrollPane(roomList);
        mainPanel.add(scrollPane, BorderLayout.CENTER);

        roomList.addMouseListener(new MouseAdapter() {
            public void mouseClicked(MouseEvent e) {
                if (e.getClickCount() == 2) { 
                    int index = roomList.locationToIndex(e.getPoint());
                    if (index >= 0 && index < searchResults.size()) {
                        Room selectedRoom = searchResults.get(index);
                        proceedToPayment(selectedRoom);
                    }
                }
            }
        });

        add(mainPanel);
        setVisible(true);
    }

    private JPanel createFormPanel() {
        JPanel formPanel = new JPanel();
        formPanel.setLayout(new BoxLayout(formPanel, BoxLayout.Y_AXIS));
        formPanel.setBorder(BorderFactory.createEmptyBorder(0, 0, 0, 20));

        JLabel titleLabel = new JLabel("Grandview Hotel - Room Search", JLabel.CENTER);
        titleLabel.setFont(new Font("Serif", Font.BOLD, 36));
        titleLabel.setForeground(new Color(0, 102, 204));
        titleLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
        formPanel.add(titleLabel);
        formPanel.add(Box.createVerticalStrut(20));

        checkInDateChooser = new JDateChooser();
        checkOutDateChooser = new JDateChooser();
        String[] roomTypes = {"Single", "Double", "Suite"};
        JComboBox<String> typeComboBox = new JComboBox<>(roomTypes);

        formPanel.add(createLabeledPanel("Check-in Date:", checkInDateChooser));
        formPanel.add(createLabeledPanel("Check-out Date:", checkOutDateChooser));
        formPanel.add(createLabeledPanel("Room Type:", typeComboBox));

        priceLabel = new JLabel("Total Price:");
        formPanel.add(createLabeledPanel("Total Price:", priceLabel));

        JButton searchButton = new JButton("Search Rooms");
        searchButton.addActionListener(e -> performSearch(checkInDateChooser, checkOutDateChooser, typeComboBox, priceLabel));
        formPanel.add(searchButton);

        JButton backButton = new JButton("Back");
        backButton.addActionListener(e -> {
            dispose();
            new MainMenuFrame(user);
        });
        formPanel.add(backButton);

        return formPanel;
    }

    private JPanel createRoomCardPanel() {
        JPanel roomCardPanel = new JPanel(new GridLayout(1, 3, 20, 20));
        roomCardPanel.setBorder(BorderFactory.createTitledBorder("Featured Rooms"));

        String[][] featuredRooms = {
            {"Single", "$100/night", "images/single.jpg"},
            {"Double", "$150/night", "images/double.jpg"},
            {"Suite", "$250/night", "images/suite.jpg"}
        };

        for (String[] room : featuredRooms) {
            JPanel card = new JPanel();
            card.setPreferredSize(new Dimension(300, 200));
            card.setLayout(new BorderLayout());
            card.setBorder(BorderFactory.createLineBorder(Color.GRAY, 1));

            JLabel roomImageLabel = new JLabel();
            roomImageLabel.setHorizontalAlignment(JLabel.CENTER);
            roomImageLabel.setIcon(new ImageIcon(new ImageIcon(room[2]).getImage().getScaledInstance(300, 150, Image.SCALE_SMOOTH)));  // Room image
            card.add(roomImageLabel, BorderLayout.CENTER);

            JLabel roomDetailsLabel = new JLabel("Room Type: " + room[0] + "	Price: " + room[1], JLabel.CENTER);
            roomDetailsLabel.setFont(new Font("Arial", Font.PLAIN, 14));
            card.add(roomDetailsLabel, BorderLayout.SOUTH);

            roomCardPanel.add(card);
        }

        return roomCardPanel;
    }

    private JPanel createLabeledPanel(String labelText, JComponent component) {
        JPanel panel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        JLabel label = new JLabel(labelText);
        label.setFont(new Font("Arial", Font.PLAIN, 16));
        panel.add(label);
        panel.add(component);
        return panel;
    }

    private void performSearch(JDateChooser checkInDateChooser, JDateChooser checkOutDateChooser, JComboBox<String> typeComboBox, JLabel priceLabel) {
        if (checkInDateChooser.getDate() == null || checkOutDateChooser.getDate() == null) {
            JOptionPane.showMessageDialog(this, "Please select both check-in and check-out dates.", "Input Error", JOptionPane.ERROR_MESSAGE);
            return;
        }

        String type = (String) typeComboBox.getSelectedItem();
        double basePrice = switch (type) {
            case "Single" -> 100;
            case "Double" -> 150;
            case "Suite" -> 250;
            default -> 0;
        };

        long days = calculateDaysBetween(checkInDateChooser.getDate(), checkOutDateChooser.getDate());
        double totalPrice = basePrice * days;

        ArrayList<Room> availableRooms = getAvailableRooms(type, totalPrice);

        System.out.println("Available Rooms: " + availableRooms.size()); 
        updateRoomList(availableRooms);
        searchResults = availableRooms;
        priceLabel.setText("Total Price: $" + totalPrice);
    }

    private long calculateDaysBetween(java.util.Date startDate, java.util.Date endDate) {
        long diffInMillies = Math.abs(endDate.getTime() - startDate.getTime());
        return diffInMillies / (1000 * 60 * 60 * 24);
    }

    private ArrayList<Room> getAvailableRooms(String type, double priceLimit) {
        ArrayList<Room> availableRooms = new ArrayList<>();
        String sql = "SELECT id, room_type, price, is_available, room_number FROM rooms WHERE room_type = ? AND price <= ? AND is_available = TRUE";

        try (Connection conn = getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, type);
            stmt.setDouble(2, priceLimit);
            ResultSet rs = stmt.executeQuery();

            while (rs.next()) {
                int roomId = rs.getInt("id");
                String roomType = rs.getString("room_type");
                double price = rs.getDouble("price");
                boolean isAvailable = rs.getBoolean("is_available");
                String roomNumber = rs.getString("room_number");

                availableRooms.add(new Room(roomId, roomType, price, isAvailable, roomNumber));

                System.out.println("Room Found: ID = " + roomId + ", Type = " + roomType + " - " + roomNumber);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return availableRooms;
    }

    private void updateRoomList(ArrayList<Room> availableRooms) {
        listModel.clear();
        for (Room room : availableRooms) {
            listModel.addElement(room.getType() + " - " + room.getRoomNumber());
            System.out.println("Added to List: " + room.getType() + " - " + room.getRoomNumber()); // Debugging
        }
    }

    private void proceedToPayment(Room selectedRoom) {
        if (selectedRoom == null || selectedRoom.getId() == 0) {
            JOptionPane.showMessageDialog(this, "Room ID is not set. Please verify the room selection.", "Room Verification Error", JOptionPane.ERROR_MESSAGE);
            return;
        }

        if (checkInDateChooser.getDate() == null || checkOutDateChooser.getDate() == null) {
            JOptionPane.showMessageDialog(this, "Please select valid check-in and check-out dates.", "Date Selection Error", JOptionPane.ERROR_MESSAGE);
            return;
        }

        double totalPrice = Double.parseDouble(priceLabel.getText().replace("Total Price: $", ""));


        PaymentFrame paymentFrame = new PaymentFrame(user, selectedRoom, totalPrice);


        paymentFrame.setPaymentSuccessCallback(() -> {

            saveBookingToDatabase(selectedRoom, totalPrice);


            updateRoomStatus(selectedRoom);


            sendNotification("Booking confirmed for " + selectedRoom.getType() + " - " + selectedRoom.getRoomNumber());


            JOptionPane.showMessageDialog(this, "Booking confirmed! Your room is reserved.", "Success", JOptionPane.INFORMATION_MESSAGE);
        });

        paymentFrame.setVisible(true);
    }




    private void saveBookingToDatabase(Room selectedRoom, double totalPrice) {
        if (selectedRoom == null) {
            JOptionPane.showMessageDialog(this, "No room selected for booking.", "Database Error", JOptionPane.ERROR_MESSAGE);
            return;
        }

        java.util.Date checkInDate = checkInDateChooser.getDate();
        java.util.Date checkOutDate = checkOutDateChooser.getDate();

        if (checkInDate == null || checkOutDate == null) {
            JOptionPane.showMessageDialog(this, "Please select valid check-in and check-out dates.", "Date Selection Error", JOptionPane.ERROR_MESSAGE);
            return;
        }

        String sql = "INSERT INTO bookings (room_id, room_number, check_in_date, check_out_date, total_price, username) VALUES (?, ?, ?, ?, ?, ?)";

        try (Connection conn = DriverManager.getConnection(DB_URL, DB_USER, DB_PASSWORD);
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setInt(1, selectedRoom.getId());
            stmt.setString(2, selectedRoom.getRoomNumber());
            stmt.setDate(3, new java.sql.Date(checkInDate.getTime()));
            stmt.setDate(4, new java.sql.Date(checkOutDate.getTime()));
            stmt.setDouble(5, totalPrice);
            stmt.setString(6, user.getUsername());

            stmt.executeUpdate();
            JOptionPane.showMessageDialog(this, "Booking saved successfully.", "Success", JOptionPane.INFORMATION_MESSAGE);
        } catch (SQLException e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(this, "Error saving booking to database.", "Database Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void updateRoomStatus(Room selectedRoom) {
        String sql = "UPDATE rooms SET is_available = FALSE WHERE id = ?";

        try (Connection conn = getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setInt(1, selectedRoom.getId());
            stmt.executeUpdate();
            System.out.println("Room status updated to unavailable for room ID: " + selectedRoom.getId());
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    private void refreshAvailableRooms(String type, java.util.Date checkInDate, java.util.Date checkOutDate) {
        double basePrice = switch (type) {
            case "Single" -> 100;
            case "Double" -> 150;
            case "Suite" -> 250;
            default -> 0;
        };

        long days = calculateDaysBetween(checkInDate, checkOutDate);
        double totalPrice = basePrice * days;

        ArrayList<Room> availableRooms = getAvailableRooms(type, totalPrice);
        updateRoomList(availableRooms);
        searchResults = availableRooms;
    }

    private Connection getConnection() throws SQLException {
        return DriverManager.getConnection(DB_URL, DB_USER, DB_PASSWORD);
    }

    private void sendNotification(String message) {
        try {
            NotificationService.sendNotification(message);
        } catch (Exception ex) {
            System.out.println("Notification Service error: " + ex.getMessage());
        }
    }
}
