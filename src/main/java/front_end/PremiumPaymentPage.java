package front_end;

import backend.Customer;

import javax.swing.BorderFactory;
import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.SwingConstants;
import javax.swing.border.TitledBorder;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;

public class PremiumPaymentPage extends JPanel {
    public static final double MEMBERSHIP_PRICE = 25.0;

    private final TheaterFrame frame;

    private JTextField cardholderField;
    private JTextField cardNumberField;
    private JTextField expiryField;
    private JTextField cvvField;
    private JLabel validationLabel;

    public PremiumPaymentPage(TheaterFrame frame) {
        super();
        this.frame = frame;

        setBorder(BorderFactory.createEmptyBorder(16, 16, 16, 16));
        setLayout(new BorderLayout(0, 12));

        JLabel title = new JLabel("Become a Premium Member");
        title.setHorizontalAlignment(SwingConstants.CENTER);
        title.setFont(title.getFont().deriveFont(18f));
        add(title, BorderLayout.NORTH);

        JPanel center = new JPanel();
        center.setLayout(new BoxLayout(center, BoxLayout.Y_AXIS));
        center.add(buildSummarySection());
        center.add(Box.createVerticalStrut(12));
        center.add(buildPaymentSection());
        center.add(Box.createVerticalGlue());

        JPanel centerHolder = new JPanel(new FlowLayout(FlowLayout.CENTER, 0, 0));
        centerHolder.add(center);
        add(centerHolder, BorderLayout.CENTER);
        add(buildButtonRow(), BorderLayout.SOUTH);
    }

    private JPanel buildSummarySection() {
        JPanel section = new JPanel(new GridBagLayout());
        section.setBorder(createSectionBorder("Membership summary"));
        section.setPreferredSize(new Dimension(640, 120));

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(4, 8, 4, 8);
        gbc.anchor = GridBagConstraints.WEST;
        gbc.fill = GridBagConstraints.HORIZONTAL;

        addSummaryRow(section, gbc, 0, "Membership:", new JLabel("Premium Member"));
        addSummaryRow(section, gbc, 1, "Benefit:", new JLabel("15% off tickets and food"));
        addSummaryRow(section, gbc, 2, "Today:", new JLabel("$" + formatMoney(MEMBERSHIP_PRICE)));
        return section;
    }

    private JPanel buildPaymentSection() {
        JPanel section = new JPanel(new GridBagLayout());
        section.setBorder(createSectionBorder("Payment"));
        section.setPreferredSize(new Dimension(640, 200));

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(4, 8, 4, 8);
        gbc.anchor = GridBagConstraints.WEST;
        gbc.fill = GridBagConstraints.HORIZONTAL;

        cardholderField = new JTextField(20);
        cardNumberField = new JTextField(20);
        expiryField = new JTextField(7);
        cvvField = new JTextField(5);

        addFormRow(section, gbc, 0, "Cardholder name:", cardholderField);
        addFormRow(section, gbc, 1, "Card number:", cardNumberField);
        addFormRow(section, gbc, 2, "Expiry (MM/YY):", expiryField);
        addFormRow(section, gbc, 3, "CVV:", cvvField);
        return section;
    }

    private JPanel buildButtonRow() {
        JPanel row = new JPanel(new BorderLayout(0, 4));
        validationLabel = new JLabel(" ");
        validationLabel.setHorizontalAlignment(SwingConstants.CENTER);

        JPanel buttons = new JPanel(new FlowLayout(FlowLayout.RIGHT, 8, 0));
        JButton backButton = new JButton("Back");
        backButton.addActionListener(e -> frame.showCard(TheaterFrame.CARD_CUSTOMER_PROFILE));

        JButton completeButton = new JButton("Complete Purchase");
        completeButton.addActionListener(e -> completePurchase());

        buttons.add(backButton);
        buttons.add(completeButton);

        row.add(validationLabel, BorderLayout.NORTH);
        row.add(buttons, BorderLayout.CENTER);
        return row;
    }

    private void completePurchase() {
        String error = validateForm();
        if (error != null) {
            validationLabel.setText(error);
            JOptionPane.showMessageDialog(this, error, "Payment incomplete", JOptionPane.WARNING_MESSAGE);
            return;
        }

        Customer customer = frame.getCustomer();
        if (customer == null) {
            JOptionPane.showMessageDialog(this, "No customer signed in.", "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }

        customer.setPreferred(true);
        clearForm();
        JOptionPane.showMessageDialog(this, "Premium membership activated. Your 15% discount is ready.");
        frame.showCard(TheaterFrame.CARD_CUSTOMER_PROFILE);
    }

    private String validateForm() {
        String name = cardholderField.getText().trim();
        if (name.length() == 0) {
            return "Enter cardholder name";
        }
        String digits = cardNumberField.getText().replaceAll("[^0-9]", "");
        if (digits.length() != 16) {
            return "Card number must be 16 digits";
        }
        if (!isExpiryValid(expiryField.getText())) {
            return "Expiry must be MM/YY";
        }
        String cvvDigits = cvvField.getText().replaceAll("[^0-9]", "");
        if (cvvDigits.length() != 3) {
            return "CVV must be 3 digits";
        }
        return null;
    }

    private boolean isExpiryValid(String expiry) {
        if (expiry.length() != 5 || expiry.charAt(2) != '/') {
            return false;
        }
        try {
            int month = Integer.parseInt(expiry.substring(0, 2));
            int year = Integer.parseInt(expiry.substring(3, 5));
            return month >= 1 && month <= 12 && year >= 0 && year <= 99;
        } catch (NumberFormatException e) {
            return false;
        }
    }

    private void clearForm() {
        cardholderField.setText("");
        cardNumberField.setText("");
        expiryField.setText("");
        cvvField.setText("");
        validationLabel.setText(" ");
    }

    private TitledBorder createSectionBorder(String title) {
        TitledBorder border = BorderFactory.createTitledBorder(title);
        Font base = border.getTitleFont();
        if (base == null) {
            base = getFont();
        }
        border.setTitleFont(base.deriveFont(Font.BOLD, 16f));
        return border;
    }

    private static void addSummaryRow(JPanel panel, GridBagConstraints gbc, int row, String labelText, JLabel valueLabel) {
        gbc.gridx = 0;
        gbc.gridy = row;
        gbc.weightx = 0;
        gbc.fill = GridBagConstraints.NONE;
        panel.add(new JLabel(labelText), gbc);

        gbc.gridx = 1;
        gbc.weightx = 1;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        panel.add(valueLabel, gbc);
    }

    private static void addFormRow(JPanel panel, GridBagConstraints gbc, int row, String labelText, JTextField field) {
        gbc.gridx = 0;
        gbc.gridy = row;
        gbc.weightx = 0;
        gbc.fill = GridBagConstraints.NONE;
        panel.add(new JLabel(labelText), gbc);

        gbc.gridx = 1;
        gbc.weightx = 1;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        panel.add(field, gbc);
    }

    private String formatMoney(double price) {
        return String.format("%.2f", price);
    }
}
