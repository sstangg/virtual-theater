package main.java.backend.Theater;

import main.java.backend.Seating.UnassignedSeating;

public class OutdoorTheater extends Theater {
    public OutdoorTheater(int theaterId, TheaterType type, int capacity) {
        super(theaterId, type, capacity, new UnassignedSeating());
    }
    // TODO:
}
