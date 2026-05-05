package backend.Tickets;

import backend.Seating.SeatType;

// Frame only. i didn't do seat-booking side effects, pricing by seatType, and other details
public class SeatedTicket extends Ticket {
    private final int seatId;
    private final SeatType seatType;

    // TODO: book the seat, price by seatType + preferred-customer discount, etc.

    public SeatedTicket(int ticketId, int userId, int showingId, int seatId, SeatType seatType, double price) {
        super(ticketId, userId, showingId, price);
        this.seatId = seatId;
        this.seatType = seatType;
    }

    public int getSeatId() { return seatId; }
    public SeatType getSeatType() { return seatType; }
}
