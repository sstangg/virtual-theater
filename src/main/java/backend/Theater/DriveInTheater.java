package main.java.backend.Theater;

import main.java.backend.Seating.UnassignedSeating;

public class DriveInTheater extends Theater {
    public DriveInTheater(int theaterId, int capacity) {
        super(theaterId, TheaterType.DRIVEIN, capacity, new UnassignedSeating());
    }
}
