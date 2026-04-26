package front_end;

import java.awt.GridBagLayout;
import java.awt.FlowLayout;

import java.awt.GridBagConstraints;
import java.awt.Insets;

import javax.swing.JPanel;
import javax.swing.JLabel;
import javax.swing.JButton;
import javax.swing.SwingConstants;

public class TheaterLobby extends JPanel implements IRefreshable {

    private String userName;
    private TheaterFrame frame;
    private JLabel whereDoYouWantToGoMessage;

    public TheaterLobby(TheaterFrame frame) {
        super();
        this.frame = frame;

        // Set the layout of the panel
        GridBagLayout layout = new GridBagLayout();
        this.setLayout(layout);

        // Create the where do you want to go message label
        whereDoYouWantToGoMessage = new JLabel();
        whereDoYouWantToGoMessage.setHorizontalAlignment(SwingConstants.CENTER);
        whereDoYouWantToGoMessage.setFont(whereDoYouWantToGoMessage.getFont().deriveFont(18f));
        refreshCache();

        // Create the option buttons holder panel with the buttons
        JPanel optionButtonsHolderPanel = new JPanel();
        optionButtonsHolderPanel.setLayout(new FlowLayout(FlowLayout.CENTER));
        JButton browseMoviesButton = new JButton();
        browseMoviesButton.setText("Browse Movies");
        browseMoviesButton.addActionListener(e -> {
            // TODO: Implement the logic to go to the browse movies page
        });
        JButton watchMovieButton = new JButton();
        watchMovieButton.setText("Watch Movie");
        watchMovieButton.addActionListener(e -> {
            // TODO: Implement the logic to go to the watch movie page
        });
        optionButtonsHolderPanel.add(browseMoviesButton);
        optionButtonsHolderPanel.add(watchMovieButton);

        // Create the exit theater button
        JButton exitTheaterButton = new JButton();
        exitTheaterButton.setText("Exit Theater");
        exitTheaterButton.addActionListener(e -> {
            frame.showCard(TheaterFrame.CARD_WELCOME);
        });
        exitTheaterButton.setHorizontalAlignment(SwingConstants.CENTER);

        // Add the labels and buttons to the panel
        GridBagConstraints constraints = new GridBagConstraints(0, 0, 1, 1, 0, 0, GridBagConstraints.CENTER, GridBagConstraints.BOTH, new Insets(0, 0, 0, 0), 0, 0);
        this.add(whereDoYouWantToGoMessage, constraints);

        constraints.gridy = 1;
        this.add(optionButtonsHolderPanel, constraints);
        
        constraints.gridy = 2;
        this.add(exitTheaterButton, constraints);
    }

    @Override
    public void refreshCache() {
        userName = frame.getUserName();
        if (userName == null) {
            userName = "";
        }
        whereDoYouWantToGoMessage.setText("Where do you want to go, " + userName + "?");
    }
}
