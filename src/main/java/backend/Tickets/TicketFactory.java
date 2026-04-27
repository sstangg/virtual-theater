package main.java.backend.Tickets;

public class TicketFactory {
    // TODO: seated parameters? what params..
    public static Ticket createTicket(boolean seated) {
        if (seated) {
            return new SeatedTicket();
        }else {
            return new UnseatedTicket();
        }
    }
}
