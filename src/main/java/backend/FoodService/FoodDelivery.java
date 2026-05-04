package backend.FoodService;

import java.util.ArrayList;
import java.util.List;

// Available to all theater types; only packaged food can be delivered.
public class FoodDelivery implements FoodService {
    private final List<Food> foodAvailable;

    public FoodDelivery(List<Food> foodAvailable) {
        this.foodAvailable = new ArrayList<>(foodAvailable);
    }

    @Override
    public List<Food> getMenu() {
        List<Food> menu = new ArrayList<>();
        for (Food f : foodAvailable) {
            if (f.isPackaged()) {
                menu.add(f);
            }
        }
        return menu;
    }
}
