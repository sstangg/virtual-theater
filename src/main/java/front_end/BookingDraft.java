package front_end;

import backend.FoodService.Food;
import backend.Seating.Seat;
import backend.TheaterSchedule.Movie;
import backend.TheaterSchedule.Showing;

import java.util.ArrayList;

public class BookingDraft {
    private Showing showing;
    private Movie movie;
    private String location;
    private String showtime;
    private ArrayList<Seat> chosenSeats;
    private ArrayList<Food> chosenFoods;

    public BookingDraft() {
        this.chosenSeats = new ArrayList<Seat>();
        this.chosenFoods = new ArrayList<Food>();
    }

    public Showing getShowing() {
        return showing; 
    }

    public void setShowing(Showing showing) {
        this.showing = showing; 
    }

    public Movie getMovie() { 
        return movie; 
    }
    public void setMovie(Movie movie) { 
        this.movie = movie; 
    }

    public String getLocation() { 
        return location; 
    }
    public void setLocation(String location) { 
        this.location = location; 
    }

    public String getShowtime() { 
        return showtime; 
    }
    public void setShowtime(String showtime) { 
        this.showtime = showtime; 
    }

    public ArrayList<Seat> getChosenSeats() { 
        return chosenSeats; 
    }
    public void setChosenSeats(ArrayList<Seat> chosenSeats) {
        this.chosenSeats = chosenSeats;
    }

    public ArrayList<Food> getChosenFoods() { 
        return chosenFoods; 
    }
    public void setChosenFoods(ArrayList<Food> chosenFoods) {
        if (chosenFoods == null) {
            this.chosenFoods = new ArrayList<Food>();
        } else {
            this.chosenFoods = chosenFoods;
        }
    }

    public boolean isSeated() {
        return "Indoor".equals(location);
    }

    public String movieName() {
        return movie.getName();
    }
}
