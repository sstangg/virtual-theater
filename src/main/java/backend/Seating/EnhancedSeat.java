package backend.Seating;

public class EnhancedSeat extends Seat {
    public static final double PRICE = 20.0;

    public EnhancedSeat(int seatId) {
        super(seatId, PRICE, SeatType.ENHANCED);
    }

    @Override
    public String getServiceDescription() {
        return "Premium seating with extra legroom";
    }
}
