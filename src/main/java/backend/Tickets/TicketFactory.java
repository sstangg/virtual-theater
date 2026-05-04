package backend.Tickets;

import backend.Seating.SeatType;

// Frame only. i didn't do pricing logic and any preferred-customer discount.
public class TicketFactory {
    private static int nextId = 1;

    // TODO : compute price from seatType + preferred-customer status, validate inputs.

    public static SeatedTicket createSeatedTicket(int userId, int showingId, int seatId, SeatType seatType) {
        return new SeatedTicket(nextId++, userId, showingId, seatId, seatType);
    }

    public static UnseatedTicket createUnseatedTicket(int userId, int showingId) {
        return new UnseatedTicket(nextId++, userId, showingId);
    }
}
