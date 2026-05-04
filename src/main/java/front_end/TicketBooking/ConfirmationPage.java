package front_end.TicketBooking;

import front_end.TheaterFrame;

import javax.swing.JPanel;
import javax.swing.JLabel;
import javax.swing.SwingConstants;

import java.awt.BorderLayout;

public class ConfirmationPage extends JPanel {
    private TheaterFrame frame;

    private JLabel bookingDetailsLabel;

    public ConfirmationPage(TheaterFrame frame) {
        super();
        this.frame = frame;

        // Set the layout of the panel
        BorderLayout layout = new BorderLayout();
        this.setLayout(layout);

        // Create the title label
        JLabel title = new JLabel("Confirm Your Booking");
        title.setHorizontalAlignment(SwingConstants.CENTER);
        title.setFont(title.getFont().deriveFont(18f));
        this.add(title, BorderLayout.NORTH);
        
        // Create the body indicating the booked movies
        bookingDetailsLabel = new JLabel();
        bookingDetailsLabel.setHorizontalAlignment(SwingConstants.CENTER);
        bookingDetailsLabel.setFont(bookingDetailsLabel.getFont().deriveFont(16f));
        this.add(bookingDetailsLabel, BorderLayout.CENTER);
    }

    public void setBookingDetails(String[] movie, String[] seats) {
        String bookingDetailsText = "You have booked the following movie:\n";
        bookingDetailsText += "Movie Name: " + movie[0] + "\n";
        bookingDetailsText += "Location: " + movie[1] + "\n";
        bookingDetailsText += "Showtime: " + movie[6] + "\n";
        bookingDetailsText += "Seats: " + String.join(", ", seats) + "\n";
        bookingDetailsLabel.setText(bookingDetailsText);
    }
}
