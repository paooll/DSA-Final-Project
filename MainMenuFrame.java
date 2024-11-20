package hotel;

import javax.swing.*;
import java.awt.*;

public class MainMenuFrame extends JFrame {
    private User user;

    public MainMenuFrame(User user) {
        this.user = user;

        setTitle("Hotel Management System - Main Menu");
        setExtendedState(JFrame.MAXIMIZED_BOTH); 
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);


        JPanel backgroundPanel = new JPanel();
        backgroundPanel.setBackground(new Color(120, 179, 206)); 
        backgroundPanel.setLayout(new BorderLayout());
        backgroundPanel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));


        JLabel titleLabel = new JLabel("Welcome to Grandview Hotel", JLabel.CENTER);
        titleLabel.setFont(new Font("Serif", Font.BOLD, 36));
        titleLabel.setForeground(new Color(0, 102, 204));
        titleLabel.setBorder(BorderFactory.createEmptyBorder(20, 0, 20, 0));
        backgroundPanel.add(titleLabel, BorderLayout.NORTH);


        JPanel centerPanel = new JPanel(new GridBagLayout());
        centerPanel.setOpaque(false); 
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(15, 15, 15, 15); 
        gbc.fill = GridBagConstraints.HORIZONTAL;


        JButton bookRoomButton = new JButton("Book a Room");
        bookRoomButton.setFont(new Font("Arial", Font.BOLD, 20));
        bookRoomButton.setBackground(new Color(0, 153, 76));
        bookRoomButton.setForeground(Color.WHITE);
        bookRoomButton.setIcon(resizeIcon(new ImageIcon("images/booking.png"), 40, 40)); 
        bookRoomButton.setPreferredSize(new Dimension(300, 80));
        bookRoomButton.addActionListener(e -> openRoomSearchFrame());
        gbc.gridx = 0;
        gbc.gridy = 0;
        centerPanel.add(bookRoomButton, gbc);


        JButton viewReservationsButton = new JButton("View My Reservations");
        viewReservationsButton.setFont(new Font("Arial", Font.BOLD, 20));
        viewReservationsButton.setBackground(new Color(0, 102, 204));
        viewReservationsButton.setForeground(Color.WHITE);
        viewReservationsButton.setIcon(resizeIcon(new ImageIcon("images/reservation.png"), 40, 40)); 
        viewReservationsButton.setPreferredSize(new Dimension(300, 80));
        viewReservationsButton.addActionListener(e -> openReservationsFrame());
        gbc.gridx = 0;
        gbc.gridy = 1;
        centerPanel.add(viewReservationsButton, gbc);


        JButton logoutButton = new JButton("Logout");
        logoutButton.setFont(new Font("Arial", Font.BOLD, 20));
        logoutButton.setBackground(new Color(204, 0, 0));
        logoutButton.setForeground(Color.WHITE);
        logoutButton.setIcon(resizeIcon(new ImageIcon("images/logout.png"), 40, 40)); 
        logoutButton.setPreferredSize(new Dimension(300, 80));
        logoutButton.addActionListener(e -> logout());
        gbc.gridx = 0;
        gbc.gridy = 2;
        centerPanel.add(logoutButton, gbc);


        backgroundPanel.add(centerPanel, BorderLayout.CENTER);

        add(backgroundPanel);
        setVisible(true);
    }

    private ImageIcon resizeIcon(ImageIcon icon, int width, int height) {
        Image img = icon.getImage();
        Image resizedImg = img.getScaledInstance(width, height, Image.SCALE_SMOOTH);
        return new ImageIcon(resizedImg);
    }

    private void openRoomSearchFrame() {
        new RoomSearchFrame(user); 
        this.setVisible(false); 
    }

    private void openReservationsFrame() {
        ReservationsFrame reservationsFrame = new ReservationsFrame(user, this);
        this.setVisible(false); 
    }

    private void logout() {
        new LoginFrame(new Authentication()); 
        dispose(); 
    }

    public static void main(String[] args) {

        User user = new User("JohnDoe", "password123");


        SwingUtilities.invokeLater(() -> new MainMenuFrame(user));
    }
}
