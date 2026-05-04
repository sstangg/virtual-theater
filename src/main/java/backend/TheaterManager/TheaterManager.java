package backend.TheaterManager;

import backend.Customer;
import backend.Theater.Theater;
import backend.Theater.TheaterType;
import backend.Tickets.SeatedTicket;
import backend.Tickets.Ticket;

import java.util.ArrayList;
import java.util.List;

public abstract class TheaterManager {
    private final List<Theater> theaters;
    private final List<Customer> customers;
    private final List<Ticket> tickets;
    private final TheaterType type;

    public TheaterManager(TheaterType type) {
        this.theaters = new ArrayList<>();
        this.customers = new ArrayList<>();
        this.tickets = new ArrayList<>();
        this.type = type;
    }

    // GETTERS & SETTERS
    public TheaterType getType() { return type; }

    public Theater getTheater(int i) { return theaters.get(i); }
    public List<Theater> getTheaters() { return theaters; }
    public void addTheater(Theater t) { theaters.add(t); }

    public Customer getCustomer(int i) { return customers.get(i); }
    public List<Customer> getCustomers() { return customers; }
    public void addCustomer(Customer c) { customers.add(c); }

    public List<Ticket> getTickets() { return tickets; }
    public void addTicket(Ticket t) { tickets.add(t); }

    // SEAT-CONFLICT + LOOKUP HELPERS (used by frontend booking + watching flows)
    public boolean isSeatBooked(int seatId, int showingId) {
        for (Ticket t : tickets) {
            if (t instanceof SeatedTicket
                    && t.getShowingId() == showingId
                    && ((SeatedTicket) t).getSeatId() == seatId) {
                return true;
            }
        }
        return false;
    }

    public List<Ticket> getTicketsForCustomer(int customerId) {
        List<Ticket> result = new ArrayList<>();
        for (Ticket t : tickets) {
            if (t.getUserId() == customerId) {
                result.add(t);
            }
        }
        return result;
    }

    public boolean hasTicketForShowing(int customerId, int showingId) {
        for (Ticket t : tickets) {
            if (t.getUserId() == customerId && t.getShowingId() == showingId) {
                return true;
            }
        }
        return false;
    }

    @Override
    public String toString() {
        return getType().toString();
    }
}
