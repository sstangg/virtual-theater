package front_end.TicketBooking;

import backend.Customer;
import backend.FoodService.Food;
import backend.Seating.Seat;
import front_end.BookingDraft;
import front_end.TheaterFrame;

import javax.swing.BorderFactory;
import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.SwingConstants;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.util.ArrayList;

import javax.swing.border.TitledBorder;

public class ConfirmationPage extends JPanel {

    private final TheaterFrame frame;

    // Summary labels (values get rewritten on each setBooking call).
    private JLabel movieValueLabel;
    private JLabel locationValueLabel;
    private JLabel showtimeValueLabel;
    private JLabel seatsValueLabel;
    private JLabel foodsValueLabel;
    private JLabel seatSubtotalLabel;
    private JLabel foodSubtotalLabel;
    private JLabel discountStatusLabel;
    private JLabel totalLabel;

    // Payment fields.
    private JTextField cardholderField;
    private JTextField cardNumberField;
    private JTextField expiryField;
    private JTextField cvvField;

    private JButton completeButton;
    private JLabel validationLabel;

    private BookingDraft currentDraft;

    public ConfirmationPage(TheaterFrame frame) {
        super();
        this.frame = frame;

        setBorder(BorderFactory.createEmptyBorder(16, 16, 16, 16));
        setLayout(new BorderLayout(0, 12));

        // Title
        JLabel title = new JLabel("Confirm Your Booking");
        title.setHorizontalAlignment(SwingConstants.CENTER);
        title.setFont(title.getFont().deriveFont(18f));
        add(title, BorderLayout.NORTH);

        // Center stack of summary + payment
        JPanel summaryPaymentHolder = new JPanel();
        summaryPaymentHolder.setLayout(new BoxLayout(summaryPaymentHolder, BoxLayout.Y_AXIS));
        summaryPaymentHolder.add(buildSummarySection());
        summaryPaymentHolder.add(Box.createVerticalStrut(12));
        summaryPaymentHolder.add(buildPaymentSection());
        summaryPaymentHolder.add(Box.createVerticalGlue());

        JPanel summaryPaymentHolderWrapper = new JPanel(new FlowLayout(FlowLayout.CENTER, 0, 0));
        summaryPaymentHolderWrapper.add(summaryPaymentHolder);
        add(summaryPaymentHolderWrapper, BorderLayout.CENTER);

        // Bottom region for validation message + buttons
        add(buildButtonRow(), BorderLayout.SOUTH);
    }


    // Build the summary section
    private JPanel buildSummarySection() {
        JPanel section = new JPanel(new GridBagLayout());
        section.setBorder(createSectionBorder("Booking summary"));
        section.setPreferredSize(new Dimension(640, 245));

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(4, 8, 4, 8);
        gbc.anchor = GridBagConstraints.WEST;
        gbc.fill = GridBagConstraints.HORIZONTAL;

        movieValueLabel = new JLabel("");
        locationValueLabel = new JLabel("");
        showtimeValueLabel = new JLabel("");
        seatsValueLabel = new JLabel("");
        foodsValueLabel = new JLabel("");
        seatSubtotalLabel = new JLabel("$0.00");
        foodSubtotalLabel = new JLabel("$0.00");
        discountStatusLabel = new JLabel("Basic pricing");
        totalLabel = new JLabel("$0.00");
        totalLabel.setFont(totalLabel.getFont().deriveFont(Font.BOLD, 14f));

        int row = 0;
        addSummaryRow(section, gbc, row++, "Movie:", movieValueLabel);
        addSummaryRow(section, gbc, row++, "Location:", locationValueLabel);
        addSummaryRow(section, gbc, row++, "Showtime:", showtimeValueLabel);
        addSummaryRow(section, gbc, row++, "Seats:", seatsValueLabel);
        addSummaryRow(section, gbc, row++, "Food:", foodsValueLabel);
        addSummaryRow(section, gbc, row++, "Seats subtotal:", seatSubtotalLabel);
        addSummaryRow(section, gbc, row++, "Food subtotal:", foodSubtotalLabel);
        addSummaryRow(section, gbc, row++, "Membership:", discountStatusLabel);
        addSummaryRow(section, gbc, row++, "Total:", totalLabel);

        return section;
    }

    // Build the payment section
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

        int row = 0;
        addFormRow(section, gbc, row++, "Cardholder name:", cardholderField);
        addFormRow(section, gbc, row++, "Card number:", cardNumberField);
        addFormRow(section, gbc, row++, "Expiry (MM/YY):", expiryField);
        addFormRow(section, gbc, row++, "CVV:", cvvField);

        return section;
    }

    // Build the button row
    private JPanel buildButtonRow() {
        JPanel buttonRow = new JPanel(new BorderLayout(0, 4));

        validationLabel = new JLabel(" ");
        validationLabel.setHorizontalAlignment(SwingConstants.CENTER);

        JPanel buttons = new JPanel(new FlowLayout(FlowLayout.RIGHT, 8, 0));
        JButton backButton = new JButton("Back");
        backButton.addActionListener(e -> frame.openFoodSelection());
        completeButton = new JButton("Complete Purchase");
        completeButton.addActionListener(e -> finalizePurchase());
        buttons.add(backButton);
        buttons.add(completeButton);

        buttonRow.add(validationLabel, BorderLayout.NORTH);
        JPanel buttonsHolder = new JPanel(new BorderLayout());
        buttonsHolder.add(buttons, BorderLayout.EAST);
        buttonRow.add(buttonsHolder, BorderLayout.CENTER);
        return buttonRow;
    }

    // Set the booking draft for the confirmation page
    public void setBooking(BookingDraft draft) {
        currentDraft = draft;
    
        // Set the values for the summary section
        movieValueLabel.setText(draft.movieName());
        locationValueLabel.setText(draft.getLocation());
        showtimeValueLabel.setText(draft.getShowtime());
        if (draft.isSeated()) {
            seatsValueLabel.setText(formatSeats(draft.getChosenSeats(), frame));
        } else {
            seatsValueLabel.setText("General admission");
        }
        foodsValueLabel.setText(formatFoods(draft.getChosenFoods()));

        // Set the values for the payment section
        Customer customer = frame.getCustomer();
        double seatSubtotal = frame.getManager().seatTotal(draft, customer);
        double foodSubtotal = frame.getManager().foodTotal(draft, customer);
        seatSubtotalLabel.setText(formatMoney(seatSubtotal));
        foodSubtotalLabel.setText(formatMoney(foodSubtotal));
        discountStatusLabel.setText(customer != null && customer.isPreferred() ? "Premium - 15% discount applied" : "Basic pricing");
        totalLabel.setText(formatMoney(seatSubtotal + foodSubtotal));

        // Reset the payment fields
        cardholderField.setText("");
        cardNumberField.setText("");
        expiryField.setText("");
        cvvField.setText("");
        validationLabel.setText(" ");
    }

    // Finalize the purchase
    private void finalizePurchase() {
        String error = validateForm();
        if (error != null) {
            validationLabel.setText(error);
            JOptionPane.showMessageDialog(this, error, "Payment incomplete", JOptionPane.WARNING_MESSAGE);
            return;
        }
        validationLabel.setText(" ");

        if (currentDraft == null) {
            JOptionPane.showMessageDialog(this, "No booking to confirm.", "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }
        Customer customer = frame.getCustomer();
        if (customer == null) {
            JOptionPane.showMessageDialog(this, "No customer signed in.", "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }

        frame.getManager().completePurchase(currentDraft, customer);
        frame.clearBookingDraft();
        frame.showCard(TheaterFrame.CARD_CUSTOMER_PROFILE);
    }

    // Checks for the validity of the payment form
    private String validateForm() {
        // Check if the cardholder name is empty
        String name = cardholderField.getText().trim();
        if (name.length() == 0) {
            return "Enter cardholder name";
        }
        // Check if the card number is 16 digits
        String digits = cardNumberField.getText().replaceAll("[^0-9]", "");
        if (digits.length() != 16) {
            return "Card number must be 16 digits";
        }
        // Check if the expiry is in the format MM/YY and is valid month and year
        String expiry = expiryField.getText();
        if (!isExpiryValid(expiry)) {
            return "Expiry must be MM/YY";
        }
        String cvvDigits = cvvField.getText().replaceAll("[^0-9]", "");
        if (cvvDigits.length() != 3) {
            return "CVV must be 3 digits";
        }
        return null;
    }

    // Checks if the expiry is in the format MM/YY and is valid month and year
    private boolean isExpiryValid(String expiry) {
        // Check if the expiry is in the format MM/YY
        if (expiry.length() != 5) {
            return false;
        }
        if (expiry.charAt(2) != '/') {
            return false;
        }

        // Parse the month and year from the expiry
        int month = Integer.parseInt(expiry.substring(0, 2));
        int year = Integer.parseInt(expiry.substring(3, 5));
        // Check if the month is valid
        if (month < 1 || month > 12) {
            return false;
        }
        // Check if the year is valid
        if (year < 0 || year > 99) {
            return false;
        }
        return true;
    }

    // --- Layout helpers -------------------------------------------------------------------

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

    // Format the seats for the summary section
    private String formatSeats(ArrayList<Seat> seats, TheaterFrame frame) {
        if (seats.size() == 0) {
            return "None";
        }
        String seatsText = "";
        for (int i = 0; i < seats.size(); i++) {
            if (i > 0) {
                seatsText += ", ";
            }
            String seatLabel = frame.getManager().getSeatLabel(seats.get(i).getSeatId());
            seatsText += frame.getManager().seatTypeLabel(seats.get(i).getType()) + " Seat " + seatLabel;
        }
        return seatsText;
    }

    // Format the foods for the summary section
    private String formatFoods(ArrayList<Food> foods) {
        if (foods.size() == 0) {
            return "None";
        }
        String foodsText = "";
        for (int i = 0; i < foods.size(); i++) {
            if (i > 0) {
                foodsText += ", ";
            }
            foodsText += foods.get(i).getName().replace('_', ' ');
        }
        return foodsText;
    }

    // Format the money to 2 decimal places
    private String formatMoney(double price) {
        return "$" + String.format("%.2f", price);
    }
}
