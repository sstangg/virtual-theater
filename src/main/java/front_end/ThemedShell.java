package front_end;

import java.awt.BorderLayout;
import java.awt.CardLayout;
import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.io.InputStream;

import javax.imageio.ImageIO;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;

/*
 * ThemedShell is a simple “wrapper” panel for the whole app window.
 *
 * It does 3 simple things (matching your requirements):
 * 1) Retrieve the picture and paint it as the background.
 * 2) Add a solid-color header with text.
 * 3) Paint a low-opacity “outer frame” (a translucent border overlay).
 *
 * Everything is intentionally kept straightforward and heavily commented so it is
 * easy to read and modify.
 */
public final class ThemedShell extends JPanel {

    private static final String CURTAIN_FILE = "Images/TheaterCurtains.png";

    private final BufferedImage backgroundImage;

    private static final Color HEADER_COLOR = new Color(0, 0, 255, 100); // Blue
    private static final Color HEADER_TEXT_COLOR = Color.WHITE;

    private static final Color CARD_AREA_SHEET_COLOR = new Color(255, 255, 255, 150);

    // Reference to the center wrapper panel
    private final JPanel centerWrapper;

    public ThemedShell(JPanel cardStack) {
        super(new BorderLayout());

        // Set the panel to be transparent
        setOpaque(false);

        // Retrieve the curtain picture from the classpath
        backgroundImage = readBackgroundImage();

        // Add the header to the panel
        add(buildHeader(), BorderLayout.NORTH);

        // The cards are added to the center of the panel with padding and made transparent
        cardStack.setOpaque(false);
        centerWrapper = wrapWithSimplePadding(cardStack);
        add(centerWrapper, BorderLayout.CENTER);
    }

    // Create the panel that holds all the cards
    public static JPanel createCardsPanel(CardLayout cardLayout) {
        JPanel p = new JPanel(cardLayout);
        p.setOpaque(false);
        return p;
    }

    // Creates the header panel with the title and the header text
    private JPanel buildHeader() {
        JPanel header = new JPanel(new BorderLayout());
        header.setOpaque(true); 
        header.setBackground(HEADER_COLOR);

        JLabel title = new JLabel("Virtual Theater");
        title.setForeground(HEADER_TEXT_COLOR);
        title.setFont(title.getFont().deriveFont(Font.BOLD, 24f));
        title.setBorder(new EmptyBorder(14, 18, 14, 18));

        header.add(title, BorderLayout.WEST);

        return header;
    }

    // Wrap the card stack with simple padding to leave a space around the cards
    private static JPanel wrapWithSimplePadding(JPanel cardStack) {
        JPanel center = new JPanel(new BorderLayout());
        center.setOpaque(false);
        center.setBorder(new EmptyBorder(20, 20, 20, 20));
        center.add(cardStack, BorderLayout.CENTER);
        return center;
    }

    // Retrieve the curtain picture from the classpath
    private static BufferedImage readBackgroundImage() {
        InputStream input = ThemedShell.class.getResourceAsStream(CURTAIN_FILE);
        if (input == null) {
            return null;
        }
        try {
            return ImageIO.read(input);
        } catch (IOException e) {
            return null;
        }
    }

    // Paint the component to add the background image and additional transparent sheet
    @Override
    protected void paintComponent(Graphics graphics) {
        super.paintComponent(graphics);

        // Create a Graphics2D object to draw the background image
        Graphics2D graphic = (Graphics2D) graphics.create();

        int w = this.getWidth();
        int h = this.getHeight();

        graphic.drawImage(backgroundImage, 0, 0, w, h, null);

        // Get the position and size of the center wrapper
        int x = centerWrapper.getX();
        int y = centerWrapper.getY();
        int centerWidth = centerWrapper.getWidth();
        int centerHeight = centerWrapper.getHeight();

        // Decides how large the transparent sheet should be
        double sheetWidthPercent = 0.95;
        double sheetHeightPercent = 0.95;

        int sheetW = (int) (centerWidth * sheetWidthPercent);
        int sheetH = (int) (centerHeight * sheetHeightPercent);

        // find the center for the smaller transparent sheet
        int sheetX = x + (centerWidth - sheetW) / 2;
        int sheetY = y + (centerHeight - sheetH) / 2;

        // Paint the transparent sheet
        graphic.setColor(CARD_AREA_SHEET_COLOR);
        graphic.fillRect(sheetX, sheetY, sheetW, sheetH);

        graphic.dispose();
    }
}
