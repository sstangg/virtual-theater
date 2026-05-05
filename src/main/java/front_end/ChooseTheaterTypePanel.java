package front_end;

import java.awt.FlowLayout;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;

import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.SwingConstants;

/*
 * ChooseTheaterTypePanel is a simple panel that lets the user choose a theater type
 * before proceeding to browse available movies.
 */
public class ChooseTheaterTypePanel extends JPanel implements IRefreshable {

    private final TheaterFrame frame;
    private final JLabel titleLabel;

    public ChooseTheaterTypePanel(TheaterFrame frame) {
        super();
        this.frame = frame;

        GridBagLayout layout = new GridBagLayout();
        this.setLayout(layout);

        titleLabel = new JLabel();
        titleLabel.setHorizontalAlignment(SwingConstants.CENTER);
        titleLabel.setFont(titleLabel.getFont().deriveFont(18f));
        refreshCache();

        JPanel buttonsHolder = new JPanel(new FlowLayout(FlowLayout.CENTER));
        JButton indoorButton = new JButton("Indoor");
        indoorButton.addActionListener(e -> 
            System.out.println("Indoor selected")
        );
        JButton outdoorButton = new JButton("Outdoor");
        outdoorButton.addActionListener(e -> 
            System.out.println("Outdoor selected")
        );
        JButton driveInButton = new JButton("Drive-In");
        driveInButton.addActionListener(e -> 
            System.out.println("Drive-In selected")
        );
        buttonsHolder.add(indoorButton);
        buttonsHolder.add(outdoorButton);
        buttonsHolder.add(driveInButton);

        JPanel backHolder = new JPanel();
        backHolder.setLayout(new FlowLayout(FlowLayout.CENTER));
        JButton backButton = new JButton();
        backButton.setText("Back to Lobby");
        backButton.addActionListener(e -> 
            frame.showCard(TheaterFrame.CARD_THEATER_LOBBY)
        );
        backHolder.add(backButton);

        GridBagConstraints constraints = new GridBagConstraints(0, 0, 1, 1, 0, 0, GridBagConstraints.CENTER, GridBagConstraints.BOTH, new Insets(0, 0, 0, 0), 0, 0);
        this.add(titleLabel, constraints);

        constraints.gridy = 1;
        this.add(buttonsHolder, constraints);

        constraints.gridy = 2;
        this.add(backHolder, constraints);
    }

    @Override
    public void refreshCache() {
    }
}

