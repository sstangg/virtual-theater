package backend.TheaterSchedule;

public class SingleFeature extends Showing {
    private final int movieId;
    public SingleFeature(Movie m) {
        super(true);
        this.movieId = m.getMovieId();
    }
}