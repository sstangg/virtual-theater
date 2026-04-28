package front_end.TicketBooking;

import javax.swing.JPanel;
import javax.swing.JLabel;
import javax.swing.SwingConstants;
import javax.swing.JToggleButton;
import javax.swing.BorderFactory;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.JButton;

import java.awt.Dimension;
import java.awt.BorderLayout;
import java.awt.FlowLayout;
import java.awt.Color;
import javax.swing.BoxLayout;

import front_end.TheaterFrame;

import java.util.ArrayList;

public class SeatChart extends JPanel {
    private TheaterFrame frame;

    private String[] seatRows = { "A", "B", "C", "D", "E", "F", "G", "H", "I", "J"};
    private String[] seatColumns = { "1", "2", "3", "4", "5", "6", "7", "8", "9", "10", "11", "12"};

    private ArrayList<String> selectedSeats = new ArrayList<>();

    // Components we update when a new movie is loaded.
    private JLabel seatChartTitle;
    private JPanel seatChartPanel;
    private JTextArea selectedSeatsArea;

    public SeatChart(TheaterFrame frame, String[] movie) {
        super();
        this.frame = frame;

        // Border padding
        this.setBorder(BorderFactory.createEmptyBorder(16, 16, 16, 16));

        // Set the layout of the panel
        BorderLayout layout = new BorderLayout(0, 12);
        this.setLayout(layout);

        // Create the seat chart title label
        seatChartTitle = new JLabel();
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

        // Use flow layout to not auto stretch the seat chart when the window grows
        JPanel seatChartHolder = new JPanel(new FlowLayout(FlowLayout.CENTER, 0, 0));
        // Create the border for the seat chart and indicate where the screen is located
        seatChartHolder.setBorder(BorderFactory.createTitledBorder(
                BorderFactory.createLineBorder(Color.DARK_GRAY),
                "* * * * * * * * * * * * * * * Screen * * * * * * * * * * * * * * *",
                javax.swing.border.TitledBorder.CENTER,
                javax.swing.border.TitledBorder.TOP
        ));
        seatChartHolder.add(seatChartPanel);

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
            // TODO: Implement the logic to continue to the ticket confirmation page
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

        // Load the initial movie data (also clears any old selections).
        setMovie(movie);
    }

    // Build seat chart
    private void buildSeatChart() {
        // Loop through the rows of seats
        for (int i = 0; i < seatRows.length; i++) {
            JPanel rowPanel = new JPanel();
            rowPanel.setLayout(new FlowLayout(FlowLayout.CENTER, 4, 4));

            // Create the seats in the row
            for (int j = 0; j < seatColumns.length; j++) {
                JToggleButton seatButton = new JToggleButton("" + seatRows[i] + seatColumns[j]);
                seatButton.setHorizontalAlignment(SwingConstants.CENTER);

                // Adjust properties of the toggle button
                seatButton.setPreferredSize(new Dimension(40, 40));
                seatButton.setFont(seatButton.getFont().deriveFont(10f));
                seatButton.setFocusPainted(false);
                seatButton.setOpaque(true);
                seatButton.setContentAreaFilled(true);

                Color defaultBg = seatButton.getBackground();
                Color defaultFg = seatButton.getForeground();
                Color selectedBg = seatButton.getBackground().darker();
                Color selectedFg = seatButton.getForeground().darker();

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

    // Update the panel when a new movie is loaded.
    public void setMovie(String[] movie) {
        // Set the title of the seat chart to the movie name, location, and time
        seatChartTitle.setText(movie[0] + " - " + movie[1] + " - " + movie[6]);

        // Reset selected seats
        selectedSeats.clear();
        selectedSeatsArea.setText("None");

        // Reset the selected seats in the seat chart panel to be unselected
        for (java.awt.Component rowComponent : seatChartPanel.getComponents()) {
            JPanel rowPanel = (JPanel) rowComponent;
            for (java.awt.Component seatComponent : rowPanel.getComponents()) {
                if (seatComponent instanceof JToggleButton) {
                    ((JToggleButton) seatComponent).setSelected(false);
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
                    selectedSeatsText += seatButton.getText();
                    selectedSeats.add(seatButton.getText());
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
}
