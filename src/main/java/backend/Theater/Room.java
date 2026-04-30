package main.java.backend.Theater;

import main.java.backend.Seating.Seat;

import java.util.ArrayList;
import java.util.List;

public class Room {
    private final int roomId;
    private final List<Seat> seats;

    public Room(int roomId) {
        this.roomId = roomId;
        this.seats = new ArrayList<>();
    }

    public int getRoomId() { return roomId; }
    public List<Seat> getSeats() { return seats; }
    public int getCapacity() { return seats.size(); }

    public void addSeat(Seat s) {
        seats.add(s);
    }

    @Override
    public String toString() {
        return "Room#" + roomId + " (" + seats.size() + " seats)";
    }
}
