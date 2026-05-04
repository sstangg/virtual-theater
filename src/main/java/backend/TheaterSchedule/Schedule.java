package backend.TheaterSchedule;

import backend.Theater.TheaterType;

import java.util.ArrayList;
import java.util.Random;

public class Schedule {
    private ArrayList<Showing> showings;

    public Schedule() {
        showings = new ArrayList<>();
    }

    // GETTERS & SETTERS
    public ArrayList<Showing> getShowings() {
        return showings;
    }
    public Showing getShowing(int i) {
        return showings.get(i);
    }
    public void addShowing(Showing s) {
        this.showings.add(s);
    }

    // Builds a schedule from movies based on the theater types
    public static Schedule buildScheduleFromMovies(ArrayList<Movie> movies) {
        Schedule schedule = new Schedule();

        // Put each movie into a list for its theater type
        ArrayList<Movie> indoorList = new ArrayList<>();
        ArrayList<Movie> outdoorList = new ArrayList<>();
        ArrayList<Movie> driveInList = new ArrayList<>();

        // Sort the movies by theater type
        for (int i = 0; i < movies.size(); i++) {
            Movie m = movies.get(i);
            TheaterType type = m.getTheaterType();
            if (type == TheaterType.INDOOR) {
                indoorList.add(m);
            } else if (type == TheaterType.OUTDOOR) {
                outdoorList.add(m);
            } else if (type == TheaterType.DRIVEIN) {
                driveInList.add(m);
            }
        }

        // for indoor movies, we shuffle them randomly and then add them to the schedule for variety
        shuffleMovies(indoorList);
        for (int slot = 0; slot < 4; slot++) {
            if (slot < indoorList.size()) {
                Movie movieForSlot = indoorList.get(slot);
                schedule.addShowing(ShowingFactory.createIndoorSingleForSlot(movieForSlot, slot));
            } else {
                break;
            }
        }

        // for outdoor movies, we ensure that there are 2 movies for the double feature
        if (outdoorList.size() != 2) {
            throw new IllegalArgumentException("Expected exactly 2 OUTDOOR movies, got " + outdoorList.size());
        }
        Movie outdoorFirst;
        Movie outdoorSecond;
        if (outdoorList.get(0).getMovieId() <= outdoorList.get(1).getMovieId()) {
            outdoorFirst = outdoorList.get(0);
            outdoorSecond = outdoorList.get(1);
        } else {
            outdoorFirst = outdoorList.get(1);
            outdoorSecond = outdoorList.get(0);
        }
        schedule.addShowing(ShowingFactory.createOutdoorOrDriveInDoubleForSlot(outdoorFirst, outdoorSecond, 0));
        schedule.addShowing(ShowingFactory.createOutdoorOrDriveInDoubleForSlot(outdoorFirst, outdoorSecond, 1));

        // for drive-in movies, we ensure that there are 2 movies for the double feature
        if (driveInList.size() != 2) {
            throw new IllegalArgumentException("Expected exactly 2 DRIVEIN movies, got " + driveInList.size());
        }
        Movie driveFirst;
        Movie driveSecond;
        if (driveInList.get(0).getMovieId() <= driveInList.get(1).getMovieId()) {
            driveFirst = driveInList.get(0);
            driveSecond = driveInList.get(1);
        } else {
            driveFirst = driveInList.get(1);
            driveSecond = driveInList.get(0);
        }
        schedule.addShowing(ShowingFactory.createOutdoorOrDriveInDoubleForSlot(driveFirst, driveSecond, 0));
        schedule.addShowing(ShowingFactory.createOutdoorOrDriveInDoubleForSlot(driveFirst, driveSecond, 1));

        return schedule;
    }

    // Shuffle the movies randomly
    private static void shuffleMovies(ArrayList<Movie> movies) {
        Random random = new Random();
        for (int i = movies.size() - 1; i > 0; i--) {
            int j = random.nextInt(i + 1);
            Movie temp = movies.get(i);
            movies.set(i, movies.get(j));
            movies.set(j, temp);
        }
    }
}
