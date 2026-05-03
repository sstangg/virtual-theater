# Virtual Theater
---------------------------------------------------------------------------
- Logan Zhou, Minh Dinh, Sophia Tang

## Files
---------------------------------------------------------------------------
/java
- Main

/backend

- /Theater
  - Theater - Abstract class defining the structure & methods of a general theater object
  - TheaterType - Enum labelling each theater type for easy type-checking
  - DriveInTheater - Concrete class extending the Theater abstract class. Customers are not seated, as it operates on a first-come, first-serve basis. Only 1 Showing can play at a time; does not have Rooms. 
  - IndoorTheater - Concrete class extending the Theater abstract class. Offers seated ticket options for customers & has Rooms for multiple Movies to play at once.
  - OutdoorTheater - Concrete class extending the Theater abstract class. Customers are not seated, as it operates on a first-come, first-serve basis. Only 1 Showing can play at a time; does not have Rooms.

- /TheaterManager
  - TheaterManager - Abstract class defining the structure & methods of a theater manager that facilitate operations between the Theaters, Schedules, FoodService, and frontend-backend communication.
  - DriveInTheaterManager - Concrete class extending the TheaterManager abstract class. 
  - OutdoorTheaterManager - Concrete class extending the TheaterManager abstract class. Schedules Showings into Rooms, organizes Customers into Seats.
  - SeatedTheaterManager - Concrete class extending the TheaterManager abstract class.

- /TheaterSchedule
  - Movie - Class of final objects created based off of database/movies.txt
  - Showing - Abstract class of a Movie showing at a Theater. Created using information from database/showings.txt
  - ShowingFactory - Factory pattern class that creates Showings. Used to abstract Showing & Movie creation logic from Schedule
  - Schedule - Organizes all Showings objects for a Theater
  - SingleFeature - Concrete class extension of Showing abstract. Associated with 1 Movie
  - DoubleFeature - Concrete class extension of Showing abstract. Associated with 2 Movies

- /Seating
  - ..

- /FoodService
  - FoodService - Interface of methods for the buying and selling of Food items.
  - FoodDelivery - Class implementing FoodService interface selling only packaged Foods. only applicable to 
  - ConcessionStand - Class implementing FoodService interface selling only non-packaged Foods. applicable to all
  - Food - Class of final food objects created based off of database/food.txt

/frontend
- CustomerProfile
- IRefreshable
- TheaterFrame
- TheaterLobby
- WelcomePanel

- /TicketBooking
  - ConfirmationPage
  - SearchMovies
  - SeatChart

/database
- food.txt - list of food items represented as Food objects & sold by ConcessionStand & FoodDelivery. packaged food is sold through FoodDelivery, while ConcessionStand sells non-packaged foods
- movies.txt - list of movies represented as Movie objects to be shown at a theater. Each movie is limited to 1 theater type for simplicity's sake
- showings.txt - list of showing times available for Movies, represented by Showing objects created by the ShowingFactory. Showings are either single or double feature

### The architecture supports scalability and extendability because:
- New Theater types can be added by extending the TheaterType enum & the Theater & TheaterManager abstracts for custom theater operations, 
following the Open-Closed Principle.
- New Ticket & Seat types can be added by extending the SeatFactory & TicketFactory methods, as well as the Ticket & Seat abstracts for custom user experiences,
  following the Open-Closed Principle.
- Many new foods, Showing times, and Movies can be added by inserting them in the .txt files in /database. Food, Movie, and Showing 
objects are created from these files in standardized format by ShowingFactory and FoodService.
- IndoorTheaters can scale by having a wide range of Rooms with more Seats with different seating charts. Those seats could be of
many different SeatTypes.
- Schedule organizes Showings, and it could scale by having many Showings in 1 Schedule

## Notes
---------------------------------------------------------------------------
Design Decisions:
1. New Theater


## Division of Work
---------------------------------------------------------------------------

Everyone
- UML diagram
- 

Minh
- Designed GUI
- Implemented frontend
- Data files

Logan
- Implemented backend

Sophia
- Implemented backend
- README
- Presentation slides

## How to compile and run
---------------------------------------------------------------------------

1. Navigate to the directory "611-A4/src" after unzipping the files
2. Run the following instructions:

javac --release 8 -d bin core/\*.java game/\*.java grid/\*.java hero/\*.java iohandler/\*.java main/\*.java monster/\*.java

java -cp bin main/Main

## Input/Output Example
---------------------------------------------------------------------------
Example execution:

```