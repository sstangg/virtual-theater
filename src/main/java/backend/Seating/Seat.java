package main.java.backend.Seating;

public abstract class Seat {
    private final int seatId;
    private final double price;
    private final SeatType type;

    protected Seat(int seatId, double price, SeatType type) {
        this.seatId = seatId;
        this.price = price;
        this.type = type;
    }

    public int getSeatId() { return seatId; }
    public double getPrice() { return price; }
    public SeatType getType() { return type; }

    public abstract String getServiceDescription();

    @Override
    public String toString() {
        return "Seat#" + seatId + " (" + type + ", $" + price + ")";
    }
}
