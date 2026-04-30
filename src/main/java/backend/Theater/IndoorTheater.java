package main.java.backend.Theater;

import main.java.backend.Seating.AssignedSeating;

import java.util.ArrayList;
import java.util.List;

public class IndoorTheater extends Theater {
    private final List<Room> rooms;

    public IndoorTheater(int theaterId, List<Room> rooms) {
        super(theaterId, TheaterType.INDOOR, totalCapacity(rooms), new AssignedSeating());
        this.rooms = new ArrayList<>(rooms);
    }

    public List<Room> getRooms() { return rooms; }

    private static int totalCapacity(List<Room> rs) {
        int total = 0;
        for (Room r : rs) {
            total += r.getCapacity();
        }
        return total;
    }
}
