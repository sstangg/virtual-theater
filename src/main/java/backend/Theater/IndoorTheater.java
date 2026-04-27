package main.java.backend.Theater;

import main.java.backend.Seating.AssignedSeating;

public class IndoorTheater extends Theater {
    public IndoorTheater(int theaterId, TheaterType type, int capacity) {
        super(theaterId, type, capacity, new AssignedSeating());
    }
    // TODO:
}
