package main.java.backend.Theater;

import main.java.backend.Seating.SeatingStrategy;
import main.java.backend.TheaterSchedule.Schedule;

import java.util.Objects;

public abstract class Theater {
    private final int theaterId;
    private Schedule schedule;
    private final int capacity;
    private final SeatingStrategy seatingStrategy;
    private final TheaterType type;

    public Theater(int theaterId, TheaterType type, int capacity, SeatingStrategy seatingStrategy) {
        this.type = type;
        this.theaterId = theaterId;
        this.capacity = capacity;
        this.seatingStrategy = seatingStrategy;
    }

    // GETTERS & SETTERS
    public int getTheaterId() { return theaterId; }
    public int getCapacity() { return capacity; }
    public SeatingStrategy getSeatingStrategy() { return seatingStrategy; }
    public TheaterType getType() { return type; }

    public void setSchedule(Schedule schedule) {
        this.schedule = schedule;
    }

    @Override
    public String toString() { return String.valueOf(getTheaterId()); }

    @Override
    public boolean equals(Object t) {
        if (Objects.isNull(t) || !(t instanceof Theater)) { return false;}
        return theaterId == ((Theater)t).theaterId;
    }
}