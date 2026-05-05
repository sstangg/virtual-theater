package backend.FoodService;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;

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

    public static ArrayList<Food> loadAll(String path) {
        ArrayList<Food> foods = new ArrayList<Food>();
        try (BufferedReader reader = new BufferedReader(new FileReader(path))) {
            reader.readLine();
            String line;
            while ((line = reader.readLine()) != null) {
                line = line.trim();
                if (line.length() == 0) {
                    continue;
                }
                String[] parts = line.split(",");
                int foodId = Integer.parseInt(parts[0].trim());
                String name = parts[1].trim();
                double price = Double.parseDouble(parts[2].trim());
                boolean packaged = Integer.parseInt(parts[3].trim()) == 1;
                foods.add(new Food(foodId, name, price, packaged));
            }
        } catch (IOException e) {
            System.out.println("Error reading " + path);
            e.printStackTrace();
        }
        return foods;
    }

    @Override
    public String toString() {
        return name + " ($" + price + ")";
    }
}
