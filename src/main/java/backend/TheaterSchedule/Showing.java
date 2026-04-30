package main.java.backend.TheaterSchedule;

import java.sql.Time;

public abstract class Showing {
    private int showingId;
    private int theaterId; // necessary?
    private Time startTime;
    private Time endTime;
    private boolean isSingle;

    public Showing(boolean isSingle) {
        this.isSingle = isSingle;
    }

    // TODO: figure out intialization, whether it needs this many attributes, methods, etc
}