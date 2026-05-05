import javax.swing.SwingUtilities;

import front_end.TheaterFrame;
import front_end.DataManagers.testManager;

public class Main {

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            testManager manager = new testManager();
            TheaterFrame frame = new TheaterFrame(manager.getSearchMovieRows(), manager);
            frame.setVisible(true);
        });
    }
}
