package front_end.TicketBooking;

import javax.swing.JPanel;
import javax.swing.JLabel;
import javax.swing.SwingConstants;
import javax.swing.JTable;
import javax.swing.table.DefaultTableModel;
import java.awt.GridBagLayout;
import java.awt.Dimension;
import java.awt.BorderLayout;

import front_end.TheaterFrame;

public class SeatChart extends JPanel {
    private TheaterFrame frame;

    public SeatChart(TheaterFrame frame, String[] movie) {
        super();
        this.frame = frame;

        // Set the layout of the panel
        GridBagLayout layout = new GridBagLayout();
        this.setLayout(layout);

        // Create the seat chart title label
        JLabel seatChartTitle = new JLabel(movie[0] + " - " + movie[1]);
        seatChartTitle.setHorizontalAlignment(SwingConstants.CENTER);
        seatChartTitle.setFont(seatChartTitle.getFont().deriveFont(18f));

        // Create the seat chart
        JTable seatChartTable = new JTable(new DefaultTableModel(new String[][] { { "1", "2", "3", "4", "5" }, { "1", "2", "3", "4", "5" }, { "1", "2", "3", "4", "5" }, { "1", "2", "3", "4", "5" }, { "1", "2", "3", "4", "5" } }, new String[] { "Row", "Column", "Seat" }));
        seatChartTable.setPreferredScrollableViewportSize(new Dimension(400, 200));
        seatChartTable.setFillsViewportHeight(false);

        // Add the seat chart to the panel
        this.add(seatChartTable, BorderLayout.CENTER);
    }

}
