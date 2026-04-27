package main.java.backend.Theater;

import main.java.backend.Seating.UnassignedSeating;

public class DriveInTheater extends Theater {
    public DriveInTheater(int theaterId, TheaterType type, int capacity) {
        super(theaterId, type, capacity, new UnassignedSeating());
    }
    // TODO:
}
