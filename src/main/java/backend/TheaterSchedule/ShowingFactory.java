package main.java.backend.TheaterSchedule;

// static class? or singleton..
public class ShowingFactory {
    public static Showing createSingleShowing(Movie movie) {
        return new SingleFeature(movie);
    }
    public static Showing createDoubleShowing(Movie movie1, Movie movie2) {
        return new DoubleFeature(movie1, movie2);
    }
}