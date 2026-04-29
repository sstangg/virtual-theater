package backend.TheaterManager;

import backend.Theater.TheaterType;

public class SeatedTheaterManager extends TheaterManager {
    public SeatedTheaterManager() {
        super(TheaterType.INDOOR);
    }
}