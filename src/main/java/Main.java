import javax.swing.SwingUtilities;

import front_end.testManager;
import front_end.TheaterFrame;
import javafx.application.Platform;

public class Main {

    public static void main(String[] args) {
        Platform.startup(() -> {});
        SwingUtilities.invokeLater(() -> {
            testManager manager = new testManager();
            TheaterFrame frame = new TheaterFrame(manager.getSearchMovieRows(), manager);
            frame.setVisible(true);
        });
    }
}
