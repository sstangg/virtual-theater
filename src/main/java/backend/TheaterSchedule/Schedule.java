package main.java.backend.TheaterSchedule;
import java.util.ArrayList;
import java.util.List;

public class Schedule {
    private List<Showing> showings;

    public Schedule() {
        showings = new ArrayList<>();
    }

    // GETTERS & SETTERS
    public List<Showing> getShowings() {
        return showings;
    }
    public Showing getShowing(int i) {
        return showings.get(i);
    }
    public void addShowing(Showing s) {
        this.showings.add(s);
    }

    // TODO: order showings by start time?
}