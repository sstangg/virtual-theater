package main.java.backend.Seating;

public class SeatFactory {
    private static int nextId = 1;

    public static Seat createBasicSeat() {
        return new BasicSeat(nextId++);
    }

    public static Seat createEnhancedSeat() {
        return new EnhancedSeat(nextId++);
    }

    public static Seat createLuxurySeat() {
        return new LuxurySeat(nextId++);
    }
}
