package front_end;

import javax.swing.*;

import java.awt.BorderLayout;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;

public class CustomerProfile extends JPanel implements IRefreshable {
    private TheaterFrame frame;

    private String[] customerInfo;

    private JTextField customerNameTextField;
    private JTextField customerEmailTextField;
    private JTextField customerPhoneNumberTextField;
    private JTextField customerAddressTextField;
    private JTextField customerCityTextField;
    private JTextField customerStateTextField;
    private JTextField customerZipCodeTextField;

    private JLabel customerIsPreferredCustomerLabel;
    private JLabel customerOwnedTicketsLabel;
    private JLabel customerOwnedFoodItemsLabel;

    public CustomerProfile(TheaterFrame frame, String[] customerInfo) {
        super();
        this.frame = frame;

        this.customerInfo = customerInfo;

        // Set the layout of the panel
        BorderLayout layout = new BorderLayout();
        this.setLayout(layout);
        this.setBorder(BorderFactory.createEmptyBorder(16, 16, 16, 16));

        // Create the customer name label and add to the panel
        JLabel pageTitleLabel = new JLabel("Customer Profile");
        pageTitleLabel.setHorizontalAlignment(SwingConstants.CENTER);
        pageTitleLabel.setFont(pageTitleLabel.getFont().deriveFont(18f));
        this.add(pageTitleLabel, BorderLayout.NORTH);

        // GridBagLayout: rows stay compact; only text fields grow horizontally (not vertically).
        JPanel customerInformationPanel = new JPanel(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(4, 4, 4, 4);
        gbc.anchor = GridBagConstraints.WEST;
        gbc.fill = GridBagConstraints.NONE;
        gbc.weightx = 0;
        gbc.weighty = 0;

        int row = 0;
        customerNameTextField = new JTextField(20);
        addFormRow(customerInformationPanel, gbc, row++, new JLabel("Name:"), customerNameTextField);
        customerEmailTextField = new JTextField(20);
        addFormRow(customerInformationPanel, gbc, row++, new JLabel("Email:"), customerEmailTextField);
        customerPhoneNumberTextField = new JTextField(20);
        addFormRow(customerInformationPanel, gbc, row++, new JLabel("Phone Number:"), customerPhoneNumberTextField);
        customerAddressTextField = new JTextField(20);
        addFormRow(customerInformationPanel, gbc, row++, new JLabel("Address:"), customerAddressTextField);
        customerCityTextField = new JTextField(20);
        addFormRow(customerInformationPanel, gbc, row++, new JLabel("City:"), customerCityTextField);
        customerStateTextField = new JTextField(20);
        addFormRow(customerInformationPanel, gbc, row++, new JLabel("State:"), customerStateTextField);
        customerZipCodeTextField = new JTextField(20);
        addFormRow(customerInformationPanel, gbc, row++, new JLabel("Zip Code:"), customerZipCodeTextField);

        customerIsPreferredCustomerLabel = new JLabel("");
        addFormRow(customerInformationPanel, gbc, row++, new JLabel("Customer Membership:"), customerIsPreferredCustomerLabel);

        customerOwnedTicketsLabel = new JLabel("");
        addFormRow(customerInformationPanel, gbc, row++, new JLabel("Owned Tickets:"), customerOwnedTicketsLabel);

        customerOwnedFoodItemsLabel = new JLabel("");
        addFormRow(customerInformationPanel, gbc, row++, new JLabel("Owned Food Items:"), customerOwnedFoodItemsLabel);

        // Add button to save the customer information
        JButton saveButton = new JButton("Save & Continue");
        saveButton.addActionListener(e -> {
            String name = customerNameTextField.getText();
            String email = customerEmailTextField.getText();
            String phoneNumber = customerPhoneNumberTextField.getText();
            String address = customerAddressTextField.getText();
            String city = customerCityTextField.getText();
            String state = customerStateTextField.getText();
            String zipCode = customerZipCodeTextField.getText();
            String isPreferredCustomer = customerIsPreferredCustomerLabel.getText();
            String ownedTickets = customerOwnedTicketsLabel.getText();
            String ownedFoodItems = customerOwnedFoodItemsLabel.getText();
            this.customerInfo = new String[] { name, email, phoneNumber, address, city, state, zipCode, isPreferredCustomer, ownedTickets, ownedFoodItems };
            frame.setCustomerInfo(this.customerInfo);
            frame.showCard(TheaterFrame.CARD_THEATER_LOBBY);
        });
        gbc.gridx = 1;
        gbc.gridy = row++;
        gbc.gridwidth = 1;
        gbc.weightx = 0;
        gbc.weighty = 0;
        gbc.fill = GridBagConstraints.NONE;
        gbc.anchor = GridBagConstraints.EAST;
        customerInformationPanel.add(saveButton, gbc);

        // Eat extra vertical space below the form so rows don't stretch.
        gbc.gridx = 0;
        gbc.gridy = row;
        gbc.gridwidth = 2;
        gbc.weighty = 1;
        gbc.fill = GridBagConstraints.BOTH;
        customerInformationPanel.add(Box.createVerticalGlue(), gbc);

        

        this.add(customerInformationPanel, BorderLayout.CENTER);

        applyCustomerInfo(customerInfo);
    }

    private void applyCustomerInfo(String[] customerInfo) {
        if (customerInfo == null || customerInfo.length < 7) {
            return;
        }
        customerNameTextField.setText(customerInfo[0] != null ? customerInfo[0] : "");
        customerEmailTextField.setText(customerInfo[1] != null ? customerInfo[1] : "");
        customerPhoneNumberTextField.setText(customerInfo[2] != null ? customerInfo[2] : "");
        customerAddressTextField.setText(customerInfo[3] != null ? customerInfo[3] : "");
        customerCityTextField.setText(customerInfo[4] != null ? customerInfo[4] : "");
        customerStateTextField.setText(customerInfo[5] != null ? customerInfo[5] : "");
        customerZipCodeTextField.setText(customerInfo[6] != null ? customerInfo[6] : "");
        customerIsPreferredCustomerLabel.setText(customerInfo[7] != null && customerInfo[7] != "" ? customerInfo[7] : "Basic");
        customerOwnedTicketsLabel.setText(customerInfo[8] != null && customerInfo[8] != "" ? customerInfo[8] : "None");
        customerOwnedFoodItemsLabel.setText(customerInfo[9] != null && customerInfo[9] != "" ? customerInfo[9] : "None");
    }

    @Override
    public void refreshCache() {
        this.customerInfo = frame.getCustomerInfo();
        applyCustomerInfo(this.customerInfo);
    }

    private static void addFormRow(JPanel panel, GridBagConstraints gbc, int row, JLabel label, JTextField field) {
        gbc.gridx = 0;
        gbc.gridy = row;
        gbc.gridwidth = 1;
        gbc.weightx = 0;
        gbc.weighty = 0;
        gbc.fill = GridBagConstraints.NONE;
        gbc.anchor = GridBagConstraints.WEST;
        panel.add(label, gbc);

        gbc.gridx = 1;
        gbc.weightx = 1;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        panel.add(field, gbc);
    }

    private static void addFormRow(JPanel panel, GridBagConstraints gbc, int row, JLabel label, JLabel valueLabel) {
        gbc.gridx = 0;
        gbc.gridy = row;
        gbc.gridwidth = 1;
        gbc.weightx = 0;
        gbc.weighty = 0;
        gbc.fill = GridBagConstraints.NONE;
        gbc.anchor = GridBagConstraints.WEST;
        panel.add(label, gbc);

        gbc.gridx = 1;
        gbc.weightx = 1;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        panel.add(valueLabel, gbc);
    }
}
