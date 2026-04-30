package backend.TheaterSchedule;

import backend.Theater.TheaterType;

import java.sql.Time;
// TODO: figure out how to make this created from a JSON file of Movies
public class Movie {
    private final int movieId;
    private final String name;
    private final TheaterType theaterType;
    private final String rated;
    private final int runtime;
    private final int releaseYear;
    private final String description;

    public Movie(int movieId, String name, TheaterType theaterType, String rated, int runtime, int releaseYear, String description) {
        this.movieId = movieId;
        this.name = name;
        this.theaterType = theaterType;
        this.rated = rated;
        this.runtime = runtime;
        this.releaseYear = releaseYear;
        this.description = description;
    }

    public int getMovieId() {
        return movieId;
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

    @Override
    public String toString() {
        return getName();
    }

}
