package front_end.TicketBooking;

import backend.FoodService.Food;
import front_end.BookingDraft;
import front_end.TheaterFrame;
import front_end.testManager;

import javax.swing.BorderFactory;
import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.SwingConstants;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Font;
import java.util.ArrayList;

import javax.swing.border.TitledBorder;

/**
 * After seat selection: choose food quantities (delivery = packaged, concession = non-packaged
 * for indoor/drive-in only). Outdoor shows delivery only.
 */
public class FoodSelectionPanel extends JPanel {

    /** Minimum width for the food menu column (sections + rows). */
    private static final int FOOD_MENU_WIDTH = 720;

    private final TheaterFrame frame;

    private final ArrayList<Food> deliveryFoods;
    private final int[] deliveryQuantities;
    private final JLabel[] deliveryCountLabels;

    private final ArrayList<Food> concessionFoods;
    private final int[] concessionQuantities;
    private final JLabel[] concessionCountLabels;

    private final JPanel concessionOuterPanel;

    // Booking the user is currently working on. Set by beginOrder when entering the card.
    private BookingDraft currentDraft;

    private JLabel headerLabel;

    public FoodSelectionPanel(TheaterFrame parentFrame, testManager manager) {
        super();
        this.frame = parentFrame;

        this.deliveryFoods = manager.getDeliveryFoods();
        this.deliveryQuantities = new int[deliveryFoods.size()];
        this.deliveryCountLabels = new JLabel[deliveryFoods.size()];

        this.concessionFoods = manager.getConcessionFoods();
        this.concessionQuantities = new int[concessionFoods.size()];
        this.concessionCountLabels = new JLabel[concessionFoods.size()];

        setBorder(BorderFactory.createEmptyBorder(16, 16, 16, 16));
        setLayout(new BorderLayout(0, 12));

        headerLabel = new JLabel(" ");
        headerLabel.setHorizontalAlignment(SwingConstants.CENTER);
        headerLabel.setFont(headerLabel.getFont().deriveFont(18f));
        add(headerLabel, BorderLayout.NORTH);

        JPanel scrollInner = new JPanel();
        scrollInner.setLayout(new BoxLayout(scrollInner, BoxLayout.Y_AXIS));

        JPanel deliverySection = new JPanel();
        deliverySection.setLayout(new BorderLayout());
        deliverySection.setBorder(createSectionBorder("Food Delivery"));
        JPanel deliveryRows = new JPanel();
        deliveryRows.setLayout(new BoxLayout(deliveryRows, BoxLayout.Y_AXIS));
        for (int i = 0; i < deliveryFoods.size(); i++) {
            // Build the quantity row for the items in the delivery stand
            deliveryRows.add(buildQuantityRow(deliveryFoods.get(i), i, true));
        }
        deliverySection.add(deliveryRows, BorderLayout.CENTER);
        scrollInner.add(deliverySection);

        concessionOuterPanel = new JPanel();
        concessionOuterPanel.setLayout(new BorderLayout());
        concessionOuterPanel.setBorder(createSectionBorder("Concession Stand"));
        JPanel concessionRows = new JPanel();
        concessionRows.setLayout(new BoxLayout(concessionRows, BoxLayout.Y_AXIS));
        for (int i = 0; i < concessionFoods.size(); i++) {
            // Build the quantity row for the items in the concession stand
            concessionRows.add(buildQuantityRow(concessionFoods.get(i), i, false));
        }
        concessionOuterPanel.add(concessionRows, BorderLayout.CENTER);
        scrollInner.add(concessionOuterPanel);

        // Wide enough to read comfortably; not stretched to full frame width.
        scrollInner.validate();
        Dimension innerPref = scrollInner.getPreferredSize();
        int menuWidth = FOOD_MENU_WIDTH;
        if (innerPref.width > menuWidth) {
            menuWidth = innerPref.width;
        }
        int menuHeight = innerPref.height;
        if (menuHeight < 1) {
            menuHeight = 200;
        }
        scrollInner.setPreferredSize(new Dimension(menuWidth, menuHeight));

        JPanel scrollInnerHolder = new JPanel(new FlowLayout(FlowLayout.CENTER, 0, 0));
        scrollInnerHolder.add(scrollInner);

        JPanel scrollBody = new JPanel(new BorderLayout());
        scrollBody.add(scrollInnerHolder, BorderLayout.NORTH);

        JScrollPane scrollPane = new JScrollPane(scrollBody);
        scrollPane.setPreferredSize(new Dimension(menuWidth + 40, 420));
        scrollPane.getVerticalScrollBar().setUnitIncrement(16);
        add(scrollPane, BorderLayout.CENTER);

        JPanel buttonRow = new JPanel(new FlowLayout(FlowLayout.RIGHT, 8, 0));
        JButton backButton = new JButton("Back");
        backButton.addActionListener(e -> {
            // Only go back to the seat chart if the location is indoor, else go back to the search movies panel
            if (currentDraft.isSeated()) {
                this.frame.openSeatChart();
            } else {
                this.frame.showCard(TheaterFrame.CARD_SEARCH_MOVIES);
            }
        });
        JButton skipButton = new JButton("Skip");
        skipButton.addActionListener(e -> {
            // Pass with an empty list of chosen foods
            currentDraft.setChosenFoods(new ArrayList<Food>());
            this.frame.openConfirmationPage();
        });
        JButton nextButton = new JButton("Next");
        nextButton.addActionListener(e -> {
            // Set the chosen foods for the booking draft
            currentDraft.setChosenFoods(buildOrderedFoodList());
            this.frame.openConfirmationPage();
        });
        buttonRow.add(backButton);
        buttonRow.add(skipButton);
        buttonRow.add(nextButton);

        JPanel southWrapper = new JPanel(new BorderLayout());
        southWrapper.add(buttonRow, BorderLayout.EAST);
        add(southWrapper, BorderLayout.SOUTH);
    }

    // Create the border for the section with larger font
    private TitledBorder createSectionBorder(String title) {
        TitledBorder border = BorderFactory.createTitledBorder(title);
        Font base = border.getTitleFont();
        border.setTitleFont(base.deriveFont(Font.BOLD, 16f));
        return border;
    }

    // Format the money to 2 decimal places
    private String formatMoney(double price) {
        return String.format("%.2f", price);
    }

    // Build the quantity row for the delivery or concession stand
    private JPanel buildQuantityRow(Food food, int index, boolean isDelivery) {
        JPanel row = new JPanel(new BorderLayout(16, 0));
        row.setBorder(BorderFactory.createEmptyBorder(4, 8, 4, 8));

        String displayName = food.getName().replace('_', ' ');
        String nameWithPrice = displayName + "  ($" + formatMoney(food.getPrice()) + ")";
        JLabel nameLabel = new JLabel(nameWithPrice);
        nameLabel.setHorizontalAlignment(SwingConstants.LEFT);
        row.add(nameLabel, BorderLayout.WEST);

        JPanel controls = new JPanel(new FlowLayout(FlowLayout.LEFT, 8, 0));
        JButton minus = new JButton("-");
        minus.setPreferredSize(new Dimension(40, 28));
        JLabel countLabel = new JLabel("0");
        countLabel.setPreferredSize(new Dimension(32, 28));
        countLabel.setHorizontalAlignment(SwingConstants.CENTER);
        JButton plus = new JButton("+");
        plus.setPreferredSize(new Dimension(40, 28));

        if (isDelivery) {
            deliveryCountLabels[index] = countLabel;
            minus.addActionListener(e -> {
                if (deliveryQuantities[index] > 0) {
                    deliveryQuantities[index]--;
                    countLabel.setText(String.valueOf(deliveryQuantities[index]));
                }
            });
            plus.addActionListener(e -> {
                deliveryQuantities[index]++;
                countLabel.setText(String.valueOf(deliveryQuantities[index]));
            });
        } else {
            concessionCountLabels[index] = countLabel;
            minus.addActionListener(e -> {
                if (concessionQuantities[index] > 0) {
                    concessionQuantities[index]--;
                    countLabel.setText(String.valueOf(concessionQuantities[index]));
                }
            });
            plus.addActionListener(e -> {
                concessionQuantities[index]++;
                countLabel.setText(String.valueOf(concessionQuantities[index]));
            });
        }

        controls.add(minus);
        controls.add(countLabel);
        controls.add(plus);
        row.add(controls, BorderLayout.EAST);
        return row;
    }

    // Set up the food selection panel when it is opened
    public void beginOrder(BookingDraft draft) {
        currentDraft = draft;

        String location = draft.getLocation();
        String movieName = draft.movieName();

        // Indoor + Drive-In get a concession stand; Outdoor only has delivery.
        boolean showConcession = "Indoor".equals(location) || "Drive-In".equals(location);
        concessionOuterPanel.setVisible(showConcession);

        // Reset the delivery and concession quantities
        for (int i = 0; i < deliveryQuantities.length; i++) {
            deliveryQuantities[i] = 0;
            deliveryCountLabels[i].setText("0");
        }
        for (int i = 0; i < concessionQuantities.length; i++) {
            concessionQuantities[i] = 0;
            concessionCountLabels[i].setText("0");
        }

        // Set the header label to the movie name and location
        headerLabel.setText("Add food for " + movieName + " (" + location + ")");
    }

    // Build the ordered food list from the delivery and concession quantities
    private ArrayList<Food> buildOrderedFoodList() {
        ArrayList<Food> ordered = new ArrayList<Food>();
        for (int i = 0; i < deliveryFoods.size(); i++) {
            Food f = deliveryFoods.get(i);
            int n = deliveryQuantities[i];
            for (int k = 0; k < n; k++) {
                ordered.add(f);
            }
        }
        for (int i = 0; i < concessionFoods.size(); i++) {
            Food f = concessionFoods.get(i);
            int n = concessionQuantities[i];
            for (int k = 0; k < n; k++) {
                ordered.add(f);
            }
        }
        return ordered;
    }
}
