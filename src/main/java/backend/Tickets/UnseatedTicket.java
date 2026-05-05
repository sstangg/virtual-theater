package backend.Tickets;

// Frame only. I didn't add drive-in / outdoor specific fields (parking spot? radio channel?).
public class UnseatedTicket extends Ticket {
    // TODO : drive-in / outdoor specific attributes, capacity-based validationand so on.

    public UnseatedTicket(int ticketId, int userId, int showingId, double price) {
        super(ticketId, userId, showingId, price);
    }
}
