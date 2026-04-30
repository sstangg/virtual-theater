package backend.TheaterSchedule;

import java.sql.Time;

public final class ShowingFactory {

    private static int nextShowingId = 1;

    // Indoor: 4 slots, each 3 hours (10am–10pm)
    private static final Time INDOOR_SLOT_1_START = Time.valueOf("10:00:00");
    private static final Time INDOOR_SLOT_1_END = Time.valueOf("13:00:00");
    private static final Time INDOOR_SLOT_2_START = Time.valueOf("13:00:00");
    private static final Time INDOOR_SLOT_2_END = Time.valueOf("16:00:00");
    private static final Time INDOOR_SLOT_3_START = Time.valueOf("16:00:00");
    private static final Time INDOOR_SLOT_3_END = Time.valueOf("19:00:00");
    private static final Time INDOOR_SLOT_4_START = Time.valueOf("19:00:00");
    private static final Time INDOOR_SLOT_4_END = Time.valueOf("22:00:00");

    // Outdoor + Drive-in: 2 slots, same double feature both times
    private static final Time OUTDOOR_DRIVE_SLOT_1_START = Time.valueOf("09:00:00");
    private static final Time OUTDOOR_DRIVE_SLOT_1_END = Time.valueOf("15:00:00");
    private static final Time OUTDOOR_DRIVE_SLOT_2_START = Time.valueOf("16:00:00");
    private static final Time OUTDOOR_DRIVE_SLOT_2_END = Time.valueOf("23:00:00");

    private ShowingFactory() {}

    public static int allocateShowingId() {
        return nextShowingId++;
    }

    public static Showing createSingleShowing(Movie movie, Time startTime, Time endTime) {
        Showing s = new SingleFeature(movie, startTime, endTime);
        s.setShowingId(allocateShowingId());
        return s;
    }

    public static Showing createDoubleShowing(Movie movie1, Movie movie2, Time startTime, Time endTime) {
        Showing s = new DoubleFeature(movie1, movie2, startTime, endTime);
        s.setShowingId(allocateShowingId());
        return s;
    }

    public static Showing createIndoorSingleForSlot(Movie movie, int slotIndex) {
        switch (slotIndex) {
            case 0:
                return createSingleShowing(movie, INDOOR_SLOT_1_START, INDOOR_SLOT_1_END);
            case 1:
                return createSingleShowing(movie, INDOOR_SLOT_2_START, INDOOR_SLOT_2_END);
            case 2:
                return createSingleShowing(movie, INDOOR_SLOT_3_START, INDOOR_SLOT_3_END);
            case 3:
                return createSingleShowing(movie, INDOOR_SLOT_4_START, INDOOR_SLOT_4_END);
            default:
                System.out.println("Invalid indoor slot index: " + slotIndex);
                return null;
        }
    }

    public static Showing createOutdoorOrDriveInDoubleForSlot(Movie movie1, Movie movie2, int slotIndex) {
        switch (slotIndex) {
            case 0:
                return createDoubleShowing(movie1, movie2, OUTDOOR_DRIVE_SLOT_1_START, OUTDOOR_DRIVE_SLOT_1_END);
            case 1:
                return createDoubleShowing(movie1, movie2, OUTDOOR_DRIVE_SLOT_2_START, OUTDOOR_DRIVE_SLOT_2_END);
            default:
                System.out.println("Invalid outdoor/drive-in slot index: " + slotIndex);
                return null;
        }
    }
}
