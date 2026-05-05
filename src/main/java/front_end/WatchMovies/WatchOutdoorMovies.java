package front_end.WatchMovies;

import backend.Theater.TheaterType;
import backend.TheaterSchedule.Movie;
import front_end.IRefreshable;
import front_end.TheaterFrame;

import javax.swing.*;
import java.awt.*;
import java.io.File;
import java.time.LocalTime;

import javafx.application.Platform;
import javafx.embed.swing.JFXPanel;
import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.scene.layout.StackPane;
import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;
import javafx.scene.media.MediaView;

public class WatchOutdoorMovies extends JPanel implements IRefreshable {
    private TheaterFrame frame;
    private JLabel titleMessage;
    private JLabel noneMessage;
    private JLabel movieTitleLabel;
    private JFXPanel fxPanel;
    private String videoPath;
    private boolean play;
    private MediaPlayer player;
    private boolean initialized;
    private static final String[] MOVIE_PATHS = { "Interstellar", "Michael", "Project-Hail-Mary", "South-Park", "Pacific Rim", "Pacific-Rim-Uprising"};

    public WatchOutdoorMovies(TheaterFrame frame) {
        super();
        this.frame = frame;
        // Set the layout of the panel
        BorderLayout layout = new BorderLayout();
        this.setLayout(layout);

        // Title
        titleMessage = new JLabel();
        titleMessage.setHorizontalAlignment(SwingConstants.CENTER);
        titleMessage.setFont(titleMessage.getFont().deriveFont(18f));
        titleMessage.setText("Outdoor Movie Theater");
        this.add(titleMessage, BorderLayout.NORTH);

        // None message when not playing
        movieTitleLabel = new JLabel();
        movieTitleLabel.setHorizontalAlignment(SwingConstants.CENTER);
        movieTitleLabel.setFont(movieTitleLabel.getFont().deriveFont(16f));
        this.add(movieTitleLabel, BorderLayout.NORTH);

        //refreshCache();

        fxPanel = new JFXPanel();
        this.add(fxPanel, BorderLayout.CENTER);
        initOnce();

        // exit theater
        JButton exitTheaterButton = new JButton();
        exitTheaterButton.setText("Return to Theater Lobby");
        exitTheaterButton.addActionListener(e -> {
            pauseVideo();
            frame.showCard(TheaterFrame.CARD_WATCH_MOVIES);
        });
        this.add(exitTheaterButton, BorderLayout.SOUTH);

    }


    // play video every time panel opened which is when it is refreshed
    @Override
    public void refreshCache() {
        pauseVideo();

        LocalTime now = LocalTime.now();
        Movie movie = frame.getManager().getCurrentMovie(TheaterType.OUTDOOR, now);

        if (movie == null) {
            showNoneMessage();
            return;
        }

        File file = new File("out/videos/" + movie.getPath());

        if (!file.exists()) {
            System.out.println("Missing file: " + file.getAbsolutePath());
            showNoneMessage();
            return;
        }

        String url = file.toURI().toString();
        playVideo(url, movie.getName());
    }

    private void playVideo(String url, String title) {
        if (url == null) return;

        Platform.runLater(() -> {

            try {
                // SAME VIDEO → just resume
                if (player != null && url.equals(videoPath)) {
                    player.play();
                    return;
                }

                // DIFFERENT VIDEO → replace
                if (player != null) {
                    player.stop();
                    player.dispose();
                }

                videoPath = url;

                Media media = new Media(url);
                player = new MediaPlayer(media);

                MediaView view = new MediaView(player);
                view.setPreserveRatio(true);

                StackPane root = new StackPane(view);
                Scene scene = new Scene(root);

                view.fitWidthProperty().bind(scene.widthProperty());
                view.fitHeightProperty().bind(scene.heightProperty());

                fxPanel.setScene(scene);

                player.play();

            } catch (Exception e) {
                e.printStackTrace();
            }
        });

        updateUI(title, true);
    }
    private void pauseVideo() {
        if (player != null) {
            Platform.runLater(() -> player.pause());
        }
    }
    private void disposePlayer() {
        if (player != null) {
            Platform.runLater(() -> {
                player.dispose();
            });
            player = null;
        }
    }
    private void initOnce() {
        if (initialized) return;
        initialized = true;

        Platform.runLater(() -> {
            StackPane root = new StackPane();
            Scene scene = new Scene(root);

            fxPanel.setScene(scene);
        });
    }
    private void updateUI(String title, boolean playing) {
        SwingUtilities.invokeLater(() -> {
            if (playing) {
                movieTitleLabel.setText("Now Playing: " + title);
                if (noneMessage != null) remove(noneMessage);
            } else {
                movieTitleLabel.setText("");
            }
            revalidate();
            repaint();
        });
    }
    private void showNoneMessage() {
        SwingUtilities.invokeLater(() -> {
            if (player != null) {
                player.pause();
            }

            movieTitleLabel.setText("");
            this.remove(noneMessage);
            this.add(noneMessage, BorderLayout.CENTER);

            revalidate();
            repaint();
        });
    }

}
