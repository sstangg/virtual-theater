package front_end.TheaterTypes;

import java.awt.BorderLayout;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;

import javax.imageio.ImageIO;
import javax.swing.BorderFactory;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.SwingConstants;

import backend.Theater.TheaterType;
import backend.TheaterSchedule.Movie;
import front_end.TheaterFrame;

/*
 * OutdoorTheaterPage shows available Outdoor movies as posters.
 * It keeps the UI simple: posters in the middle + a back button.
 */
public class OutdoorTheaterPage extends JPanel {

    private final TheaterFrame frame;
    private final JPanel postersPanel;

    public OutdoorTheaterPage(TheaterFrame frame) {
        super(new BorderLayout(0, 12));
        this.frame = frame;
        this.setBorder(BorderFactory.createEmptyBorder(16, 16, 16, 16));

        JLabel title = new JLabel("Outdoor Theater");
        title.setHorizontalAlignment(SwingConstants.CENTER);
        title.setFont(title.getFont().deriveFont(Font.BOLD, 18f));
        this.add(title, BorderLayout.NORTH);

        postersPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 12, 12));
        postersPanel.setOpaque(false);
        this.add(postersPanel, BorderLayout.CENTER);

        JPanel backHolder = new JPanel(new FlowLayout(FlowLayout.CENTER));
        JButton back = new JButton("Back");
        back.addActionListener(e -> frame.showCard(TheaterFrame.CARD_CHOOSE_THEATER_TYPE));
        backHolder.add(back);
        this.add(backHolder, BorderLayout.SOUTH);

        loadPosters();
    }

    private void loadPosters() {
        ArrayList<Movie> movies = frame.getManager().getMovies();
        for (int i = 0; i < movies.size(); i++) {
            Movie m = movies.get(i);
            if (m.getTheaterType() != TheaterType.OUTDOOR) {
                continue;
            }
            postersPanel.add(buildPosterLabel(m.getName()));
        }

        if (postersPanel.getComponentCount() == 0) {
            postersPanel.add(new JLabel("No Outdoor movies found."));
        }
    }

    private JLabel buildPosterLabel(String movieName) {
        String posterFile = guessPosterFileName(movieName);
        ImageIcon icon = loadPosterIconAbsolute(posterFile);

        if (icon == null) {
            JLabel missing = new JLabel("Poster not found: " + posterFile);
            missing.setHorizontalAlignment(SwingConstants.CENTER);
            return missing;
        }

        JLabel label = new JLabel(icon);
        label.setHorizontalAlignment(SwingConstants.CENTER);
        return label;
    }

    private static String guessPosterFileName(String movieName) {
        String compact = movieName.replaceAll("[^A-Za-z0-9]", "");
        if (compact.length() == 0) {
            compact = "Unknown";
        }
        return compact + ".png";
    }

    // Intentionally mirrors TheaterBackground.java (87-97), but uses an absolute file path.
    private static ImageIcon loadPosterIconAbsolute(String fileName) {
        String absolutePath = new File("src/main/java/front_end/Images/" + fileName).getAbsolutePath();
        InputStream input;
        try {
            input = new FileInputStream(absolutePath);
        } catch (IOException e) {
            return null;
        }
        try {
            BufferedImage img = ImageIO.read(input);
            if (img == null) {
                return null;
            }
            return new ImageIcon(img);
        } catch (IOException e) {
            return null;
        }
    }
}

