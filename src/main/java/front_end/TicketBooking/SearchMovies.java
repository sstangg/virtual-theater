package front_end.TicketBooking;

import front_end.IRefreshable;
import front_end.TheaterFrame;

import javax.swing.BorderFactory;
import javax.swing.JPanel;
import javax.swing.JLabel;
import javax.swing.JTextField;
import javax.swing.JComboBox;
import javax.swing.JButton;
import javax.swing.JTable;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.SwingConstants;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableModel;

import java.awt.BorderLayout;
import java.awt.GridBagLayout;
import java.awt.GridBagConstraints;
import java.awt.Insets;
import java.awt.FlowLayout;
import java.awt.Dimension;
import java.util.ArrayList;

public class SearchMovies extends JPanel implements IRefreshable {
    // Model column indexes for the extra info (kept in the data, hidden in the table).
    private static final int MOVIE_NAME_MODEL_COLUMN = 0;
    private static final int LOCATION_MODEL_COLUMN = 1;
    private static final int RATED_MODEL_COLUMN = 2;
    private static final int RELEASE_YEAR_MODEL_COLUMN = 3;
    private static final int RUNTIME_MODEL_COLUMN = 4;
    private static final int DESCRIPTION_MODEL_COLUMN = 5;
    private static final int SHOWTIMES_MODEL_COLUMN = 6;

    private TheaterFrame frame;

    private String[][] movies = {
        { "Interstellar", "Indoor", "PG-13", "2014", "169 mins", "This is a movie about a group of astronauts who travel to a new planet to find a new home for humanity.", "12:00, 16:00, 18:00, 20:00"},
        { "Project Hail Mary", "Outdoor", "PG-13", "2026", "156 mins", "This is a movie about a scientist who travels interstellar on a mission to save the Earth.", "09:00, 11:00, 15:00, 22:00"},
        { "Michael", "Drive-In", "PG-13", "2026", "130 mins", "The early life of the famous musician Michael Jackson, known as the King of Pop.", "10:00, 14:00, 17:00, 21:00"},
    };
    private ArrayList<String[]> filteredMovies = new ArrayList<>();

    private JTable moviesTable;
    private JScrollPane moviesTableScrollPane;
    private JTextArea movieDescriptionArea;
    private JPanel showtimesPanel;

    public SearchMovies(TheaterFrame frame) {
        this.frame = frame;

        moviesTable = buildMoviesTable(movies);
        moviesTableScrollPane = new JScrollPane(moviesTable);

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
            String nameQuery = searchMoviesByNameTextField.getText().trim().toLowerCase();

            String location = (String) searchMoviesByLocationDropdown.getSelectedItem();
            if (location == null) {
                location = "All";
            }

            // Get the movies that match the search criteria
            filteredMovies.clear();
            for (String[] movie : movies) {
                boolean matchesLocation = location.equals("All") || movie[1].equals(location);
                boolean matchesName = nameQuery.isEmpty() || movie[0].toLowerCase().contains(nameQuery);
                if (matchesLocation && matchesName) {
                    filteredMovies.add(movie);
                }
            }
            movieDescriptionArea.setText("Click a movie in the table to see its description here.");
            updateMoviesTable(filteredMovies.toArray(new String[0][]));
        });

        // Button for the reset search button
        JButton resetSearchButton = new JButton("Reset Search");
        resetSearchButton.addActionListener(e -> {
            searchMoviesByNameTextField.setText("");
            searchMoviesByLocationDropdown.setSelectedItem("All");
            movieDescriptionArea.setText("Click a movie in the table to see its description here.");
            updateMoviesTable(movies);
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

        // Button holder panel for the search and reset buttons
        JPanel buttonHolderPanel = new JPanel();
        buttonHolderPanel.setLayout(new FlowLayout(FlowLayout.LEFT, 0, 0));
        buttonHolderPanel.add(searchMoviesByLocationButton);
        buttonHolderPanel.add(resetSearchButton);

        // Add the button holder panel to the search form panel
        searchFormPanelConstraints.gridx = 1;
        searchFormPanelConstraints.gridy = 1;
        searchFormPanelConstraints.weightx = 0;
        searchFormPanelConstraints.anchor = GridBagConstraints.WEST;
        searchFormPanelConstraints.fill = GridBagConstraints.NONE;
        searchFormPanel.add(buttonHolderPanel, searchFormPanelConstraints);

        // Panel for the results: table on top, description under it when you click a row
        JPanel resultsPanel = new JPanel();
        resultsPanel.setBorder(BorderFactory.createTitledBorder("Results"));
        resultsPanel.setLayout(new BorderLayout(0, 8));
        moviesTable.setPreferredScrollableViewportSize(new Dimension(400, 200));
        resultsPanel.add(moviesTableScrollPane, BorderLayout.CENTER);

        movieDescriptionArea = new JTextArea();
        movieDescriptionArea.setRows(14);
        movieDescriptionArea.setColumns(52);
        movieDescriptionArea.setEditable(false);
        movieDescriptionArea.setLineWrap(true);
        movieDescriptionArea.setWrapStyleWord(true);
        movieDescriptionArea.setOpaque(false);
        movieDescriptionArea.setText("Click a movie in the table to see its description here.");

        JScrollPane descriptionScroll = new JScrollPane(movieDescriptionArea);
        descriptionScroll.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED);
        descriptionScroll.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
        descriptionScroll.setPreferredSize(new Dimension(640, 260));
        descriptionScroll.setMinimumSize(new Dimension(200, 160));

        JPanel descriptionPanel = new JPanel(new BorderLayout());
        descriptionPanel.setBorder(BorderFactory.createTitledBorder("Description"));
        descriptionPanel.add(descriptionScroll, BorderLayout.CENTER);

        // Buttons for showtimes will appear here after selecting a movie.
        showtimesPanel = new JPanel(new FlowLayout(FlowLayout.LEFT, 8, 0));
        descriptionPanel.add(showtimesPanel, BorderLayout.SOUTH);

        resultsPanel.add(descriptionPanel, BorderLayout.SOUTH);

        showSelectedMovieDetails();

        // Panel for search form and results
        JPanel centerPanel = new JPanel();
        centerPanel.setLayout(new BorderLayout(0, 12));
        centerPanel.add(searchFormPanel, BorderLayout.NORTH);
        centerPanel.add(resultsPanel, BorderLayout.CENTER);

        // Button to return to the theater lobby
        JButton returnToTheaterLobbyButton = new JButton("Return to Theater Lobby");
        returnToTheaterLobbyButton.addActionListener(e -> {
            frame.showCard(TheaterFrame.CARD_THEATER_LOBBY);
        });
        returnToTheaterLobbyButton.setHorizontalAlignment(SwingConstants.CENTER);

        this.add(searchMoviesTitle, BorderLayout.NORTH);
        this.add(centerPanel, BorderLayout.CENTER);
        this.add(returnToTheaterLobbyButton, BorderLayout.SOUTH);
    }

    private JTable buildMoviesTable(String[][] movies) {
        String[] columnNames = { "Movie", "Location", "Rated", "Release Year", "Runtime", "Movie Description", "Showtimes" };
        
        // Make it so that the table is not editable
        DefaultTableModel model = new DefaultTableModel(movies, columnNames) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };

        // Set the size of the table
        JTable table = new JTable(model);
        table.setPreferredScrollableViewportSize(new Dimension(400, 200));
        table.setFillsViewportHeight(false);

        // Only show the first 3 columns for the table view
        for (int i = columnNames.length - 1; i >= 3; i--) {
            table.removeColumn(table.getColumnModel().getColumn(i));
        }

        return table;
    }

    private void showSelectedMovieDetails() {
        moviesTable.getSelectionModel().addListSelectionListener(e -> {
            if (e.getValueIsAdjusting()) {
                return;
            }
            int viewRow = moviesTable.getSelectedRow();
            if (viewRow < 0) {
                movieDescriptionArea.setText("Click a movie in the table to see its description here.");
                return;
            }
            // getValueAt on JTable uses *view* column indexes — after hiding columns,
            // only 0–2 exist. Read from the TableModel instead.
            int modelRow = moviesTable.convertRowIndexToModel(viewRow);
            TableModel model = moviesTable.getModel();

            Object name = model.getValueAt(modelRow, MOVIE_NAME_MODEL_COLUMN);
            Object location = model.getValueAt(modelRow, LOCATION_MODEL_COLUMN);
            Object year = model.getValueAt(modelRow, RELEASE_YEAR_MODEL_COLUMN);
            Object rated = model.getValueAt(modelRow, RATED_MODEL_COLUMN);
            Object runtime = model.getValueAt(modelRow, RUNTIME_MODEL_COLUMN);
            Object desc = model.getValueAt(modelRow, DESCRIPTION_MODEL_COLUMN);
            Object showtimes = model.getValueAt(modelRow, SHOWTIMES_MODEL_COLUMN);

            String descriptionText = ""
                    + "Movie Name: " + (name != null ? name.toString() : "") + "\n"
                    + "Release Year: " + (year != null ? year.toString() : "") + "\n"
                    + "Rated: " + (rated != null ? rated.toString() : "") + "\n"
                    + "Runtime: " + (runtime != null ? runtime.toString() : "") + "\n"
                    + "Movie Description: " + (desc != null ? desc.toString() : "");

            movieDescriptionArea.setText(descriptionText);
            movieDescriptionArea.setCaretPosition(0);

            // Populate showtime buttons (no functionality yet).
            showtimesPanel.removeAll();
            JLabel showtimesLabel = new JLabel( location.toString() + " Theatre Showtimes:");
            showtimesPanel.add(showtimesLabel);
            if (showtimes != null) {
                String[] times = getShowtimes(showtimes.toString());
                for (String time : times) {
                    JButton timeButton = new JButton(time);
                    // TODO: Add functionality to the showtime buttons to go to seat chart
                    showtimesPanel.add(timeButton);
                }
            }
            showtimesPanel.revalidate();
            showtimesPanel.repaint();
        });
    }

    private String[] getShowtimes(String showtimes) {
        return showtimes.split(", ");
    }

    private void updateMoviesTable(String[][] rows) {
        moviesTable = buildMoviesTable(rows);
        moviesTableScrollPane.setViewportView(moviesTable);
        showSelectedMovieDetails();
    }

    @Override
    public void refreshCache() {

    }
}
