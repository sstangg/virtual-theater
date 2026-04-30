package main.java.backend.Seating;

public class UnassignedSeating implements SeatingStrategy {
    @Override
    public String describe() {
        return "Unassigned (first come, first served)";
    }

    @Override
    public String toString() {
        return describe();
    }
}
