package main.java.backend.TheaterManager;

import main.java.backend.Theater.OutdoorTheater;
import main.java.backend.Theater.TheaterType;

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
