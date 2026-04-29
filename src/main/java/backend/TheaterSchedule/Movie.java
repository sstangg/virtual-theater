package backend.TheaterSchedule;
import java.sql.Time;
// TODO: figure out how to make this created from a JSON file of Movies
public class Movie {
    private final int movieId;
    private final String name;
    private final Time runtime;

    public Movie(int movieId, String name, Time runtime) {
        this.movieId = movieId;
        this.name = name;
        this.runtime = runtime;
    }

    public int getMovieId() {
        return movieId;
    }
    public String getName() {
        return name;
    }
    public Time getRuntime() {
        return runtime;
    }

    @Override
    public String toString() {
        return getName();
    }

}
