package main.java.backend.Theater;

import main.java.backend.Seating.UnassignedSeating;

public class OutdoorTheater extends Theater {
    public OutdoorTheater(int theaterId, int capacity) {
        super(theaterId, TheaterType.OUTDOOR, capacity, new UnassignedSeating());
    }
}
