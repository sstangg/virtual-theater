package backend.FoodService;

import java.util.ArrayList;
import java.util.List;

// Indoor and drive-in theaters have concession stands; sells everything (packaged + non-packaged).
public class ConcessionStand implements FoodService {
    private final int theaterId;
    private final List<Food> foodAvailable;

    public ConcessionStand(int theaterId, List<Food> foodAvailable) {
        this.theaterId = theaterId;
        this.foodAvailable = new ArrayList<>(foodAvailable);
    }

    public int getTheaterId() { return theaterId; }

    @Override
    public List<Food> getMenu() {
        return new ArrayList<>(foodAvailable);
    }
}
