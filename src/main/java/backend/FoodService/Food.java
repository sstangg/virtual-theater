package backend.FoodService;

public class Food {
    private final int foodId;
    private final String name;
    private final double price;
    private final boolean packaged;

    public Food(int foodId, String name, double price, boolean packaged) {
        this.foodId = foodId;
        this.name = name;
        this.price = price;
        this.packaged = packaged;
    }

    public int getFoodId() { return foodId; }
    public String getName() { return name; }
    public double getPrice() { return price; }
    public boolean isPackaged() { return packaged; }

    @Override
    public String toString() {
        return name + " ($" + price + ")";
    }
}
