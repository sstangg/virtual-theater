package backend.TheaterSchedule;

import java.sql.Time;

public class DoubleFeature extends Showing {
    private final int[] movieIds;
    public DoubleFeature(Movie movie1, Movie movie2, Time startTime, Time endTime) {
        super(false, startTime, endTime);
        movieIds = new int[]{movie1.getMovieId(), movie2.getMovieId()};
    }

    @Override
    public int getMovieId() {
        System.out.println("DoubleFeature does not have a single movie id");
        return -1; // DoubleFeature does not have a single movie id
    }

    public int[] getMovieIds() {
        return new int[]{movieIds[0], movieIds[1]};
    }

    /** Given one of the two movies in this double feature, returns the other movie's id. */
    public int getPartnerMovieId(int movieId) {
        if (movieIds[0] == movieId) {
            return movieIds[1];
        } else if (movieIds[1] == movieId) {
            return movieIds[0];
        } else {
            System.out.println("movieId not part of this double feature: " + movieId);
            return -1; // movieId not part of this double feature
        }
    }
}