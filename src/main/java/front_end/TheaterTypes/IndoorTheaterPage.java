package front_end.TheaterTypes;

import java.awt.BorderLayout;
import java.awt.FlowLayout;
import java.awt.Font;
import java.io.IOException;
import java.io.InputStream;
import java.io.FileInputStream;

import javax.imageio.ImageIO;
import javax.swing.BorderFactory;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.SwingConstants;

import front_end.TheaterFrame;

public class IndoorTheaterPage extends JPanel {
    private final TheaterFrame frame;
    private final JPanel postersPanel;

    // Absolute path to the poster file (keeps loading simple + reliable).
    private static final String POSTER_FILE = "src/main/java/front_end/Images/SouthPark.png";

    public IndoorTheaterPage(TheaterFrame frame) {
        super(new BorderLayout(0, 12));
        this.frame = frame;
        this.setBorder(BorderFactory.createEmptyBorder(16, 16, 16, 16));

        // Add the title to the panel
        JLabel title = new JLabel("Drive-In Theater");
        title.setHorizontalAlignment(SwingConstants.CENTER);
        title.setFont(title.getFont().deriveFont(Font.BOLD, 18f));
        this.add(title, BorderLayout.NORTH);

        // Add the posters panel that holds the poster
        postersPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 12, 12));
        postersPanel.setOpaque(false);
        this.add(postersPanel, BorderLayout.CENTER);

        // Add the back button to the panel
        JPanel backHolder = new JPanel(new FlowLayout(FlowLayout.CENTER));
        JButton back = new JButton("Back");
        back.addActionListener(e -> frame.showCard(TheaterFrame.CARD_CHOOSE_THEATER_TYPE));
        backHolder.add(back);
        this.add(backHolder, BorderLayout.SOUTH);

        // Add the poster to the panel
        postersPanel.add(buildPosterLabel());
    }

    // Build the label with the poster
    private JLabel buildPosterLabel() {
        ImageIcon icon = loadPoster();
        JLabel label = new JLabel(icon);
        label.setHorizontalAlignment(SwingConstants.CENTER);
        return label;
    }

    // Load movie poster from file
    private static ImageIcon loadPoster() {
        InputStream input = null;
        try {
            input = new FileInputStream(POSTER_FILE);
            return new ImageIcon(ImageIO.read(input));
        } catch (IOException e) {
            return null;
        }
    }
}
