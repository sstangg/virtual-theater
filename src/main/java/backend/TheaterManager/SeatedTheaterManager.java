package backend.TheaterManager;

import backend.Theater.IndoorTheater;
import backend.Theater.Room;
import backend.Theater.TheaterType;

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
