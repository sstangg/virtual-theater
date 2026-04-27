package main.java.backend.TheaterManager;

import main.java.backend.Customer;
import main.java.backend.Theater.Theater;
import main.java.backend.Theater.TheaterType;

import java.util.ArrayList;
import java.util.List;


public abstract class TheaterManager {
    private List<Theater> theaters;
    private List<Customer> customers; // TODO: should this keep a list??? then theatermanagers can have overlapping customers...
    private final TheaterType type;

    public TheaterManager(TheaterType type) {
        theaters = new ArrayList<>();
        customers = new ArrayList<>();
        this.type = type;
    }

    // GETTER & SETTERS
    public TheaterType getType() { return type; }
    public Theater getTheater(int i) { return theaters.get(i); }
    public Customer getCustomer(int i) { return customers.get(i); }

    @Override
    public String toString() {
        return getType().toString();
    }

}