package backend.TheaterManager;

import backend.Theater.DriveInTheater;
import backend.Theater.TheaterType;

public class DriveInTheaterManager extends TheaterManager {
    public DriveInTheaterManager() {
        super(TheaterType.DRIVEIN);
    }

    public DriveInTheater createTheater(int theaterId, int capacity) {
        DriveInTheater t = new DriveInTheater(theaterId, capacity);
        addTheater(t);
        return t;
    }
}
