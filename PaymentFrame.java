package hotel;

import java.awt.*;
import javax.swing.*;

public class PaymentFrame extends JFrame {
    private Room room;
    private double amount;
    private User user;

    private JRadioButton creditCardRadio;
    private JRadioButton gcashRadio;
    private JRadioButton paypalRadio;
    private JTextField cardNumberField;
    private JTextField expiryDateField;
    private JTextField cvvField;
    private JTextField gcashNumberField;
    private JTextField paypalEmailField;
    private JLabel amountLabel;
    private Runnable paymentSuccessCallback;

    public PaymentFrame(User user, Room room, double amount) {
        this.user = user;
        this.room = room;
        this.amount = amount;

        setTitle("Grandview Hotel - Payment Information");
        setExtendedState(JFrame.MAXIMIZED_BOTH);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setLocationRelativeTo(null);


        JPanel mainPanel = new JPanel(new BorderLayout());
        mainPanel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));

        JPanel formPanel = new JPanel(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(10, 10, 10, 10);
        gbc.fill = GridBagConstraints.HORIZONTAL;


        JLabel titleLabel = new JLabel("Grandview Hotel - Payment", JLabel.CENTER);
        titleLabel.setFont(new Font("Serif", Font.BOLD, 36));
        titleLabel.setForeground(new Color(0, 102, 204));
        titleLabel.setBorder(BorderFactory.createEmptyBorder(20, 0, 20, 0));
        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.gridwidth = 2;
        formPanel.add(titleLabel, gbc);


        JLabel paymentMethodLabel = new JLabel("Select Payment Method:");
        paymentMethodLabel.setFont(new Font("Arial", Font.PLAIN, 16));
        gbc.gridwidth = 1;
        gbc.gridx = 0;
        gbc.gridy = 1;
        formPanel.add(paymentMethodLabel, gbc);

        creditCardRadio = new JRadioButton("Credit Card");
        gcashRadio = new JRadioButton("GCash");
        paypalRadio = new JRadioButton("PayPal");

        ButtonGroup paymentGroup = new ButtonGroup();
        paymentGroup.add(creditCardRadio);
        paymentGroup.add(gcashRadio);
        paymentGroup.add(paypalRadio);

        gbc.gridx = 1;
        formPanel.add(creditCardRadio, gbc);
        gbc.gridy = 2;
        formPanel.add(gcashRadio, gbc);
        gbc.gridy = 3;
        formPanel.add(paypalRadio, gbc);


        creditCardRadio.setSelected(true);


        gbc.gridx = 0;
        gbc.gridy = 4;
        gbc.gridwidth = 2;
        amountLabel = new JLabel("Total Amount: $" + amount, JLabel.CENTER);
        amountLabel.setFont(new Font("Arial", Font.BOLD, 18));
        amountLabel.setForeground(new Color(0, 153, 76));
        formPanel.add(amountLabel, gbc);


        setupPaymentFields(formPanel, gbc);


        gbc.gridy++;
        gbc.gridx = 0;
        gbc.gridwidth = 2;
        JButton payButton = new JButton("Pay $" + amount);
        payButton.setFont(new Font("Arial", Font.BOLD, 16));
        payButton.setBackground(new Color(0, 153, 76));
        payButton.setForeground(Color.WHITE);
        payButton.addActionListener(e -> processPayment());
        formPanel.add(payButton, gbc);

        mainPanel.add(formPanel, BorderLayout.CENTER);
        add(mainPanel);
        setVisible(true);


        creditCardRadio.addActionListener(e -> updatePaymentFields());
        gcashRadio.addActionListener(e -> updatePaymentFields());
        paypalRadio.addActionListener(e -> updatePaymentFields());

        updatePaymentFields();
    }

    private void setupPaymentFields(JPanel formPanel, GridBagConstraints gbc) {
        gbc.gridwidth = 1;
        gbc.gridy = 5;


        JLabel cardNumberLabel = new JLabel("Card Number:");
        formPanel.add(cardNumberLabel, gbc);
        cardNumberField = new JTextField(15);
        gbc.gridx = 1;
        formPanel.add(cardNumberField, gbc);

        gbc.gridy++;
        gbc.gridx = 0;
        JLabel expiryDateLabel = new JLabel("Expiry Date:");
        formPanel.add(expiryDateLabel, gbc);
        expiryDateField = new JTextField(15);
        gbc.gridx = 1;
        formPanel.add(expiryDateField, gbc);

        gbc.gridy++;
        gbc.gridx = 0;
        JLabel cvvLabel = new JLabel("CVV:");
        formPanel.add(cvvLabel, gbc);
        cvvField = new JTextField(5);
        gbc.gridx = 1;
        formPanel.add(cvvField, gbc);


        gbc.gridy++;
        gbc.gridx = 0;
        JLabel gcashNumberLabel = new JLabel("GCash Number:");
        formPanel.add(gcashNumberLabel, gbc);
        gcashNumberField = new JTextField(15);
        gbc.gridx = 1;
        formPanel.add(gcashNumberField, gbc);


        gbc.gridy++;
        gbc.gridx = 0;
        JLabel paypalEmailLabel = new JLabel("PayPal Email:");
        formPanel.add(paypalEmailLabel, gbc);
        paypalEmailField = new JTextField(15);
        gbc.gridx = 1;
        formPanel.add(paypalEmailField, gbc);
    }

    private void processPayment() {
        String selectedMethod = getSelectedPaymentMethod();

        if (selectedMethod.equals("Credit Card") &&
                (cardNumberField.getText().isEmpty() || expiryDateField.getText().isEmpty() || cvvField.getText().isEmpty())) {
            JOptionPane.showMessageDialog(this, "Please fill in all credit card fields.", "Incomplete Information", JOptionPane.WARNING_MESSAGE);
            return;
        } else if (selectedMethod.equals("GCash")) {
            String gcashNumber = gcashNumberField.getText();
            if (gcashNumber.isEmpty() || !gcashNumber.matches("09\\d{9}")) {
                JOptionPane.showMessageDialog(this, "Please enter a valid GCash number. It should be 11 digits starting with '09'.", "Invalid GCash Number", JOptionPane.WARNING_MESSAGE);
                return;
            }
        } else if (selectedMethod.equals("PayPal") && paypalEmailField.getText().isEmpty()) {
            JOptionPane.showMessageDialog(this, "Please enter your PayPal email.", "Incomplete Information", JOptionPane.WARNING_MESSAGE);
            return;
        }

        JOptionPane.showMessageDialog(this, "Payment Successful! Booking Confirmed.");
        
        if (paymentSuccessCallback != null) {
            paymentSuccessCallback.run();
        }
        

        new MainMenuFrame(user);
        dispose();
    }

    private String getSelectedPaymentMethod() {
        if (creditCardRadio.isSelected()) return "Credit Card";
        else if (gcashRadio.isSelected()) return "GCash";
        else if (paypalRadio.isSelected()) return "PayPal";
        return null;
    }

    private void updatePaymentFields() {
        cardNumberField.setVisible(creditCardRadio.isSelected());
        expiryDateField.setVisible(creditCardRadio.isSelected());
        cvvField.setVisible(creditCardRadio.isSelected());
        gcashNumberField.setVisible(gcashRadio.isSelected());
        paypalEmailField.setVisible(paypalRadio.isSelected());

        revalidate();
        repaint();
    }

    public void setPaymentSuccessCallback(Runnable callback) {
        this.paymentSuccessCallback = callback;
    }
}
