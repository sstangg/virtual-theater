package front_end;

import javax.swing.*;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.FlowLayout;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;

import backend.Customer;
import backend.FoodService.Food;
import backend.TheaterSchedule.Movie;
import backend.TheaterSchedule.Showing;
import backend.Tickets.SeatedTicket;
import backend.Tickets.Ticket;
import backend.TheaterSchedule.DoubleFeature;

/*
 * CustomerProfile is the panel that allows the user to view and edit their customer profile.
 * It also allows the user to see their tickets and foods that they have purchased.
 */
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
    private JButton premiumButton;

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

        // The information of the customer
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

        // eliminate extra vertical space below the form so rows don't stretch.
        gbc.gridx = 0;
        gbc.gridy = row;
        gbc.gridwidth = 2;
        gbc.weighty = 1;
        gbc.fill = GridBagConstraints.BOTH;
        customerInformationPanel.add(Box.createVerticalGlue(), gbc);

        this.add(customerInformationPanel, BorderLayout.CENTER);

        JPanel bottomRightPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT, 0, 0));
        premiumButton = new JButton("Become a Premium Member");
        premiumButton.addActionListener(e -> frame.openPremiumPaymentPage());
        bottomRightPanel.add(premiumButton);
        this.add(bottomRightPanel, BorderLayout.SOUTH);

        applyCustomerInfo(customerInfo);
    }

    // Apply the customer information to the panel
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
    }

    // Refresh the cache information of the panel
    @Override
    public void refreshCache() {
        this.customerInfo = frame.getCustomerInfo();
        applyCustomerInfo(this.customerInfo);

        // Membership + tickets + foods come from the real Customer object on the frame.
        Customer customer = frame.getCustomer();
        if (customer != null) {
            customerIsPreferredCustomerLabel.setText(customer.isPreferred() ? "Premium" : "Basic");
            customerIsPreferredCustomerLabel.setForeground(customer.isPreferred() ? new Color(145, 105, 0) : Color.BLACK);
            premiumButton.setEnabled(!customer.isPreferred());
            premiumButton.setText(customer.isPreferred() ? "Premium Member Active" : "Become a Premium Member");
            customerOwnedTicketsLabel.setText(buildTicketsHtml(customer));
            customerOwnedFoodItemsLabel.setText(buildFoodsText(customer));

            // Keep the name in the demographic field synced with the Customer.
            if (customer.getName() != null
                    && (customerNameTextField.getText() == null || customerNameTextField.getText().length() == 0)) {
                customerNameTextField.setText(customer.getName());
            }
        } else {
            customerIsPreferredCustomerLabel.setText("Basic");
            customerIsPreferredCustomerLabel.setForeground(Color.BLACK);
            premiumButton.setEnabled(false);
            premiumButton.setText("Become a Premium Member");
            customerOwnedTicketsLabel.setText("None");
            customerOwnedFoodItemsLabel.setText("None");
        }
    }

    // Build the text for the tickets the customer has purchased using HTML formatting
    private String buildTicketsHtml(Customer customer) {
        if (customer.getTickets() == null || customer.getTickets().size() == 0) {
            return "None";
        }
        String ticketsText = "";
        ticketsText += "<html>";
        for (int i = 0; i < customer.getTickets().size(); i++) {
            Ticket t = customer.getTickets().get(i);
            ticketsText += formatTicketLineForCustomer(t);
            if (i < customer.getTickets().size() - 1) {
                ticketsText += "<br>";
            }
        }
        ticketsText += "</html>";
        return ticketsText;
    }

    private String formatTicketLineForCustomer(Ticket t) {
        String ticketLine = "";
        Showing sh = findShowing(t.getShowingId());
        if (sh != null) {
            String movieName = "";
            if (sh instanceof DoubleFeature) {
                int[] ids = ((DoubleFeature) sh).getMovieIds();
                Movie m1 = frame.getManager().getMovieById(ids[0]);
                Movie m2 = frame.getManager().getMovieById(ids[1]);
                String n1 = m1 != null ? m1.getName() : ("#" + ids[0]);
                String n2 = m2 != null ? m2.getName() : ("#" + ids[1]);
                movieName = n1 + " + " + n2;
            } else {
                Movie m = frame.getManager().getMovieById(sh.getMovieId());
                movieName = m != null ? m.getName() : ("#" + sh.getMovieId());
            }
            ticketLine += movieName + " - " + frame.getManager().formatShowingTime(sh);
        } else {
            ticketLine += "Showing #" + t.getShowingId();
        }

        if (t instanceof SeatedTicket) {
            SeatedTicket seatedTicket = (SeatedTicket) t;
            int seatId = seatedTicket.getSeatId();
            String label = frame.getManager().getSeatLabel(seatId);
            ticketLine += " - " + frame.getManager().seatTypeLabel(seatedTicket.getSeatType())
                    + " Seat " + (label != null ? label : ("#" + seatId));
        } else {
            ticketLine += " - General admission";
        }
        ticketLine += " - $" + String.format("%.2f", t.getPrice());
        return ticketLine;
    }

    // Format a single ticket line based on the ticket's showing and seat
    private String formatTicketLine(Ticket t) {
        String ticketLine = "";
        Showing sh = findShowing(t.getShowingId());
        String movieName = "";
        if (sh instanceof DoubleFeature) {
            int[] ids = ((DoubleFeature) sh).getMovieIds();
            Movie m1 = frame.getManager().getMovieById(ids[0]);
            Movie m2 = frame.getManager().getMovieById(ids[1]);
            String n1 = m1.getName();
            String n2 = m2 != null ? m2.getName() : ("#" + ids[1]);
            movieName = n1 + " + " + n2;
        } else {
            Movie m = frame.getManager().getMovieById(sh.getMovieId());
            movieName = m != null ? m.getName() : ("#" + sh.getMovieId());
        }
        ticketLine += movieName + " — " + frame.getManager().formatShowingTime(sh);
        if (t instanceof SeatedTicket) {
            int seatId = ((SeatedTicket) t).getSeatId();
            String label = frame.getManager().getSeatLabel(seatId);
            ticketLine += " — Seat " + (label != null ? label : ("#" + seatId));
        } else {
            ticketLine += " — General admission";
        }
        return ticketLine;
    }

    // Find the showing in the schedule that has the given showing id
    private Showing findShowing(int showingId) {
        for (int i = 0; i < frame.getManager().getSchedule().getShowings().size(); i++) {
            Showing sh = frame.getManager().getSchedule().getShowings().get(i);
            if (sh.getShowingId() == showingId) {
                return sh;
            }
        }
        return null;
    }

    // Build the text for the foods the customer has purchased
    private String buildFoodsText(Customer customer) {
        if (customer.getFoods() == null || customer.getFoods().size() == 0) {
            return "None";
        }
        String foodsText = "";
        for (int i = 0; i < customer.getFoods().size(); i++) {
            if (i > 0) {
                foodsText += ", ";
            }
            Food f = customer.getFoods().get(i);
            foodsText += f.getName().replace('_', ' '); // replace underscores in backend
        }
        return foodsText;
    }

    // Add a form row to the panel for a text field
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

    // Add a form row to the panel for a label
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
