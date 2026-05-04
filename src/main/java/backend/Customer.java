package backend;

import backend.FoodService.Food;
import backend.Seating.SeatType;
import backend.Tickets.SeatedTicket;
import backend.Tickets.Ticket;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class Customer {
    private final int customerId;
    private final String name;
    private boolean isPreferred;
    private final List<Ticket> tickets;
    private final List<Food> foods;

    public Customer(int customerId, String name, boolean isPreferred) {
        this.customerId = customerId;
        this.name = name;
        this.isPreferred = isPreferred;
        this.tickets = new ArrayList<>();
        this.foods = new ArrayList<>();
    }

    // GETTERS & SETTERS
    public int getCustomerId() { return customerId; }
    public String getName() { return name; }
    public boolean isPreferred() { return isPreferred; }
    public void setPreferred(boolean preferred) { isPreferred = preferred; }

    public List<Ticket> getTickets() { return tickets; }
    public void addTicket(Ticket t) { tickets.add(t); }

    public List<Food> getFoods() { return foods; }
    public void addFood(Food f) { foods.add(f); }

    /**
     * Watching-Movie simulation (simplest version).
     * Frontend passes the showingId + movieName directly so this method does not
     * depend on the (still-evolving) Showing/Movie classes Sophia owns.
     */
    public String watchMovie(int showingId, String movieName) {
        Ticket ticket = findTicketForShowing(showingId);
        if (ticket == null) {
            return "You do not have a ticket for this showing.";
        }
        StringBuilder sb = new StringBuilder();
        sb.append("=== Now Showing: ").append(movieName).append(" ===\n");
        sb.append(experienceFor(ticket)).append("\n");
        sb.append("Snacks: ").append(snackList()).append("\n");
        sb.append("=== Enjoy your movie, ").append(name).append("! ===");
        return sb.toString();
    }

    private Ticket findTicketForShowing(int showingId) {
        for (Ticket t : tickets) {
            if (t.getShowingId() == showingId) {
                return t;
            }
        }
        return null;
    }

    private static String experienceFor(Ticket ticket) {
        if (ticket instanceof SeatedTicket) {
            SeatType type = ((SeatedTicket) ticket).getSeatType();
            if (type == SeatType.LUXURY) {
                return "Luxury seat - recliner with table service. Press the call button anytime.";
            } else if (type == SeatType.ENHANCED) {
                return "Enhanced seat - extra legroom, premium sound.";
            } else {
                return "Basic seat - standard view, standard sound.";
            }
        }
        return "Open-air viewing - find your spot, tune your radio, enjoy the show.";
    }

    private String snackList() {
        if (foods.isEmpty()) {
            return "(none)";
        }
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < foods.size(); i++) {
            if (i > 0) {
                sb.append(", ");
            }
            sb.append(foods.get(i).getName());
        }
        return sb.toString();
    }

    // BASIC METHODS
    @Override
    public String toString() { return getName(); }

    @Override
    public boolean equals(Object c) {
        if (Objects.isNull(c) || !(c instanceof Customer)) { return false; }
        return customerId == ((Customer) c).customerId;
    }
}
