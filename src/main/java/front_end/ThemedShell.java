package front_end;

import java.awt.AlphaComposite;
import java.awt.BorderLayout;
import java.awt.CardLayout;
import java.awt.Color;
import java.awt.Font;
import java.awt.GradientPaint;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.RenderingHints;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.io.InputStream;

import javax.imageio.ImageIO;
import javax.swing.BorderFactory;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;

/**
 * Decorative wrapper around the whole app window.
 * <p>
 * It draws the curtain picture in the back, adds a title bar on top, and leaves a
 * padded area in the middle for your {@link CardLayout} screens. The picture is loaded
 * once from {@code Images/TheaterCurtains.png} next to this class on the classpath.
 */
public final class ThemedShell extends JPanel {

    // --- Colors used by the theme (other classes can reuse these if they want) ---
    public static final Color BG_TOP = new Color(28, 30, 52);
    public static final Color BG_BOTTOM = new Color(14, 16, 32);
    public static final Color HEADER_BG = new Color(18, 20, 38);
    public static final Color ACCENT = new Color(218, 180, 72);
    public static final Color CARD_SURFACE = new Color(252, 250, 246);
    public static final Color MUTED_TEXT = new Color(220, 220, 235);

    /** How strong the “frosted” layer is over the curtain inside the card area (0 = none, 1 = solid). */
    public static final float CARD_SCRIM_ALPHA = 0.50f;

    private static final float HEADER_SCRIM_ALPHA = 0.50f;
    private static final float DARK_VEIL_OVER_CURTAIN = 0.20f;

    private static final String CURTAIN_FILE = "/images/TheaterCurtains.png";

    /** Null if the file was missing or could not be read; we then paint a simple gradient instead. */
    private final BufferedImage curtainPicture;

    public ThemedShell(JPanel cardStack) {
        super(new BorderLayout());

        // false = we paint the whole background ourselves in paintComponent(...)
        setOpaque(false);

        curtainPicture = readCurtainPicture();

        JPanel header = buildHeader();
        JPanel paddedCenter = buildPaddedCenter(cardStack);

        add(header, BorderLayout.NORTH);
        add(paddedCenter, BorderLayout.CENTER);
    }

    /**
     * Builds the panel that holds all your screens ({@link CardLayout}).
     * It paints a soft cream layer so text stays readable while the curtain still peeks through.
     */
    public static JPanel createCardsPanel(CardLayout cardLayout) {
        return new FrostedCardsPanel(cardLayout);
    }

    private JPanel buildHeader() {
        JLabel title = new JLabel("Virtual Theater");
        title.setFont(title.getFont().deriveFont(Font.BOLD, 22f));
        title.setForeground(ACCENT);
        title.setBorder(new EmptyBorder(14, 22, 14, 8));

        JLabel tagline = new JLabel("Book seats, snacks, and the show");
        tagline.setForeground(MUTED_TEXT);
        tagline.setBorder(new EmptyBorder(0, 0, 0, 22));

        JPanel header = new HeaderBarPanel();
        header.setBorder(BorderFactory.createMatteBorder(0, 0, 1, 0, new Color(60, 55, 90)));
        header.add(title, BorderLayout.WEST);
        header.add(tagline, BorderLayout.EAST);
        return header;
    }

    private static JPanel buildPaddedCenter(JPanel cardStack) {
        cardStack.setOpaque(false);
        cardStack.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(new Color(48, 52, 82), 1, true),
                new EmptyBorder(12, 12, 12, 12)));

        JPanel center = new JPanel(new BorderLayout());
        center.setOpaque(false);
        center.setBorder(new EmptyBorder(18, 18, 22, 18));
        center.add(cardStack, BorderLayout.CENTER);
        return center;
    }

    private static BufferedImage readCurtainPicture() {
        try (InputStream input = ThemedShell.class.getResourceAsStream(CURTAIN_FILE)) {
            if (input == null) {
                System.err.println("ThemedShell: could not find " + CURTAIN_FILE + " on the classpath.");
                return null;
            }
            return ImageIO.read(input);
        } catch (IOException e) {
            System.err.println("ThemedShell: error reading curtain image: " + e.getMessage());
            return null;
        }
    }

    @Override
    protected void paintComponent(Graphics graphics) {
        super.paintComponent(graphics);

        Graphics2D g = (Graphics2D) graphics.create();
        int width = getWidth();
        int height = getHeight();

        if (curtainPicture != null) {
            paintCurtainScaledToFillWindow(g, curtainPicture, width, height);
            paintSemiTransparentOverlay(g, width, height, Color.BLACK, DARK_VEIL_OVER_CURTAIN);
        } else {
            paintGradientFallback(g, width, height);
        }

        g.dispose();
    }

    /** Scales the image so the whole window is covered (extra is cropped). Center stays centered. */
    private static void paintCurtainScaledToFillWindow(Graphics2D g, BufferedImage image, int windowWidth, int windowHeight) {
        g.setRenderingHint(RenderingHints.KEY_RENDERING, RenderingHints.VALUE_RENDER_QUALITY);
        g.setRenderingHint(RenderingHints.KEY_INTERPOLATION, RenderingHints.VALUE_INTERPOLATION_NEAREST_NEIGHBOR);

        int imageWidth = image.getWidth();
        int imageHeight = image.getHeight();
        if (imageWidth <= 0 || imageHeight <= 0) {
            return;
        }

        double scaleX = (double) windowWidth / imageWidth;
        double scaleY = (double) windowHeight / imageHeight;
        double scale = Math.max(scaleX, scaleY);

        int drawWidth = (int) Math.round(imageWidth * scale);
        int drawHeight = (int) Math.round(imageHeight * scale);
        int left = (windowWidth - drawWidth) / 2;
        int top = (windowHeight - drawHeight) / 2;

        g.drawImage(image, left, top, drawWidth, drawHeight, null);
    }

    private static void paintGradientFallback(Graphics2D g, int width, int height) {
        GradientPaint gradient = new GradientPaint(0, 0, BG_TOP, 0, height, BG_BOTTOM);
        g.setPaint(gradient);
        g.fillRect(0, 0, width, height);
    }

    /** One flat color with an alpha value, drawn on top of whatever is already there. */
    private static void paintSemiTransparentOverlay(Graphics2D g, int width, int height, Color color, float alpha) {
        g.setComposite(AlphaComposite.SrcOver.derive(alpha));
        g.setColor(color);
        g.fillRect(0, 0, width, height);
        g.setComposite(AlphaComposite.SrcOver);
    }

    /** Top strip: lets the parent's curtain show through a little under a dark tint. */
    private static final class HeaderBarPanel extends JPanel {
        HeaderBarPanel() {
            super(new BorderLayout());
            setOpaque(false);
        }

        @Override
        protected void paintComponent(Graphics graphics) {
            Graphics2D g = (Graphics2D) graphics.create();
            paintSemiTransparentOverlay(g, getWidth(), getHeight(), HEADER_BG, HEADER_SCRIM_ALPHA);
            g.dispose();
            super.paintComponent(graphics);
        }
    }

    /** The stack of screens from {@link TheaterFrame}: frosted rectangle, then Swing paints the cards. */
    private static final class FrostedCardsPanel extends JPanel {
        FrostedCardsPanel(CardLayout cardLayout) {
            super(cardLayout);
            setOpaque(false);
        }

        @Override
        protected void paintComponent(Graphics graphics) {
            Graphics2D g = (Graphics2D) graphics.create();
            paintSemiTransparentOverlay(g, getWidth(), getHeight(), CARD_SURFACE, CARD_SCRIM_ALPHA);
            g.dispose();
            super.paintComponent(graphics);
        }
    }
}
