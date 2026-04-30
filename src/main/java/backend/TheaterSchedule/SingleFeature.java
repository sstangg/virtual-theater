package backend.TheaterSchedule;

import java.sql.Time;
    
public class SingleFeature extends Showing {
    public SingleFeature(Movie m, Time startTime, Time endTime) {
        super(true, startTime, endTime);
        setMovieId(m.getMovieId());
    }
}