package front_end.WatchMovies;

import backend.Theater.TheaterType;
import front_end.IRefreshable;
import front_end.TheaterFrame;

import javax.swing.*;
import java.awt.*;

public class WatchMovies extends JPanel implements IRefreshable {
    private TheaterFrame frame;
    private JLabel titleMessage;

    public WatchMovies(TheaterFrame frame) {
        super();
        this.frame = frame;

        // Set the layout of the panel
        GridBagLayout layout = new GridBagLayout();
        this.setLayout(layout);

        // Create the where do you want to go message label
        titleMessage = new JLabel();
        titleMessage.setHorizontalAlignment(SwingConstants.CENTER);
        titleMessage.setFont(titleMessage.getFont().deriveFont(18f));
        titleMessage.setText("Which theater would you like to enter?");
        refreshCache();

        // Create the option buttons holder panel with the buttons
        JPanel optionButtonsHolderPanel = new JPanel();
        optionButtonsHolderPanel.setLayout(new BoxLayout(optionButtonsHolderPanel, BoxLayout.Y_AXIS));
        // TODO: center align the column, make all buttons same size
        // indoor
        JButton indoorTheaterButton = new JButton();
        indoorTheaterButton.setText("Indoor Theater");
        indoorTheaterButton.addActionListener(e -> {
            frame.showCard(TheaterFrame.CARD_WATCH_INDOOR_MOVIES);
            // TODO: deal with room entry inside
        });
        // outdoor
        JButton outdoorTheaterButton = new JButton();
        outdoorTheaterButton.setText("Outdoor Theater");
        outdoorTheaterButton.addActionListener(e -> {
            // TODO:
            //if (frame.allowNonIndoorTheaterEntry(TheaterType.OUTDOOR)) {
                frame.showCard(TheaterFrame.CARD_WATCH_OUTDOOR_MOVIES);
            //}
        });
        // drivein
        JButton driveinTheaterButton = new JButton();
        driveinTheaterButton.setText("Drive-in Theater");
        driveinTheaterButton.addActionListener(e -> {
            //if (frame.allowNonIndoorTheaterEntry(TheaterType.DRIVEIN)) {
                frame.showCard(TheaterFrame.CARD_WATCH_DRIVEIN_MOVIES);
            //}
        });

        optionButtonsHolderPanel.add(indoorTheaterButton);
        optionButtonsHolderPanel.add(outdoorTheaterButton);
        optionButtonsHolderPanel.add(driveinTheaterButton);

        // Create the exit theater button
        JPanel exitTheaterButtonHolderPanel = new JPanel();
        exitTheaterButtonHolderPanel.setLayout(new FlowLayout(FlowLayout.LEFT));
        JButton exitTheaterButton = new JButton();
        exitTheaterButton.setText("Return to Theater Lobby");
        exitTheaterButton.addActionListener(e -> {
            frame.showCard(TheaterFrame.CARD_THEATER_LOBBY);
        });
        exitTheaterButtonHolderPanel.add(exitTheaterButton);

        // Add the labels and buttons to the panel
        GridBagConstraints constraints = new GridBagConstraints(0, 0, 1, 1, 0, 0, GridBagConstraints.CENTER, GridBagConstraints.BOTH, new Insets(0, 0, 0, 0), 0, 0);
        this.add(titleMessage, constraints);

        constraints.gridy = 1;
        this.add(optionButtonsHolderPanel, constraints);

        constraints.gridy = 2;
        this.add(exitTheaterButtonHolderPanel, constraints);
    }

    @Override
    public void refreshCache() {
        // TODO:
        /*
        userName = frame.getUserName();
        if (userName == null) {
            userName = "";
        }
        whereDoYouWantToGoMessage.setText("Which theater would you like to enter, " + userName + "?");
        */
    }
}

