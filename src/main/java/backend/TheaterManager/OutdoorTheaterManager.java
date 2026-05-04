package backend.TheaterManager;

import backend.Theater.OutdoorTheater;
import backend.Theater.TheaterType;

public class OutdoorTheaterManager extends TheaterManager {
    public OutdoorTheaterManager() {
        super(TheaterType.OUTDOOR);
    }

    public OutdoorTheater createTheater(int theaterId, int capacity) {
        OutdoorTheater t = new OutdoorTheater(theaterId, capacity);
        addTheater(t);
        return t;
    }
}
