package backend.Theater;

import backend.Seating.UnassignedSeating;

public class DriveInTheater extends Theater {
    public DriveInTheater(int theaterId, int capacity) {
        super(theaterId, TheaterType.DRIVEIN, capacity, new UnassignedSeating());
    }
}
