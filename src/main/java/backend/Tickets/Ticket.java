package backend.Tickets;

// Frame only. i didn't add pricing, theaterType, payable interface, equals/hashCode.
public abstract class Ticket {
    protected final int ticketId;
    protected final int userId;
    protected final int showingId;
    protected final double price;

    // TODO : add price, TheaterType, validation, payable interface, etc.

    protected Ticket(int ticketId, int userId, int showingId, double price) {
        this.ticketId = ticketId;
        this.userId = userId;
        this.showingId = showingId;
        this.price = price;
    }

    public int getTicketId() { return ticketId; }
    public int getUserId() { return userId; }
    public int getShowingId() { return showingId; }
    public double getPrice() { return price; }
}
