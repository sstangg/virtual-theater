package front_end;

import java.awt.CardLayout;

import javax.swing.JFrame;
import javax.swing.JPanel;

import java.util.HashMap;

import front_end.TicketBooking.*;

public class TheaterFrame extends JFrame {
    // Constants for the card layout
    public static final String CARD_WELCOME = "welcome";
    public static final String CARD_THEATER_LOBBY = "theaterLobby";
    public static final String CARD_SEARCH_MOVIES = "searchMovies";
    public static final String CARD_SEAT_CHART = "seatChart";
    public static final String CARD_TICKET_CONFIRMATION = "ticketConfirmation";
    public static final String CARD_CUSTOMER_PROFILE = "customerProfile";

    // Cached variables
    private String[] customerInfo = new String[] { "", "", "", "", "", "", "", "", "Michael, Interstellar", "Popcorn, Soda" };

    private final CardLayout cardLayout = new CardLayout();
    private final JPanel cards = new JPanel(cardLayout);
    private final HashMap<String, IRefreshable> refreshablePanels = new HashMap<>();

    // The pages that are in the application
    private WelcomePanel welcomePanel;
    private TheaterLobby theaterLobbyPanel;
    private SearchMovies searchMoviesPanel;
    private SeatChart seatChartPanel;
    private ComfirmationPage ticketConfirmationPanel;
    private CustomerProfile customerProfilePanel;

    public TheaterFrame() {
        super("Virtual Theater");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(800, 1000);
        setLocationByPlatform(true);

        // Create the panels for the cards
        welcomePanel = new WelcomePanel(this);
        theaterLobbyPanel = new TheaterLobby(this);
        searchMoviesPanel = new SearchMovies(this);
        // TODO: Holds an empty movie initially, replace with the actual movie data
        seatChartPanel = new SeatChart(this, new String[] { "", "", "", "", "", "", "" });
        ticketConfirmationPanel = new ComfirmationPage(this);
        customerProfilePanel = new CustomerProfile(this, customerInfo);

        // Add the panels to the cardLayout
        cards.add(welcomePanel, CARD_WELCOME);
        cards.add(theaterLobbyPanel, CARD_THEATER_LOBBY);
        cards.add(searchMoviesPanel, CARD_SEARCH_MOVIES);
        cards.add(seatChartPanel, CARD_SEAT_CHART);
        cards.add(ticketConfirmationPanel, CARD_TICKET_CONFIRMATION);
        cards.add(customerProfilePanel, CARD_CUSTOMER_PROFILE);

        // Add the refreshable panels to the map
        refreshablePanels.put(CARD_WELCOME, (IRefreshable) welcomePanel);
        refreshablePanels.put(CARD_THEATER_LOBBY, (IRefreshable) theaterLobbyPanel);
        refreshablePanels.put(CARD_SEARCH_MOVIES, (IRefreshable) searchMoviesPanel);
        refreshablePanels.put(CARD_CUSTOMER_PROFILE, (IRefreshable) customerProfilePanel);
     
        // Add the cardLayout to the frame
        add(cards);

        // Show the welcome panel by default
        cardLayout.show(cards, CARD_WELCOME);
    }

    public void showCard(String name) {
        // Refresh the cache information of the panel if it is refreshable
        if (refreshablePanels.containsKey(name)) {
            refreshablePanels.get(name).refreshCache();
        }

        // Show the new card
        cardLayout.show(cards, name);
    }

    public String getUserName() {
        return customerInfo[0];
    }

    public void setUserName(String userName) {
        this.customerInfo[0] = userName;
    }

    public String[] getCustomerInfo() {
        return customerInfo;
    }

    public void setCustomerInfo(String[] customerInfo) {
        System.out.println("Setting customer info " + customerInfo.toString());
        this.customerInfo = customerInfo;
    }

    public void clearCustomerInfo() {
        this.customerInfo = new String[] { "", "", "", "", "", "", "", "", "", "" };
    }

    public void openSeatChart(String[] movie) {
        seatChartPanel.setMovie(movie);
        cardLayout.show(cards, CARD_SEAT_CHART);
    }

    public void openConfirmationPage(String[] movie, String[] seats){
        ticketConfirmationPanel.setBookingDetails(movie, seats);
        cardLayout.show(cards, CARD_TICKET_CONFIRMATION);
    }
}
