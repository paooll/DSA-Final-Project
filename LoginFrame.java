package hotel;

import javax.swing.*;
import java.awt.*;

class LoginFrame extends JFrame {
    private JTextField usernameField;
    private JPasswordField passwordField;
    private Authentication auth;

    public LoginFrame(Authentication auth) {
        this.auth = auth;

        setTitle("Hotel Management System - Login");
        setExtendedState(JFrame.MAXIMIZED_BOTH);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);

        JPanel backgroundPanel = new JPanel() {
            @Override
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                ImageIcon backgroundImage = new ImageIcon("images/login.jpg"); 
                Image image = backgroundImage.getImage();
                g.drawImage(image, 0, 0, getWidth(), getHeight(), this); 
            }
        };
        backgroundPanel.setLayout(new GridBagLayout());

        
        JPanel formPanel = new JPanel(new GridBagLayout());
        formPanel.setOpaque(false); 

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(10, 10, 10, 10);
        gbc.fill = GridBagConstraints.HORIZONTAL;

       
        JLabel titleLabel = new JLabel("Grandview Hotel", JLabel.CENTER);
        titleLabel.setFont(new Font("Serif", Font.BOLD, 36));
        titleLabel.setForeground(new Color(255, 255, 255)); 
        titleLabel.setOpaque(true);
        titleLabel.setBackground(new Color(0, 0, 0, 150)); 
        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.gridwidth = 2;
        formPanel.add(titleLabel, gbc);

        gbc.gridwidth = 1;

       
        JLabel usernameLabel = new JLabel("Username:");
        usernameLabel.setFont(new Font("Arial", Font.PLAIN, 16));
        usernameLabel.setForeground(Color.WHITE); 
        gbc.gridx = 0;
        gbc.gridy = 1;
        formPanel.add(usernameLabel, gbc);

        usernameField = new JTextField(15);
        usernameField.setFont(new Font("Arial", Font.PLAIN, 16));
        usernameField.setOpaque(true); 
        usernameField.setBackground(new Color(255, 255, 255, 200)); 
        usernameField.setForeground(Color.BLACK); 
        gbc.gridx = 1;
        gbc.gridy = 1;
        formPanel.add(usernameField, gbc);


        JLabel passwordLabel = new JLabel("Password:");
        passwordLabel.setFont(new Font("Arial", Font.PLAIN, 16));
        passwordLabel.setForeground(Color.WHITE); 
        gbc.gridx = 0;
        gbc.gridy = 2;
        formPanel.add(passwordLabel, gbc);

        passwordField = new JPasswordField(15);
        passwordField.setFont(new Font("Arial", Font.PLAIN, 16));
        passwordField.setOpaque(true); 
        passwordField.setBackground(new Color(255, 255, 255, 200)); 
        passwordField.setForeground(Color.BLACK); 
        gbc.gridx = 1;
        gbc.gridy = 2;
        formPanel.add(passwordField, gbc);


        gbc.gridx = 0;
        gbc.gridy = 3;
        gbc.gridwidth = 2;
        gbc.anchor = GridBagConstraints.CENTER;
        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 10, 10));
        buttonPanel.setOpaque(false); 

        JButton loginButton = new JButton("Login");
        loginButton.setFont(new Font("Arial", Font.BOLD, 16));
        loginButton.setBackground(new Color(0, 153, 76)); 
        loginButton.setForeground(Color.WHITE);
        loginButton.setPreferredSize(new Dimension(120, 40)); 
        loginButton.addActionListener(e -> {
            String username = usernameField.getText().trim();
            String password = new String(passwordField.getPassword()).trim();
            User user = new User(username, password);

            if (auth.validateCredentials(user)) {
                JOptionPane.showMessageDialog(this, "Login Successful", "Success", JOptionPane.INFORMATION_MESSAGE);
                if (user.isAdmin()) {
                    new AdminMainMenuFrame(user);
                } else {
                    new MainMenuFrame(user);
                }
                dispose();
            } else {
                JOptionPane.showMessageDialog(this, "Invalid Credentials", "Error", JOptionPane.ERROR_MESSAGE);
            }
        });
        buttonPanel.add(loginButton);

        JButton registerButton = new JButton("Register");
        registerButton.setFont(new Font("Arial", Font.BOLD, 16));
        registerButton.setBackground(new Color(0, 102, 204)); 
        registerButton.setForeground(Color.WHITE);
        registerButton.setPreferredSize(new Dimension(120, 40)); 
        registerButton.addActionListener(e -> {
            new RegisterFrame(auth);
            dispose();
        });
        buttonPanel.add(registerButton);

        formPanel.add(buttonPanel, gbc);

      
        backgroundPanel.add(formPanel);


        setContentPane(backgroundPanel);
        setVisible(true);
    }

    public static void main(String[] args) {
        Authentication auth = new Authentication();
        SwingUtilities.invokeLater(() -> new LoginFrame(auth));
    }
}
