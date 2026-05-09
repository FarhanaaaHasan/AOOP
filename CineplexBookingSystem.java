import java.util.Random;

class CineplexSystem {
    private int seatsMovie1 = 200; // Mission Impossible: Dead Reckoning
    private int seatsMovie2 = 200; // Oppenheimer
    private int seatsMovie3 = 200; // Barbie

    public synchronized void bookTicket(String customerName, int movieChoice) {
        if (movieChoice == 1) {
            if (seatsMovie1 > 0) {
                seatsMovie1--;
                System.out.println(customerName + " successfully booked a seat for Mission Impossible. Seats left: " + seatsMovie1);
            } else {
                System.out.println(customerName + " failed to book for Mission Impossible. Sorry! No seats are available.");
            }
        } else if (movieChoice == 2) {
            if (seatsMovie2 > 0) {
                seatsMovie2--;
                System.out.println(customerName + " successfully booked a seat for Oppenheimer. Seats left: " + seatsMovie2);
            } else {
                System.out.println(customerName + " failed to book for Oppenheimer. Sorry! No seats are available.");
            }
        } else if (movieChoice == 3) {
            if (seatsMovie3 > 0) {
                seatsMovie3--;
                System.out.println(customerName + " successfully booked a seat for Barbie. Seats left: " + seatsMovie3);
            } else {
                System.out.println(customerName + " failed to book for Barbie. Sorry! No seats are available.");
            }
        }
    }
}

class CustomerThread extends Thread {
    private CineplexSystem system;
    private int movieChoice;

    public CustomerThread(CineplexSystem system, String name, int movieChoice) {
        super(name);
        this.system = system;
        this.movieChoice = movieChoice;
    }

    @Override
    public void run() {
        system.bookTicket(getName(), movieChoice);
    }
}

public class CineplexBookingSystem {
    public static void main(String[] args) {
        CineplexSystem cineplexSystem = new CineplexSystem();
        Random random = new Random();
        CustomerThread[] customers = new CustomerThread[1000];

        // 1000 customers trying to book a seat at the same time
        for (int i = 0; i < 1000; i++) {
            // Generate a random number between 1 and 3 to assign a movie
            int movieChoice = random.nextInt(3) + 1; 
            customers[i] = new CustomerThread(cineplexSystem, "Customer-" + (i + 1), movieChoice);
            customers[i].start();
        }

        // Wait for all customers to finish
        for (int i = 0; i < 1000; i++) {
            try {
                customers[i].join();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
        
        System.out.println("Booking process completed.");
    }
}
