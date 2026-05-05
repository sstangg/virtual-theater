package backend.TheaterSchedule;

import backend.Theater.TheaterType;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
// TODO: figure out how to make this created from a JSON file of Movies
public class Movie {
    private final int movieId;
    private final String name;
    private final TheaterType theaterType;
    private final String rated;
    private final int runtime;
    private final int releaseYear;
    private final String description;
    private final String path;

    public Movie(int movieId, String name, TheaterType theaterType, String rated, int runtime, int releaseYear, String description, String path) {
        this.movieId = movieId;
        this.name = name;
        this.theaterType = theaterType;
        this.rated = rated;
        this.runtime = runtime;
        this.releaseYear = releaseYear;
        this.description = description;
        this.path = path;
    }

    public int getMovieId() {
        return movieId;
    }
    public String getPath() {
        return path;
    }
    public String getName() {
        return name;
    }
    public TheaterType getTheaterType() {
        return theaterType;
    }
    public String getRated() {
        return rated;
    }
    public int getRuntime() {
        return runtime;
    }
    public int getReleaseYear() {
        return releaseYear;
    }
    public String getDescription() {
        return description;
    }

    public static ArrayList<Movie> loadAll(String path) {
        ArrayList<Movie> movies = new ArrayList<Movie>();
        try (BufferedReader reader = new BufferedReader(new FileReader(path))) {
            reader.readLine();
            String line;
            while ((line = reader.readLine()) != null) {
                line = line.trim();
                if (line.length() == 0) {
                    continue;
                }
                String[] parts = line.split(",");
                int movieId = Integer.parseInt(parts[0].trim());
                String name = parts[1].trim();
                TheaterType theaterType = parseTheaterType(parts[2].trim());
                String rated = parts[3].trim();
                int runtime = Integer.parseInt(parts[4].trim());
                int releaseYear = Integer.parseInt(parts[5].trim());
                String description = parts[6].trim();
                movies.add(new Movie(movieId, name, theaterType, rated, runtime, releaseYear, description));
            }
        } catch (IOException e) {
            System.out.println("Error reading " + path);
            e.printStackTrace();
        }
        return movies;
    }

    private static TheaterType parseTheaterType(String value) {
        if ("INDOOR".equals(value)) {
            return TheaterType.INDOOR;
        } else if ("OUTDOOR".equals(value)) {
            return TheaterType.OUTDOOR;
        } else if ("DRIVE-IN".equals(value)) {
            return TheaterType.DRIVEIN;
        }
        return null;
    }

    @Override
    public String toString() {
        return getName();
    }

}
