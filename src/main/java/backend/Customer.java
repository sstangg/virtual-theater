package main.java.backend;

import java.util.Objects;

public class Customer {
    private final int customerId;
    private final String name;
    private boolean isPreferred;

    public Customer(int customerId, String name, boolean isPreferred) {
        this.customerId = customerId;
        this.name = name;
        this.isPreferred = isPreferred;
    }



    // GETTERS & SETTERS
    public int getCustomerId() { return customerId; }

    public String getName() { return name; }

    public boolean isPreferred() { return isPreferred; }

    public void setPreferred(boolean preferred) { isPreferred = preferred; }

    // BASIC METHODS
    @Override
    public String toString() { return getName(); }

    @Override
    public boolean equals(Object c) {
        if (Objects.isNull(c) || !(c instanceof Customer)) { return false;}
        return customerId == ((Customer)c).customerId;
    }
}