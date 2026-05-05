package backend.TheaterSchedule;

import backend.Theater.Theater;
import backend.Theater.TheaterType;

import java.sql.Time;

public abstract class Showing {
    private int showingId;
    private int movieId; // necessary?
    private Time startTime;
    private Time endTime;
    private boolean isSingle;
    private TheaterType theaterType;

    public Showing(boolean isSingle, Time startTime, Time endTime, TheaterType theaterType) {
        this.isSingle = isSingle;
        this.startTime = startTime;
        this.endTime = endTime;
        this.theaterType = theaterType;
    }

    // GETTERS & SETTERS
    public Time getStartTime() { return startTime; }
    public Time getEndTime() { return endTime; }
    public boolean isSingle() { return isSingle; }
    public int getMovieId() { return movieId; }
    public int getShowingId() { return showingId; }
    public TheaterType getTheaterType() { return theaterType; }

    public void setMovieId(int movieId) { this.movieId = movieId; }
    public void setShowingId(int showingId) { this.showingId = showingId; }

    // TODO: figure out intialization, whether it needs this many attributes, methods, etc
}