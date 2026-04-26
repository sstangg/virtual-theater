package front_end;

import java.awt.CardLayout;

import javax.swing.JFrame;
import javax.swing.JPanel;

import java.util.HashMap;

public class TheaterFrame extends JFrame {
    // Constants for the card layout
    public static final String CARD_WELCOME = "welcome";
    public static final String CARD_THEATER_LOBBY = "theaterLobby";

    // Cached variables
    private String userName;

    private final CardLayout cardLayout = new CardLayout();
    private final JPanel cards = new JPanel(cardLayout);
    private final HashMap<String, IRefreshable> refreshablePanels = new HashMap<>();

    public TheaterFrame() {
        super("Virtual Theater");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(800, 600);
        setLocationByPlatform(true);

        // Create the panels for the cards
        JPanel welcomePanel = new WelcomePanel(this);
        JPanel theaterLobbyPanel = new TheaterLobby(this);

        // Add the panels to the cardLayout
        cards.add(welcomePanel, CARD_WELCOME);
        cards.add(theaterLobbyPanel, CARD_THEATER_LOBBY);

        // Add the refreshable panels to the map
        refreshablePanels.put(CARD_THEATER_LOBBY, (IRefreshable) theaterLobbyPanel);
     
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
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }
}
