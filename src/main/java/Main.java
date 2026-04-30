import javax.swing.SwingUtilities;

import front_end.testManager;
import front_end.TheaterFrame;

public class Main {

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            testManager manager = new testManager();
            TheaterFrame frame = new TheaterFrame(manager.getSearchMovieRows());
            frame.setVisible(true);
        });
    }
}
