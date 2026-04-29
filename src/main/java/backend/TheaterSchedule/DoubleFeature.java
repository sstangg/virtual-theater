package backend.TheaterSchedule;

public class DoubleFeature extends Showing {
    private final int[] movieIds;
    public DoubleFeature(Movie movie1, Movie movie2) {
        super(true);
        movieIds = new int[]{movie1.getMovieId(), movie2.getMovieId()};
    }
}