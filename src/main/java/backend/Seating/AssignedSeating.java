package main.java.backend.Seating;

public class AssignedSeating implements SeatingStrategy {
    @Override
    public String describe() {
        return "Assigned seating";
    }

    @Override
    public String toString() {
        return describe();
    }
}
