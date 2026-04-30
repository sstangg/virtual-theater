package main.java.backend.Seating;

public class BasicSeat extends Seat {
    public static final double PRICE = 3.0;

    public BasicSeat(int seatId) {
        super(seatId, PRICE, SeatType.BASIC);
    }

    @Override
    public String getServiceDescription() {
        return "Standard seating";
    }
}
