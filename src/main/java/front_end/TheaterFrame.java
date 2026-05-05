package front_end;

import java.awt.CardLayout;
import javax.swing.JFrame;
import javax.swing.JPanel;

import java.time.LocalTime;
import java.util.HashMap;
import java.util.List;

import backend.Customer;
import backend.Theater.TheaterType;
import backend.Tickets.Ticket;
import front_end.WatchMovies.WatchDriveinMovies;
import front_end.WatchMovies.WatchIndoorMovies;
import front_end.WatchMovies.WatchMovies;

import front_end.TicketBooking.*;
import front_end.WatchMovies.WatchOutdoorMovies;

/*
 * TheaterFrame is the main frame that contains all the panels for the application.
 * It uses a CardLayout to switch between the different panels.
 * It also holds information between the panels, and allows the different panels to be updated accordingly.
 */
public class TheaterFrame extends JFrame {
    // Constants for the card layout
    public static final String CARD_WELCOME = "welcome";
    public static final String CARD_THEATER_LOBBY = "theaterLobby";
    public static final String CARD_SEARCH_MOVIES = "searchMovies";
    public static final String CARD_WATCH_MOVIES = "watchMovies";

    public static final String CARD_WATCH_INDOOR_MOVIES = "watchIndoorMovies";
    public static final String CARD_WATCH_OUTDOOR_MOVIES = "watchOutdoorMovies";
    public static final String CARD_WATCH_DRIVEIN_MOVIES = "watchDriveInMovies";


    public static final String CARD_SEAT_CHART = "seatChart";
    public static final String CARD_FOOD_SELECTION = "foodSelection";
    public static final String CARD_TICKET_CONFIRMATION = "ticketConfirmation";
    public static final String CARD_CUSTOMER_PROFILE = "customerProfile";
    public static final String CARD_PREMIUM_PAYMENT = "premiumPayment";

    private String[] customerInfo = new String[] { "", "", "", "", "", "", "", "", "", "" };

    private final CardLayout cardLayout = new CardLayout();
    /*
     * This is the “stack” of screens.
     * CardLayout lets us switch between pages by name (welcome, lobby, etc.).
     *
     * We keep this panel transparent so the root wrapper (`ThemedShell`) can paint the
     * background image behind it.
     */
    private final JPanel cards = ThemedShell.createCardsPanel(cardLayout);
    private final HashMap<String, IRefreshable> refreshablePanels = new HashMap<>();

    // The pages that are in the application
    private WelcomePanel welcomePanel;
    private TheaterLobby theaterLobbyPanel;
    private SearchMovies searchMoviesPanel;
    private WatchMovies watchMoviesPanel;
    private SeatChart seatChartPanel;
    private FoodSelectionPanel foodSelectionPanel;
    private ConfirmationPage ticketConfirmationPanel;
    private CustomerProfile customerProfilePanel;
    private PremiumPaymentPage premiumPaymentPage;

    // theaters
    private WatchOutdoorMovies watchOutdoorMovies;
    private WatchDriveinMovies watchDriveinMovies;
    private WatchIndoorMovies watchIndoorMovies;

    private final testManager manager;

    // Booking state for the in-progress purchase. Replaces the old String[] movie carrier.
    private BookingDraft bookingDraft;

    // The signed-in customer (created on welcome). Owns tickets + foods after purchase.
    private Customer customer;

    public TheaterFrame(String[][] searchMovieRows, testManager manager) {
        super("Virtual Theater");
        this.manager = manager;
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(800, 1000);
        setLocationByPlatform(true);

        // Create the panels for the cards
        welcomePanel = new WelcomePanel(this);
        theaterLobbyPanel = new TheaterLobby(this);
        searchMoviesPanel = new SearchMovies(this, searchMovieRows);
        watchMoviesPanel = new WatchMovies(this);
        seatChartPanel = new SeatChart(this);
        foodSelectionPanel = new FoodSelectionPanel(this, manager);
        ticketConfirmationPanel = new ConfirmationPage(this);
        customerProfilePanel = new CustomerProfile(this, customerInfo);
        premiumPaymentPage = new PremiumPaymentPage(this);

        // theaters
        watchOutdoorMovies = new WatchOutdoorMovies(this);
        watchIndoorMovies = new WatchIndoorMovies(this);
        watchDriveinMovies = new WatchDriveinMovies(this);

        // TODO: Update looks for cards
        // Apply the card surface to the panels
        applyCardSurface(
                welcomePanel,
                theaterLobbyPanel,
                searchMoviesPanel,
                watchMoviesPanel,
                seatChartPanel,
                foodSelectionPanel,
                ticketConfirmationPanel,
                customerProfilePanel,
                premiumPaymentPage);

        // Add the panels to the cardLayout
        cards.add(welcomePanel, CARD_WELCOME);
        cards.add(theaterLobbyPanel, CARD_THEATER_LOBBY);
        cards.add(searchMoviesPanel, CARD_SEARCH_MOVIES);

        cards.add(watchMoviesPanel, CARD_WATCH_MOVIES);
        cards.add(watchDriveinMovies, CARD_WATCH_INDOOR_MOVIES);
        cards.add(watchOutdoorMovies, CARD_WATCH_OUTDOOR_MOVIES);
        cards.add(watchDriveinMovies, CARD_WATCH_DRIVEIN_MOVIES);

        cards.add(seatChartPanel, CARD_SEAT_CHART);
        cards.add(foodSelectionPanel, CARD_FOOD_SELECTION);
        cards.add(ticketConfirmationPanel, CARD_TICKET_CONFIRMATION);
        cards.add(customerProfilePanel, CARD_CUSTOMER_PROFILE);
        cards.add(premiumPaymentPage, CARD_PREMIUM_PAYMENT);

        // Add the refreshable panels to the map
        refreshablePanels.put(CARD_WELCOME, (IRefreshable) welcomePanel);
        refreshablePanels.put(CARD_THEATER_LOBBY, (IRefreshable) theaterLobbyPanel);
        refreshablePanels.put(CARD_SEARCH_MOVIES, (IRefreshable) searchMoviesPanel);
        // TODO: add Movie?
        refreshablePanels.put(CARD_WATCH_OUTDOOR_MOVIES, (IRefreshable) watchDriveinMovies);
        refreshablePanels.put(CARD_WATCH_OUTDOOR_MOVIES, (IRefreshable) watchIndoorMovies);
        refreshablePanels.put(CARD_WATCH_OUTDOOR_MOVIES, (IRefreshable) watchOutdoorMovies);


        refreshablePanels.put(CARD_CUSTOMER_PROFILE, (IRefreshable) customerProfilePanel);

        /*
         * ThemedShell is the simple visual wrapper:
         * - paints background image
         * - adds a solid header with text
         * - paints a low-opacity outer border overlay
         */
        add(new ThemedShell(cards));
        cardLayout.show(cards, CARD_WELCOME);
    }
    // THEATER ENTRY FUNCTIONS ------------------------------------------------------------
    public boolean allowNonIndoorTheaterEntry(TheaterType theaterType) {
        if (theaterType == TheaterType.INDOOR) {
            throw new IllegalArgumentException("Must be nonindoor theater");
        }
        // get customer tickets for that theater type
        List<Ticket> tickets = manager.getTicketsForTheaterType(theaterType, customer);
        if (tickets.isEmpty()) {return false; }
        // get showing id
        int playingShowingId = manager.showingIdPlayingNow(theaterType, LocalTime.now());
        if (playingShowingId == -1) {return false; }

        // search for matching showing
        for (Ticket t : tickets) {
            if (t.getShowingId() == playingShowingId) {
                return true;
            }
        }
        return false;
    }

    public boolean allowIndoorTheaterEntry() {
        // TODO:
        // check room
        List<Ticket> tickets = manager.getTicketsForTheaterType(TheaterType.INDOOR, customer);
        if (tickets.isEmpty()) {return false; }
        int playingShowingId = manager.showingIdPlayingNow(TheaterType.INDOOR, LocalTime.now());
        if (playingShowingId == -1) {return false; }

        // search for matching showing
        for (Ticket t : tickets) {
            if (t.getShowingId() == playingShowingId) {
                return true;
            }
        }
        return false;
    }


    // Show the new card
    public void showCard(String name) {
        // Refresh the cache information of the panel if it is refreshable
        if (refreshablePanels.containsKey(name)) {
            refreshablePanels.get(name).refreshCache();
        }

        // Show the new card
        cardLayout.show(cards, name);
    }

    // Get the customer object
    public Customer getCustomer() {
        return customer;
    }

    // Set the customer object
    public void setCustomer(Customer customer) {
        this.customer = customer;
    }

    // Get the user name
    public String getUserName() {
        if (this.customer == null) {
            return "";
        }
        return this.customer.getName();
    }

    // Set the user name
    public void setUserName(String userName) {
        this.customerInfo[0] = userName;
    }

    // Get the customer information
    public String[] getCustomerInfo() {
        return customerInfo;
    }

    // Set the customer information
    public void setCustomerInfo(String[] customerInfo) {
        this.customerInfo = customerInfo;
    }

    // Clear the customer information
    public void clearCustomerInfo() {
        this.customerInfo = new String[] { "", "", "", "", "", "", "", "", "", "" };
        this.customer = null;
        this.bookingDraft = null;
    }

    // Booking draft to keep track of the current booking
    public BookingDraft getBookingDraft() {
        return bookingDraft;
    }

    // Set the booking draft
    public void setBookingDraft(BookingDraft draft) {
        this.bookingDraft = draft;
    }

    // Clear the booking draft
    public void clearBookingDraft() {
        this.bookingDraft = null;
    }

    // Open the seat chart panel
    public void openSeatChart() {
        seatChartPanel.setBooking(bookingDraft);
        cardLayout.show(cards, CARD_SEAT_CHART);
    }

    // Open the food selection panel
    public void openFoodSelection() {
        foodSelectionPanel.beginOrder(bookingDraft);
        cardLayout.show(cards, CARD_FOOD_SELECTION);
    }

    // Open the confirmation page
    public void openConfirmationPage() {
        ticketConfirmationPanel.setBooking(bookingDraft);
        cardLayout.show(cards, CARD_TICKET_CONFIRMATION);
    }

    public void openPremiumPaymentPage() {
        cardLayout.show(cards, CARD_PREMIUM_PAYMENT);
    }

    // Get the manager
    public testManager getManager() {
        return manager;
    }

    // Apply the card surface to the panels
    private static void applyCardSurface(JPanel... roots) {
        for (JPanel root : roots) {
            root.setOpaque(false);
        }
    }
}
