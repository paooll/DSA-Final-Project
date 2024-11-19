package hotel;

import javax.swing.*;
import java.awt.*;

public class AdminMainMenuFrame extends JFrame {
    private User admin;

    public AdminMainMenuFrame(User admin) {
        this.admin = admin;

        setTitle("Hotel Management System - Admin Main Menu");
        setExtendedState(JFrame.MAXIMIZED_BOTH);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);

        JPanel mainPanel = new JPanel(new BorderLayout());
        mainPanel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));

        JLabel titleLabel = new JLabel("Admin Dashboard - Grandview Hotel", JLabel.CENTER);
        titleLabel.setFont(new Font("Serif", Font.BOLD, 36));
        titleLabel.setForeground(new Color(128, 0, 128));
        titleLabel.setBorder(BorderFactory.createEmptyBorder(20, 0, 20, 0));
        mainPanel.add(titleLabel, BorderLayout.NORTH);

        JPanel centerPanel = new JPanel(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(15, 15, 15, 15);
        gbc.fill = GridBagConstraints.HORIZONTAL;


        JButton manageCustomersButton = new JButton("Manage Customers");
        manageCustomersButton.setFont(new Font("Arial", Font.BOLD, 20));
        manageCustomersButton.setBackground(new Color(0, 102, 204));
        manageCustomersButton.setForeground(Color.WHITE);
        manageCustomersButton.setIcon(resizeIcon(new ImageIcon("images/manageU.png"), 40, 40));
        manageCustomersButton.setPreferredSize(new Dimension(300, 80));
        manageCustomersButton.addActionListener(e -> openManageCustomersFrame());
        gbc.gridx = 0;
        gbc.gridy = 0;
        centerPanel.add(manageCustomersButton, gbc);


        JButton viewReservationsButton = new JButton("Manage Reservations");
        viewReservationsButton.setFont(new Font("Arial", Font.BOLD, 20));
        viewReservationsButton.setBackground(new Color(0, 102, 204));
        viewReservationsButton.setForeground(Color.WHITE);
        viewReservationsButton.setIcon(resizeIcon(new ImageIcon("images/manageR.png"), 40, 40));
        viewReservationsButton.setPreferredSize(new Dimension(300, 80));
        viewReservationsButton.addActionListener(e -> openManageReservationsFrame());
        gbc.gridx = 0;
        gbc.gridy = 1;
        centerPanel.add(viewReservationsButton, gbc);


        JButton manageRoomsButton = new JButton("Manage Rooms");
        manageRoomsButton.setFont(new Font("Arial", Font.BOLD, 20));
        manageRoomsButton.setBackground(new Color(0, 102, 204));
        manageRoomsButton.setForeground(Color.WHITE);
        manageRoomsButton.setIcon(resizeIcon(new ImageIcon("images/manageRooms.png"), 40, 40));
        manageRoomsButton.setPreferredSize(new Dimension(300, 80));
        manageRoomsButton.addActionListener(e -> openManageRoomsFrame());
        gbc.gridx = 0;
        gbc.gridy = 2; 
        centerPanel.add(manageRoomsButton, gbc);


        JButton logoutButton = new JButton("Logout");
        logoutButton.setFont(new Font("Arial", Font.BOLD, 20));
        logoutButton.setBackground(new Color(204, 0, 0));
        logoutButton.setForeground(Color.WHITE);
        logoutButton.setIcon(resizeIcon(new ImageIcon("images/logout.png"), 40, 40));
        logoutButton.setPreferredSize(new Dimension(300, 80));
        logoutButton.addActionListener(e -> logout());
        gbc.gridx = 0;
        gbc.gridy = 3;
        centerPanel.add(logoutButton, gbc);

        mainPanel.add(centerPanel, BorderLayout.CENTER);

        add(mainPanel);
        setVisible(true);
    }

    private ImageIcon resizeIcon(ImageIcon icon, int width, int height) {
        Image img = icon.getImage();
        Image resizedImg = img.getScaledInstance(width, height, Image.SCALE_SMOOTH);
        return new ImageIcon(resizedImg);
    }

    private void openManageCustomersFrame() {

        new ManageCustomerFrame(admin);
        dispose(); 
    }

    private void openManageReservationsFrame() {

        new ManageReservationsFrame(admin);
        dispose(); 
    }

    private void openManageRoomsFrame() {

        new ManageRoomsFrame(admin);
        dispose();
    }

    private void logout() {
        int response = JOptionPane.showConfirmDialog(this, "Are you sure you want to log out?", "Confirm Logout", JOptionPane.YES_NO_OPTION);
        if (response == JOptionPane.YES_OPTION) {
            new LoginFrame(new Authentication());
            dispose(); 
        }
    }

    public static void main(String[] args) {

        User admin = new User("AdminUser", "adminPassword");
        admin.setAdmin(true); 


        SwingUtilities.invokeLater(() -> new AdminMainMenuFrame(admin));
    }
}
