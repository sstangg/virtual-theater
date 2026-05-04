package front_end;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;

import backend.TheaterSchedule.Movie;
import backend.TheaterSchedule.Schedule;
import backend.TheaterSchedule.Showing;
import backend.TheaterSchedule.DoubleFeature;
import backend.Theater.TheaterType;

public class testManager {
    private static final String MOVIES_FILE_PATH = "src/main/java/database/movies.txt";

    private final ArrayList<Movie> movies;
    private final Schedule schedule;
    private final String[][] searchMovieRows;

    public testManager() {
        this.movies = new ArrayList<>();
        loadMoviesFromTxt(MOVIES_FILE_PATH);
        // build schedule based on the movies in database
        this.schedule = Schedule.buildScheduleFromMovies(this.movies);
        // from schedule and movies, build the search movie rows for the search movie UI
        this.searchMovieRows = buildSearchMovieRows(this.movies, this.schedule);
    }

    public ArrayList<Movie> getMovies() {
        return this.movies;
    }

    public Schedule getSchedule() {
        return this.schedule;
    }

    public String[][] getSearchMovieRows() {
        return this.searchMovieRows;
    }

    private String[][] buildSearchMovieRows(ArrayList<Movie> movies, Schedule schedule) {
        String[][] rows = new String[movies.size()][7];

        // Create search row for each movie
        for (int i = 0; i < movies.size(); i++) {
            Movie m = movies.get(i);

            // Collect showtimes for this movie by scanning all showings.
            ArrayList<String> times = new ArrayList<String>();
            String partnerName = null;

            // Get all showings from the schedule
            ArrayList<Showing> showings = schedule.getShowings();
            for (int j = 0; j < showings.size(); j++) {
                Showing sh = showings.get(j);

                // If the showing is a double feature, get the movie ids and add the showtimes to the times list
                if (sh instanceof DoubleFeature) {
                    DoubleFeature df = (DoubleFeature) sh;
                    int[] ids = df.getMovieIds();
                    int id1 = ids[0];
                    int id2 = ids[1];

                    if (m.getMovieId() == id1 || m.getMovieId() == id2) {
                        times.add(formatTime(sh.getStartTime()));

                        int otherId;
                        if (m.getMovieId() == id1) {
                            otherId = id2;
                        } else {
                            otherId = id1;
                        }

                        // Find partner name to add to the description
                        for (int k = 0; k < movies.size(); k++) {
                            if (movies.get(k).getMovieId() == otherId) {
                                partnerName = movies.get(k).getName();
                                break;
                            }
                        }
                    }
                } else {
                    // If the showing is a single feature, add the showtime to the times list
                    if (sh.getMovieId() == m.getMovieId()) {
                        times.add(formatTime(sh.getStartTime()));
                    }
                }
            }

            String description = m.getDescription();
            // If the movie is a double feature, add the partner name to the description
            if (partnerName != null) {
                description = description + "\n\nNote: Double feature with " + partnerName + ". Same showtimes.";
            }

            // create the formatted row for the movie
            rows[i] = new String[] {
                    m.getName(),
                    locationLabel(m.getTheaterType()),
                    m.getRated(),
                    String.valueOf(m.getReleaseYear()),
                    m.getRuntime() + " mins",
                    description,
                    joinTimes(times)
            };
        }

        return rows;
    }

    // Join the times into a string separated by commas
    private String joinTimes(ArrayList<String> times) { 
        String result = "";
        for (int i = 0; i < times.size(); i++) {
            if (i > 0) {
                result = result + ", ";
            }
            result = result + times.get(i);
        }
        return result;
    }

    private String locationLabel(TheaterType type) {
        if (type == null) {
            return "";
        }
        if (type == TheaterType.INDOOR) {
            return "Indoor";
        } else if (type == TheaterType.OUTDOOR) {
            return "Outdoor";
        } else if (type == TheaterType.DRIVEIN) {
            return "Drive-In";
        }
        return "";
    }

    private String formatTime(java.sql.Time t) {
        // "HH:mm" without using DateTimeFormatter to keep it simple.
        String s = t.toString(); // "HH:mm:ss"
        if (s.length() >= 5) {
            return s.substring(0, 5);
        }
        return s;
    }

    private void loadMoviesFromTxt(String fileName) {
        try (BufferedReader reader = new BufferedReader(new FileReader(fileName))) {
            reader.readLine();
            String line;
            while ((line = reader.readLine()) != null) {
                String[] parts = line.split(",");

                try {
                    int movieId = Integer.parseInt(parts[0]);
                    String name = parts[1];
                    TheaterType theaterType;
                    if (parts[2].equals("INDOOR")) {
                        theaterType = TheaterType.INDOOR;
                    } else if (parts[2].equals("OUTDOOR")) {
                        theaterType = TheaterType.OUTDOOR;
                    } else if (parts[2].equals("DRIVE-IN")) {
                        theaterType = TheaterType.DRIVEIN;
                    } else {
                        theaterType = null;
                    }
                    String rated = parts[3];
                    int runtime = Integer.parseInt(parts[4]);
                    int releaseYear = Integer.parseInt(parts[5]);
                    String description = parts[6];

                    this.movies.add(new Movie(movieId, name, theaterType, rated, runtime, releaseYear, description));
                } catch (Exception e) {
                    System.out.println("Due to an error, skipping line in " + fileName + ": " + line);
                    e.printStackTrace();
                }
            }
        } catch (IOException e) {
            System.out.println("Error reading " + fileName);
            e.printStackTrace();
        }
    }
}
