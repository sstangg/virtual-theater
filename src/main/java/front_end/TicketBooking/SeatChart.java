package front_end.TicketBooking;

import javax.swing.JPanel;
import javax.swing.JLabel;
import javax.swing.SwingConstants;
import javax.swing.JToggleButton;
import javax.swing.BorderFactory;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.JButton;
import javax.swing.JOptionPane;

import java.awt.Dimension;
import java.awt.BorderLayout;
import java.awt.FlowLayout;
import java.awt.Color;
import javax.swing.BoxLayout;

import backend.Seating.Seat;
import backend.Seating.SeatType;
import front_end.TheaterFrame;
import front_end.DataManagers.BookingDraft;

import java.util.ArrayList;

public class SeatChart extends JPanel {
    private static final String[] SEAT_ROW_LABELS = { "A", "B", "C", "D", "E", "F", "G", "H", "I", "J" };
    private static final int SEAT_COL_COUNT = 12;

    private TheaterFrame frame;

    private ArrayList<String> selectedSeats = new ArrayList<>();

    // Components we update when a new booking is loaded.
    private JLabel seatChartTitle;
    private JPanel seatChartPanel;
    private JTextArea selectedSeatsArea;

    // Booking the user is currently working on. Set by setBooking when entering the card.
    private BookingDraft currentDraft;

    public SeatChart(TheaterFrame frame) {
        super();
        this.frame = frame;

        // Border padding
        this.setBorder(BorderFactory.createEmptyBorder(16, 16, 16, 16));

        // Set the layout of the panel
        BorderLayout layout = new BorderLayout(0, 12);
        this.setLayout(layout);

        // Create the seat chart title label
        seatChartTitle = new JLabel(" ");
        seatChartTitle.setHorizontalAlignment(SwingConstants.CENTER);
        seatChartTitle.setFont(seatChartTitle.getFont().deriveFont(18f));

        // Create the seat chart panel
        seatChartPanel = new JPanel();
        seatChartPanel.setLayout(new BoxLayout(seatChartPanel, BoxLayout.Y_AXIS));

        // Text box at the bottom that shows selected seats.
        selectedSeatsArea = new JTextArea(3, 30);
        selectedSeatsArea.setEditable(false);
        selectedSeatsArea.setLineWrap(true);
        selectedSeatsArea.setWrapStyleWord(true);
        selectedSeatsArea.setText("None");
        JScrollPane selectedSeatsScrollPane = new JScrollPane(selectedSeatsArea);
        selectedSeatsScrollPane.setBorder(BorderFactory.createTitledBorder("Selected Seats"));

        // Create the rows of seats represented by toggle buttons
        buildSeatChart();

        JPanel seatChartWithLegend = new JPanel();
        seatChartWithLegend.setLayout(new BoxLayout(seatChartWithLegend, BoxLayout.Y_AXIS));
        seatChartWithLegend.add(seatChartPanel);
        seatChartWithLegend.add(buildSeatLegend());

        // Use flow layout to not auto stretch the seat chart when the window grows
        JPanel seatChartHolder = new JPanel(new FlowLayout(FlowLayout.CENTER, 0, 0));
        // Create the border for the seat chart and indicate where the screen is located
        seatChartHolder.setBorder(BorderFactory.createTitledBorder(
                BorderFactory.createLineBorder(Color.DARK_GRAY),
                "* * * * * * * * * * * * * * * Screen * * * * * * * * * * * * * * *",
                javax.swing.border.TitledBorder.CENTER,
                javax.swing.border.TitledBorder.TOP
        ));
        seatChartHolder.add(seatChartWithLegend);

        // Bottom region for selected seats and navigation buttons
        JPanel bottomPanel = new JPanel(new BorderLayout(0, 8));

        // Holder for the back and continue buttons
        JPanel buttonHolderPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT, 8, 0));
        JButton backButton = new JButton("Back");
        backButton.addActionListener(e -> {
            frame.showCard(TheaterFrame.CARD_SEARCH_MOVIES);
        });
        JButton continueButton = new JButton("Continue");
        continueButton.addActionListener(e -> {
            if (currentDraft == null) {
                JOptionPane.showMessageDialog(this, "No booking selected.", "Booking incomplete", JOptionPane.WARNING_MESSAGE);
                return;
            }
            if (selectedSeats.size() == 0) {
                JOptionPane.showMessageDialog(this, "Please choose at least one seat.", "Booking incomplete", JOptionPane.WARNING_MESSAGE);
                return;
            }
            // Translate the picked labels letter number labels into real Seat objects on the draft.
            ArrayList<Seat> chosen = new ArrayList<Seat>();
            for (int i = 0; i < selectedSeats.size(); i++) {
                Seat s = frame.getManager().getSeatByLabel(selectedSeats.get(i));
                if (s != null) {
                    chosen.add(s);
                }
            }
            currentDraft.setChosenSeats(chosen);
            frame.openFoodSelection();
        });
        buttonHolderPanel.add(backButton);
        buttonHolderPanel.add(continueButton);

        // Add the components to the bottom panel
        bottomPanel.add(selectedSeatsScrollPane, BorderLayout.CENTER);
        bottomPanel.add(buttonHolderPanel, BorderLayout.SOUTH);

        // Add the components to the main panel
        this.add(seatChartTitle, BorderLayout.NORTH);
        this.add(seatChartHolder, BorderLayout.CENTER);
        this.add(bottomPanel, BorderLayout.SOUTH);
    }

    // Build seat chart
    private void buildSeatChart() {
        // Loop through the rows of seats
        for (int i = 0; i < SEAT_ROW_LABELS.length; i++) {
            JPanel rowPanel = new JPanel();
            rowPanel.setLayout(new FlowLayout(FlowLayout.CENTER, 4, 4));

            // Create the seats in the row
            for (int j = 0; j < SEAT_COL_COUNT; j++) {
                JToggleButton seatButton = new JToggleButton("" + SEAT_ROW_LABELS[i] + (j + 1));
                String label = SEAT_ROW_LABELS[i] + (j + 1);
                seatButton.setName(label);
                seatButton.setHorizontalAlignment(SwingConstants.CENTER);

                // Adjust properties of the toggle button
                seatButton.setPreferredSize(new Dimension(48, 40));
                seatButton.setFont(seatButton.getFont().deriveFont(10f));
                seatButton.setFocusPainted(false);
                seatButton.setOpaque(true);
                seatButton.setContentAreaFilled(true);

                Seat currentSeat = frame.getManager().getSeatByLabel(label);
                SeatType seatType = currentSeat != null ? currentSeat.getType() : SeatType.BASIC;
                Color defaultBg = seatColor(seatType);
                Color defaultFg = Color.BLACK;
                Color selectedBg = defaultBg.darker();
                Color selectedFg = Color.WHITE;
                seatButton.setText(label + " " + seatTypeLetter(seatType));
                seatButton.setToolTipText(label + " " + frame.getManager().seatTypeLabel(seatType)
                        + " ($" + formatMoney(currentSeat != null ? currentSeat.getPrice() : 0.0) + ")");
                seatButton.setBackground(defaultBg);
                seatButton.setForeground(defaultFg);

                // Update the color of the toggle button if it is selected or unselected
                seatButton.addItemListener(itemEvent -> {
                    if (seatButton.isSelected()) {
                        seatButton.setBackground(selectedBg);
                        seatButton.setForeground(selectedFg);
                    } else {
                        seatButton.setBackground(defaultBg);
                        seatButton.setForeground(defaultFg);
                    }
                });

                // Update selected seats text when toggled
                seatButton.addActionListener(actionEvent -> {
                    selectedSeatsArea.setText(buildSelectedSeatsText(seatChartPanel));
                    selectedSeatsArea.setCaretPosition(0);
                });
                rowPanel.add(seatButton);
            }
            seatChartPanel.add(rowPanel);
        }
    }

    // Set the booking for the seat chart
    public void setBooking(BookingDraft draft) {
        currentDraft = draft;

        String movieName = draft.movieName();
        String location = draft.getLocation();
        String showtime = draft.getShowtime();
        boolean premium = frame.getCustomer() != null && frame.getCustomer().isPreferred();
        seatChartTitle.setText(movieName + " - " + location + " - " + showtime
                + (premium ? " - Premium 15% off" : ""));

        // Reset selected seats
        selectedSeats.clear();
        selectedSeatsArea.setText("None");

        int showingId = draft.getShowing().getShowingId();

        // Update the seat chart to show the seats that are sold for the showing
        for (java.awt.Component rowComponent : seatChartPanel.getComponents()) {
            JPanel rowPanel = (JPanel) rowComponent;
            for (java.awt.Component seatComponent : rowPanel.getComponents()) {
                if (!(seatComponent instanceof JToggleButton)) {
                    continue;
                }
                JToggleButton btn = (JToggleButton) seatComponent;
                btn.setSelected(false);

                String label = btn.getName();
                Seat currentSeat = frame.getManager().getSeatByLabel(label);
                boolean booked = currentSeat != null && frame.getManager().isSeatBookedForShowing(showingId, currentSeat.getSeatId());
                btn.setEnabled(!booked); // If the seat is booked, disable the button
                btn.setBackground(booked ? Color.LIGHT_GRAY : seatColor(currentSeat != null ? currentSeat.getType() : SeatType.BASIC));
                if (currentSeat != null) {
                    btn.setToolTipText(label + " " + frame.getManager().seatTypeLabel(currentSeat.getType())
                            + " ($" + formatMoney(frame.getManager().discountedPrice(currentSeat.getPrice(), frame.getCustomer()))
                            + (premium ? " Premium" : "") + ")");
                }
            }
        }
    }

    // Loop through the seats in the seat chart panel and build the text for the selected seats
    private String buildSelectedSeatsText(JPanel seatChartPanel) {
        // Build the text for the selected seats
        String selectedSeatsText = "";
        selectedSeats.clear();
        boolean anySelected = false;

        // Go through the rows of seats
        for (java.awt.Component rowComponent : seatChartPanel.getComponents()) {
            JPanel rowPanel = (JPanel) rowComponent;

            // Go through the seats in the row
            for (java.awt.Component seatComponent : rowPanel.getComponents()) {
                JToggleButton seatButton = (JToggleButton) seatComponent;
                if (seatButton.isSelected()) { // If the seat is selected, add it to the text
                    if (anySelected) {
                        selectedSeatsText += ", ";
                    }
                    selectedSeatsText += seatButton.getName();
                    selectedSeats.add(seatButton.getName());
                    anySelected = true;
                }
            }
        }

        // If no seats are selected, add "None" to the text
        if (!anySelected) {
            selectedSeatsText += "None";
        }

        return selectedSeatsText;
    }

    private JPanel buildSeatLegend() {
        JPanel legend = new JPanel(new FlowLayout(FlowLayout.CENTER, 12, 4));
        legend.add(new JLabel("L = Luxury"));
        legend.add(new JLabel("E = Enhanced"));
        legend.add(new JLabel("B = Basic"));
        return legend;
    }

    private Color seatColor(SeatType type) {
        if (type == SeatType.LUXURY) {
            return new Color(236, 202, 104);
        } else if (type == SeatType.ENHANCED) {
            return new Color(151, 202, 219);
        }
        return new Color(224, 224, 224);
    }

    private String seatTypeLetter(SeatType type) {
        if (type == SeatType.LUXURY) {
            return "L";
        } else if (type == SeatType.ENHANCED) {
            return "E";
        }
        return "B";
    }

    private String formatMoney(double price) {
        return String.format("%.2f", price);
    }
}
