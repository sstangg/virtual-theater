package main.java.backend.TheaterManager;

import main.java.backend.Theater.IndoorTheater;
import main.java.backend.Theater.Room;
import main.java.backend.Theater.TheaterType;

import java.util.List;

public class SeatedTheaterManager extends TheaterManager {
    public SeatedTheaterManager() {
        super(TheaterType.INDOOR);
    }

    public IndoorTheater createTheater(int theaterId, List<Room> rooms) {
        IndoorTheater t = new IndoorTheater(theaterId, rooms);
        addTheater(t);
        return t;
    }
}
