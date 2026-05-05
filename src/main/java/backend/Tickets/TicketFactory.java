package backend.Tickets;

import backend.Seating.SeatType;

// Frame only. i didn't do pricing logic and any preferred-customer discount.
public class TicketFactory {
    private static int nextId = 1;
    public static final double UNSEATED_PRICE = 12.0;
    public static final double PREMIUM_DISCOUNT_RATE = 0.15;

    // TODO : compute price from seatType + preferred-customer status, validate inputs.

    public static SeatedTicket createSeatedTicket(int userId, int showingId, int seatId, SeatType seatType) {
        return createSeatedTicket(userId, showingId, seatId, seatType, false);
    }

    public static SeatedTicket createSeatedTicket(int userId, int showingId, int seatId, SeatType seatType, boolean premium) {
        return new SeatedTicket(nextId++, userId, showingId, seatId, seatType, applyDiscount(seatPrice(seatType), premium));
    }

    public static UnseatedTicket createUnseatedTicket(int userId, int showingId) {
        return createUnseatedTicket(userId, showingId, false);
    }

    public static UnseatedTicket createUnseatedTicket(int userId, int showingId, boolean premium) {
        return new UnseatedTicket(nextId++, userId, showingId, applyDiscount(UNSEATED_PRICE, premium));
    }

    public static double applyDiscount(double price, boolean premium) {
        if (!premium) {
            return price;
        }
        return price * (1.0 - PREMIUM_DISCOUNT_RATE);
    }

    private static double seatPrice(SeatType seatType) {
        if (seatType == SeatType.LUXURY) {
            return 30.0;
        } else if (seatType == SeatType.ENHANCED) {
            return 20.0;
        }
        return 10.0;
    }
}
