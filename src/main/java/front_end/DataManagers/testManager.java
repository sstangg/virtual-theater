package front_end.DataManagers;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;

import backend.Customer;
import backend.FoodService.Food;
import backend.Seating.Seat;
import backend.Seating.SeatFactory;
import backend.Seating.SeatType;
import backend.Theater.IndoorTheater;
import backend.Theater.Room;
import backend.TheaterSchedule.DoubleFeature;
import backend.TheaterSchedule.Movie;
import backend.TheaterSchedule.Schedule;
import backend.TheaterSchedule.Showing;
import backend.Theater.TheaterType;
import backend.Tickets.SeatedTicket;
import backend.Tickets.Ticket;
import backend.Tickets.TicketFactory;
import backend.Tickets.UnseatedTicket;

import java.util.HashMap;

public class testManager {
    private static final String MOVIES_FILE_PATH = "src/main/java/database/movies.txt";
    private static final String FOODS_FILE_PATH = "src/main/java/database/food.txt";

    // Hardcoded chart dimensions matching SeatChart.
    private static final int SEAT_ROW_COUNT = 10;
    private static final int SEAT_COL_COUNT = 12;
    private static final String[] SEAT_ROW_LABELS = { "A", "B", "C", "D", "E", "F", "G", "H", "I", "J" };

    private final ArrayList<Movie> movies;
    private final ArrayList<Food> foods;
    private final Schedule schedule;
    private final String[][] searchMovieRows;

    // Indoor theatre with a single 120-seat room. Every chart cell maps to one of these Seats.
    private final IndoorTheater indoorTheater;
    private final Seat[][] seatGrid;
    private final HashMap<Integer, String> seatIdToLabel;
    private final HashMap<String, Seat> labelToSeat;

    // Global sold-ticket list
    private final ArrayList<Ticket> soldTickets;

    // Customer roster created on welcome.
    private final ArrayList<Customer> customers;
    private int nextCustomerId;

    public testManager() {
        this.movies = new ArrayList<>();
        this.foods = new ArrayList<>();
        loadMoviesFromTxt(MOVIES_FILE_PATH);
        loadFoodsFromTxt(FOODS_FILE_PATH);
        // build schedule based on the movies in database
        this.schedule = Schedule.buildScheduleFromMovies(this.movies);
        // from schedule and movies, build the search movie rows for the search movie UI
        this.searchMovieRows = buildSearchMovieRows(this.movies, this.schedule);

        // Build the indoor theatre + seat grid.
        this.seatGrid = new Seat[SEAT_ROW_COUNT][SEAT_COL_COUNT];
        this.seatIdToLabel = new HashMap<Integer, String>(); // map the seat id to the label
        this.labelToSeat = new HashMap<String, Seat>(); // map the label to the seat

        Room room = new Room(1);
        for (int r = 0; r < SEAT_ROW_COUNT; r++) {
            for (int c = 0; c < SEAT_COL_COUNT; c++) {
                Seat s = createSeatForPosition(r, c);
                room.addSeat(s); // add the seat to the room
                seatGrid[r][c] = s; // add the seat to the seat grid
                String label = SEAT_ROW_LABELS[r] + String.valueOf(c + 1); // create the label for the seat
                seatIdToLabel.put(Integer.valueOf(s.getSeatId()), label); // map the seat id to the label
                labelToSeat.put(label, s); // map the label to the seat
            }
        }
        ArrayList<Room> rooms = new ArrayList<Room>();
        rooms.add(room);
        this.indoorTheater = new IndoorTheater(1, rooms);

        this.soldTickets = new ArrayList<Ticket>();
        this.customers = new ArrayList<Customer>();
        this.nextCustomerId = 1;
    }

    public ArrayList<Movie> getMovies() {
        return this.movies;
    }

    public Schedule getSchedule() {
        return this.schedule;
    }

    public String[][] getSearchMovieRows() {
        return this.searchMovieRows;
    }

    public ArrayList<Food> getFoods() {
        return this.foods;
    }

    // Get the delivery foods (packaged)
    public ArrayList<Food> getDeliveryFoods() {
        ArrayList<Food> deliveryFoods = new ArrayList<Food>();
        for (int i = 0; i < foods.size(); i++) {
            Food f = foods.get(i);
            if (f.isPackaged()) {
                deliveryFoods.add(f);
            }
        }
        return deliveryFoods;
    }

    // Get the concession foods (non-packaged)
    public ArrayList<Food> getConcessionFoods() {
        ArrayList<Food> concessionFoods = new ArrayList<Food>();
        for (int i = 0; i < foods.size(); i++) {
            Food f = foods.get(i);
            if (!f.isPackaged()) {
                concessionFoods.add(f);
            }
        }
        return concessionFoods;
    }

    // Movie / Showing lookup helpers ---------------------------------------------------

    public Movie getMovieByName(String name) {
        for (int i = 0; i < movies.size(); i++) {
            if (name.equals(movies.get(i).getName())) {
                return movies.get(i);
            }
        }
        return null;
    }

    public Movie getMovieById(int movieId) {
        for (int i = 0; i < movies.size(); i++) {
            if (movies.get(i).getMovieId() == movieId) {
                return movies.get(i);
            }
        }
        return null;
    }

    // Find the Showing in the schedule that contains the given movie and starts at the given time
    public Showing findShowing(int movieId, String startTimeHhMm) {
        ArrayList<Showing> showings = schedule.getShowings();
        for (int i = 0; i < showings.size(); i++) {
            Showing sh = showings.get(i);
            // If the start time is not the same as the given start time, continue
            if (!startTimeHhMm.equals(formatTime(sh.getStartTime()))) {
                continue;
            }
            // If the showing is a double feature, check if the movie id is one of the two movies in the double feature
            if (sh instanceof DoubleFeature) {
                int[] ids = ((DoubleFeature) sh).getMovieIds();
                if (ids[0] == movieId || ids[1] == movieId) {
                    return sh;
                }
            } else if (sh.getMovieId() == movieId) { // If the showing is a single feature, check the movie id
                return sh;
            }
        }
        return null;
    }

    // Find a showing by its ID
    public Showing findShowingById(int showingId) {
        ArrayList<Showing> showings = schedule.getShowings();
        for (int i = 0; i < showings.size(); i++) {
            Showing sh = showings.get(i);
            if (sh.getShowingId() == showingId) {
                return sh;
            }
        }
        return null;
    }

    // True if the customer owns a ticket for any showing in the given theater type.
    public boolean customerHasTicketForTheaterType(Customer customer, TheaterType theaterType) {
        // loop through the customer's tickets
        for (int i = 0; i < customer.getTickets().size(); i++) {
            Ticket t = customer.getTickets().get(i);
            Showing sh = findShowingById(t.getShowingId());

            // If the showing is a double feature, check if the movie id is one of the two movies in the double feature
            if (sh instanceof DoubleFeature) {
                int[] ids = ((DoubleFeature) sh).getMovieIds();
                for (int j = 0; j < ids.length; j++) {
                    Movie m = getMovieById(ids[j]);
                    if (m != null && m.getTheaterType() == theaterType) {
                        return true;
                    }
                }
            } else { // If the showing is a single feature, check the movie id
                Movie m = getMovieById(sh.getMovieId());
                if (m != null && m.getTheaterType() == theaterType) {
                    return true;
                }
            }
        }
        // If no ticket is found, return false
        return false;
    }

    // Indoor seat grid -----------------------------------------------------------------

    public IndoorTheater getIndoorTheater() {
        return indoorTheater;
    }

    public Seat getSeatByLabel(String label) {
        return labelToSeat.get(label);
    }

    public String getSeatLabel(int seatId) {
        return seatIdToLabel.get(Integer.valueOf(seatId));
    }

    // Gloabl sold-ticket 

    public boolean isSeatBookedForShowing(int showingId, int seatId) {
        // loop through the sold tickets
        for (int i = 0; i < soldTickets.size(); i++) {
            Ticket t = soldTickets.get(i);
            // If the ticket is a seated ticket and the showing id and seat id match, return true
            if (t instanceof SeatedTicket && t.getShowingId() == showingId && ((SeatedTicket) t).getSeatId() == seatId) {
                return true;
            }
        }
        // If no ticket is found, return false
        return false;
    }

    public ArrayList<Ticket> getSoldTickets() {
        return soldTickets;
    }

    private Seat createSeatForPosition(int row, int col) {
        if (isLuxuryPosition(row, col)) {
            return SeatFactory.createLuxurySeat();
        } else if (isEnhancedPosition(row, col)) {
            return SeatFactory.createEnhancedSeat();
        }
        return SeatFactory.createBasicSeat();
    }

    private boolean isLuxuryPosition(int row, int col) {
        return row >= 3 && row <= 5 && col >= 4 && col <= 7;
    }

    private boolean isEnhancedPosition(int row, int col) {
        return row >= 2 && row <= 6 && col >= 2 && col <= 9;
    }

    // Customer ------------------------------------------------------------------

    public Customer createCustomer(String name) {
        Customer c = new Customer(nextCustomerId, name, false);
        nextCustomerId++;
        customers.add(c);
        return c;
    }

    public ArrayList<Customer> getCustomers() {
        return customers;
    }

    // --- Purchase finalization ------------------------------------------------------------

    // Build the tickets and attach them to the customer and the global sold-ticket list
    public ArrayList<Ticket> completePurchase(BookingDraft draft, Customer customer) {
        ArrayList<Ticket> created = new ArrayList<Ticket>();

        int showingId = draft.getShowing().getShowingId();

        // If the draft is seated, create a seated ticket for each chosen seat
        if (draft.isSeated() && draft.getChosenSeats().size() > 0) {
            ArrayList<Seat> seats = draft.getChosenSeats();
            for (int i = 0; i < seats.size(); i++) {
                Seat s = seats.get(i);
                SeatedTicket t = TicketFactory.createSeatedTicket(customer.getCustomerId(), showingId, s.getSeatId(), s.getType(), customer.isPreferred());
                customer.addTicket(t);
                soldTickets.add(t);
                created.add(t);
            }
        } else { // If the draft is unseated, create an unseated ticket
            UnseatedTicket t = TicketFactory.createUnseatedTicket(customer.getCustomerId(), showingId, customer.isPreferred());
            customer.addTicket(t);
            soldTickets.add(t);
            created.add(t);
        }

        // Add the chosen foods to the customer
        ArrayList<Food> chosenFoods = draft.getChosenFoods();
        if (chosenFoods != null) {
            for (int i = 0; i < chosenFoods.size(); i++) {
                customer.addFood(chosenFoods.get(i));
            }
        }
        return created;
    }

    // Total seat price for a draft's chosen seats.
    public double seatTotal(BookingDraft draft) {
        return seatTotal(draft, null);
    }

    public double seatTotal(BookingDraft draft, Customer customer) {
        double total = 0.0;
        if (!draft.isSeated()) {
            total = TicketFactory.UNSEATED_PRICE;
        } else {
            for (int i = 0; i < draft.getChosenSeats().size(); i++) {
                total += draft.getChosenSeats().get(i).getPrice();
            }
        }
        return discountedPrice(total, customer);
    }

    // Total food price for a draft's chosen foods.
    public double foodTotal(BookingDraft draft) {
        return foodTotal(draft, null);
    }

    public double foodTotal(BookingDraft draft, Customer customer) {
        double total = 0.0;
        for (int i = 0; i < draft.getChosenFoods().size(); i++) {
            total += draft.getChosenFoods().get(i).getPrice();
        }
        return discountedPrice(total, customer);
    }

    public double discountedPrice(double price, Customer customer) {
        return TicketFactory.applyDiscount(price, customer != null && customer.isPreferred());
    }

    public String seatTypeLabel(SeatType type) {
        if (type == SeatType.LUXURY) {
            return "Luxury";
        } else if (type == SeatType.ENHANCED) {
            return "Enhanced";
        }
        return "Basic";
    }

    // Format a "HH:mm" time string for a given showing
    public String formatShowingTime(Showing sh) {
        return formatTime(sh.getStartTime());
    }

    private String[][] buildSearchMovieRows(ArrayList<Movie> movies, Schedule schedule) {
        String[][] rows = new String[movies.size()][7];

        // Create search row for each movie
        for (int i = 0; i < movies.size(); i++) {
            Movie m = movies.get(i);

            // Collect showtimes for this movie by scanning all showings.
            ArrayList<String> times = new ArrayList<String>();
            String partnerName = null;

            // Get all showings from the schedule
            ArrayList<Showing> showings = schedule.getShowings();
            for (int j = 0; j < showings.size(); j++) {
                Showing sh = showings.get(j);

                // If the showing is a double feature, get the movie ids and add the showtimes to the times list
                if (sh instanceof DoubleFeature) {
                    DoubleFeature df = (DoubleFeature) sh;
                    int[] ids = df.getMovieIds();
                    int id1 = ids[0];
                    int id2 = ids[1];

                    if (m.getMovieId() == id1 || m.getMovieId() == id2) {
                        times.add(formatTime(sh.getStartTime()));

                        int otherId;
                        if (m.getMovieId() == id1) {
                            otherId = id2;
                        } else {
                            otherId = id1;
                        }

                        // Find partner name to add to the description
                        for (int k = 0; k < movies.size(); k++) {
                            if (movies.get(k).getMovieId() == otherId) {
                                partnerName = movies.get(k).getName();
                                break;
                            }
                        }
                    }
                } else {
                    // If the showing is a single feature, add the showtime to the times list
                    if (sh.getMovieId() == m.getMovieId()) {
                        times.add(formatTime(sh.getStartTime()));
                    }
                }
            }

            String description = m.getDescription();
            // If the movie is a double feature, add the partner name to the description
            if (partnerName != null) {
                description = description + "\n\nNote: Double feature with " + partnerName + ". Same showtimes.";
            }

            // create the formatted row for the movie
            rows[i] = new String[] {
                    m.getName(),
                    locationLabel(m.getTheaterType()),
                    m.getRated(),
                    String.valueOf(m.getReleaseYear()),
                    m.getRuntime() + " mins",
                    description,
                    joinTimes(times)
            };
        }

        return rows;
    }

    // Join the times into a string separated by commas
    private String joinTimes(ArrayList<String> times) { 
        String result = "";
        for (int i = 0; i < times.size(); i++) {
            if (i > 0) {
                result = result + ", ";
            }
            result = result + times.get(i);
        }
        return result;
    }

    private String locationLabel(TheaterType type) {
        if (type == null) {
            return "";
        }
        if (type == TheaterType.INDOOR) {
            return "Indoor";
        } else if (type == TheaterType.OUTDOOR) {
            return "Outdoor";
        } else if (type == TheaterType.DRIVEIN) {
            return "Drive-In";
        }
        return "";
    }

    private String formatTime(java.sql.Time t) {
        // "HH:mm" without using DateTimeFormatter to keep it simple.
        String s = t.toString(); // "HH:mm:ss"
        if (s.length() >= 5) {
            return s.substring(0, 5);
        }
        return s;
    }

    private void loadMoviesFromTxt(String fileName) {
        try (BufferedReader reader = new BufferedReader(new FileReader(fileName))) {
            reader.readLine();
            String line;
            while ((line = reader.readLine()) != null) {
                String[] parts = line.split(",");

                try {
                    int movieId = Integer.parseInt(parts[0]);
                    String name = parts[1];
                    TheaterType theaterType;
                    if (parts[2].equals("INDOOR")) {
                        theaterType = TheaterType.INDOOR;
                    } else if (parts[2].equals("OUTDOOR")) {
                        theaterType = TheaterType.OUTDOOR;
                    } else if (parts[2].equals("DRIVE-IN")) {
                        theaterType = TheaterType.DRIVEIN;
                    } else {
                        theaterType = null;
                    }
                    String rated = parts[3];
                    int runtime = Integer.parseInt(parts[4]);
                    int releaseYear = Integer.parseInt(parts[5]);
                    String description = parts[6];

                    this.movies.add(new Movie(movieId, name, theaterType, rated, runtime, releaseYear, description));
                } catch (Exception e) {
                    System.out.println("Due to an error, skipping line in " + fileName + ": " + line);
                    e.printStackTrace();
                }
            }
        } catch (IOException e) {
            System.out.println("Error reading " + fileName);
            e.printStackTrace();
        }
    }

    private void loadFoodsFromTxt(String fileName) {
        try (BufferedReader reader = new BufferedReader(new FileReader(fileName))) {
            reader.readLine();
            String line;
            while ((line = reader.readLine()) != null) {
                line = line.trim();
                if (line.length() == 0) {
                    continue;
                }
                String[] parts = line.split(",");
                for (int p = 0; p < parts.length; p++) {
                    parts[p] = parts[p].trim();
                }

                try {
                    int foodId = Integer.parseInt(parts[0]);
                    String name = parts[1];
                    double price = Double.parseDouble(parts[2]);
                    int packagedFlag = Integer.parseInt(parts[3]);
                    boolean packaged = packagedFlag != 0;

                    this.foods.add(new Food(foodId, name, price, packaged));
                } catch (Exception e) {
                    System.out.println("Due to an error, skipping line in " + fileName + ": " + line);
                    e.printStackTrace();
                }
            }
        } catch (IOException e) {
            System.out.println("Error reading " + fileName);
            e.printStackTrace();
        }
    }
 
}
