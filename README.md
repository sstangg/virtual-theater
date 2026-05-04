# Virtual Theater
---------------------------------------------------------------------------
- Logan Zhou, Minh Dinh, Sophia Tang

## Files
---------------------------------------------------------------------------
/java
- Main

/backend

- Customer - Class representing a customer of the booking system. Tracks customerId, name, isPreferred status, owned Tickets, and purchased Foods. Exposes watchMovie for the Watching Movie experience.

- /Theater
  - Theater - Abstract class defining the structure & methods of a general theater object
  - TheaterType - Enum labelling each theater type for easy type-checking
  - DriveInTheater - Concrete class extending the Theater abstract class. Customers are not seated, as it operates on a first-come, first-serve basis. Only 1 Showing can play at a time; does not have Rooms. 
  - IndoorTheater - Concrete class extending the Theater abstract class. Offers seated ticket options for customers & has Rooms for multiple Movies to play at once.
  - OutdoorTheater - Concrete class extending the Theater abstract class. Customers are not seated, as it operates on a first-come, first-serve basis. Only 1 Showing can play at a time; does not have Rooms.
  - Room - Class representing a room inside an IndoorTheater. Holds a List<Seat>. IndoorTheaters scale by having multiple Rooms.

- /TheaterManager
  - TheaterManager - Abstract class defining the structure & methods of a theater manager that facilitate operations between the Theaters, Schedules, FoodService, and frontend-backend communication. Holds a list of sold Tickets, and exposes seat-conflict helpers (isSeatBooked, hasTicketForShowing, getTicketsForCustomer) used by the booking & watching flows.
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
  - Seat - Abstract class defining the structure & methods of a general seat object
  - SeatType - Enum labelling each seat type for easy type-checking (BASIC, ENHANCED, LUXURY)
  - BasicSeat - Concrete class extending the Seat abstract class. Standard seating, lowest price.
  - EnhancedSeat - Concrete class extending the Seat abstract class. Premium seating with extra legroom.
  - LuxurySeat - Concrete class extending the Seat abstract class. First-class service with recliners and table service.
  - SeatFactory - Factory pattern class that creates Seats. Used to abstract Seat creation logic and assign unique seatId.
  - SeatingStrategy - Interface labelling the seating policy of a Theater (assigned vs unassigned).
  - AssignedSeating - Concrete class implementing SeatingStrategy. Used by IndoorTheater for fixed seat assignment.
  - UnassignedSeating - Concrete class implementing SeatingStrategy. Used by OutdoorTheater & DriveInTheater for first-come, first-serve.

- /FoodService
  - FoodService - Interface of methods for the buying and selling of Food items.
  - FoodDelivery - Class implementing FoodService interface selling only packaged Foods. only applicable to 
  - ConcessionStand - Class implementing FoodService interface selling only non-packaged Foods. applicable to all
  - Food - Class of final food objects created based off of database/food.txt

- /Tickets
  - Ticket - Abstract class defining the structure of a ticket booked by a Customer. Holds ticketId, userId, and showingId. Frame only; pricing & TheaterType to be filled in.
  - SeatedTicket - Concrete class extending the Ticket abstract class. Used for IndoorTheater bookings. Holds seatId & SeatType.
  - UnseatedTicket - Concrete class extending the Ticket abstract class. Used for OutdoorTheater & DriveInTheater bookings.
  - TicketFactory - Factory pattern class that creates Tickets. Used to abstract Ticket creation logic and assign unique ticketId.

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
2. Watching Movie simulation - Customer.watchMovie(int showingId, String movieName) returns the simulation string for the Watching Movie page. Different text for basic / enhanced / luxury seats and for unseated (outdoor / drive-in) tickets, plus the Customer's purchased Foods. Takes primitives instead of (Showing, Movie) objects so the simulation does not depend on the still-evolving Showing/Movie API.
3. Seat-conflict check - TheaterManager keeps a list of sold Tickets and exposes isSeatBooked(seatId, showingId). Frontend calls this when rendering the SeatChart to disable seats already booked for the chosen showing.


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
- README

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