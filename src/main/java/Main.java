import javax.swing.SwingUtilities;

import front_end.TheaterFrame;

public class Main {

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            TheaterFrame frame = new TheaterFrame();
            frame.setVisible(true);
        });
    }
}
