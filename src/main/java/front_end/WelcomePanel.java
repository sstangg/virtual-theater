package front_end;

import java.awt.GridBagLayout;

import java.awt.GridBagConstraints;
import java.awt.Insets;

import java.awt.FlowLayout;

import javax.swing.JTextField;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JButton;
import javax.swing.JOptionPane;
import javax.swing.SwingConstants;

public class WelcomePanel extends JPanel {

    private String userName;
    private TheaterFrame frame;

    public WelcomePanel(TheaterFrame frame) {
        super();
        this.frame = frame;

        // Set the layout of the panel
        GridBagLayout layout = new GridBagLayout();
        this.setLayout(layout);

        // Create the welcome title label
        JLabel welcomeMessage = new JLabel();
        welcomeMessage.setText("Welcome to the Virtual Theater!");
        welcomeMessage.setHorizontalAlignment(SwingConstants.CENTER);
        welcomeMessage.setFont(welcomeMessage.getFont().deriveFont(18f));

        // Create the user name input field
        JPanel userNamePanel = new JPanel();
        userNamePanel.setLayout(new FlowLayout(FlowLayout.CENTER));
        JLabel userNameLabel = new JLabel("User Name:"); // Label for the user name input field
        JTextField userNameTextField = new JTextField(20); // Text field for the user name input
        userNamePanel.add(userNameLabel);
        userNamePanel.add(userNameTextField);

        // Create the button and place it in the center of the panel
        JButton startButton = new JButton("Enter Theatre");
        startButton.addActionListener(e -> {
            userName = userNameTextField.getText();
            if (userName.isEmpty()) {
                JOptionPane.showMessageDialog(this, "Please enter a user name.", "Error", JOptionPane.ERROR_MESSAGE);
                return;
            }
            frame.setUserName(userName);
            frame.showCard(TheaterFrame.CARD_THEATER_LOBBY);
        });
        startButton.setHorizontalAlignment(SwingConstants.CENTER);

        // Add the label to the panel
        GridBagConstraints constraints = new GridBagConstraints(0, 0, 1, 1, 0, 0, GridBagConstraints.CENTER, GridBagConstraints.BOTH, new Insets(0, 0, 0, 0), 0, 0);
        this.add(welcomeMessage, constraints);
        
        constraints.gridy = 1;
        this.add(userNamePanel, constraints);

        constraints.gridy = 2;
        constraints.fill = GridBagConstraints.NONE;
        this.add(startButton, constraints);
    }
    
    public void refreshCache() {
        userName = frame.getUserName();
    }
}