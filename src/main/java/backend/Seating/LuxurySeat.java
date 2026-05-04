package backend.Seating;

public class LuxurySeat extends Seat {
    public static final double PRICE = 30.0;

    public LuxurySeat(int seatId) {
        super(seatId, PRICE, SeatType.LUXURY);
    }

    @Override
    public String getServiceDescription() {
        return "First-class service with recliners and table service";
    }
}
