package front_end.TicketBooking;

import front_end.IRefreshable;
import front_end.TheaterFrame;

import javax.swing.BorderFactory;
import javax.swing.JPanel;
import javax.swing.JLabel;
import javax.swing.JTextField;
import javax.swing.JComboBox;
import javax.swing.JButton;
import javax.swing.SwingConstants;

import java.awt.BorderLayout;
import java.awt.FlowLayout;
import java.awt.GridBagLayout;
import java.awt.GridBagConstraints;
import java.awt.Insets;

public class SearchMovies extends JPanel implements IRefreshable {
    private TheaterFrame frame;

    public SearchMovies(TheaterFrame frame) {
        this.frame = frame;

        // Border padding
        this.setBorder(BorderFactory.createEmptyBorder(16, 16, 16, 16));

        // Layout of the page with 12 pixels of padding between the components
        BorderLayout pageLayout = new BorderLayout(0, 12);
        this.setLayout(pageLayout);

        // Title of the page
        JLabel searchMoviesTitle = new JLabel();
        searchMoviesTitle.setText("Search Movies");
        searchMoviesTitle.setHorizontalAlignment(SwingConstants.CENTER);
        searchMoviesTitle.setFont(searchMoviesTitle.getFont().deriveFont(18f));

        // Panel for the search form
        JPanel searchFormPanel = new JPanel();
        searchFormPanel.setLayout(new GridBagLayout());

        // Label for the movie name input field
        JLabel searchMoviesByNameLabel = new JLabel("Movie Name:");

        // Text field for the movie name input field
        JTextField searchMoviesByNameTextField = new JTextField(12);

        // Label for the location input field
        JLabel searchMoviesByLocationLabel = new JLabel("Location:");

        // Dropdown for the location input field
        JComboBox<String> searchMoviesByLocationDropdown = new JComboBox<>(new String[] { "All", "Indoor", "Outdoor", "Drive-In" });
        searchMoviesByLocationDropdown.setPrototypeDisplayValue("Drive-In");

        // Button for the search location button
        JButton searchMoviesByLocationButton = new JButton("Search Results");
        searchMoviesByLocationButton.addActionListener(e -> {
            // TODO: Implement the logic to search movies by location
        });

        // GridBagConstraints for the search form panel with 8 pixels of padding between the components
        GridBagConstraints searchFormPanelConstraints = new GridBagConstraints();
        searchFormPanelConstraints.insets = new Insets(8, 8, 8, 8);

        // Add the search movies by name label to the search form panel
        searchFormPanelConstraints.gridx = 0;
        searchFormPanelConstraints.gridy = 0;
        searchFormPanelConstraints.weightx = 0;
        searchFormPanelConstraints.anchor = GridBagConstraints.WEST;
        searchFormPanelConstraints.fill = GridBagConstraints.NONE;
        searchFormPanel.add(searchMoviesByNameLabel, searchFormPanelConstraints);

        // Add the search movies by name text field to the search form panel
        searchFormPanelConstraints.gridx = 1;
        searchFormPanelConstraints.gridy = 0;
        searchFormPanelConstraints.weightx = 1;
        searchFormPanelConstraints.anchor = GridBagConstraints.WEST;
        searchFormPanelConstraints.fill = GridBagConstraints.HORIZONTAL;
        searchFormPanel.add(searchMoviesByNameTextField, searchFormPanelConstraints);

        // Add the search movies by location label to the search form panel
        searchFormPanelConstraints.gridx = 2;
        searchFormPanelConstraints.gridy = 0;
        searchFormPanelConstraints.weightx = 0;
        searchFormPanelConstraints.anchor = GridBagConstraints.WEST;
        searchFormPanelConstraints.fill = GridBagConstraints.NONE;
        searchFormPanel.add(searchMoviesByLocationLabel, searchFormPanelConstraints);

        // Add the search movies by location dropdown to the search form panel
        searchFormPanelConstraints.gridx = 3;
        searchFormPanelConstraints.gridy = 0;
        searchFormPanelConstraints.weightx = 1;
        searchFormPanelConstraints.anchor = GridBagConstraints.WEST;
        searchFormPanelConstraints.fill = GridBagConstraints.HORIZONTAL;
        searchFormPanel.add(searchMoviesByLocationDropdown, searchFormPanelConstraints);

        // Add the search movies by location button to the search form panel
        searchFormPanelConstraints.gridx = 1;
        searchFormPanelConstraints.gridy = 1;
        searchFormPanelConstraints.weightx = 0;
        searchFormPanelConstraints.anchor = GridBagConstraints.WEST;
        searchFormPanelConstraints.fill = GridBagConstraints.NONE;
        searchFormPanel.add(searchMoviesByLocationButton, searchFormPanelConstraints);

        // Panel for the results
        JPanel resultsPanel = new JPanel();
        resultsPanel.setBorder(BorderFactory.createTitledBorder("Results"));

        // Panel for search form and results
        JPanel centerPanel = new JPanel();
        centerPanel.setLayout(new BorderLayout(0, 12));
        centerPanel.add(searchFormPanel, BorderLayout.NORTH);
        centerPanel.add(resultsPanel, BorderLayout.CENTER);

        this.add(searchMoviesTitle, BorderLayout.NORTH);
        this.add(centerPanel, BorderLayout.CENTER);
    }

    @Override
    public void refreshCache() {

    }
}
