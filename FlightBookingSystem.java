import java.util.Random;

class FlightSystem {
    private int seatsRome = 250;
    private int seatsSeoul = 250;
    private int seatsIstanbul = 250;

    public synchronized void bookFlight(String passengerName, int flightChoice) {
        if (flightChoice == 1) {
            if (seatsRome > 0) {
                seatsRome--;
                System.out.println(passengerName + " successfully booked a flight to Rome. Seats left: " + seatsRome);
            } else {
                System.out.println(passengerName + " failed to book to Rome. Sorry! No flight available.");
            }
        } else if (flightChoice == 2) {
            if (seatsSeoul > 0) {
                seatsSeoul--;
                System.out.println(passengerName + " successfully booked a flight to Seoul. Seats left: " + seatsSeoul);
            } else {
                System.out.println(passengerName + " failed to book to Seoul. Sorry! No flight available.");
            }
        } else if (flightChoice == 3) {
            if (seatsIstanbul > 0) {
                seatsIstanbul--;
                System.out.println(passengerName + " successfully booked a flight to Istanbul. Seats left: " + seatsIstanbul);
            } else {
                System.out.println(passengerName + " failed to book to Istanbul. Sorry! No flight available.");
            }
        }
    }
}

class PassengerThread extends Thread {
    private FlightSystem system;
    private int flightChoice;

    public PassengerThread(FlightSystem system, String name, int flightChoice) {
        super(name);
        this.system = system;
        this.flightChoice = flightChoice;
    }

    @Override
    public void run() {
        system.bookFlight(getName(), flightChoice);
    }
}

public class FlightBookingSystem {
    public static void main(String[] args) {
        FlightSystem flightSystem = new FlightSystem();
        Random random = new Random();
        PassengerThread[] passengers = new PassengerThread[1200];

        // 1200 passengers trying to book a flight
        for (int i = 0; i < 1200; i++) {
            // Generate a random number between 1 and 3 to assign a flight
            int flightChoice = random.nextInt(3) + 1; 
            passengers[i] = new PassengerThread(flightSystem, "Passenger-" + (i + 1), flightChoice);
            passengers[i].start();
        }

        // Wait for all passengers to finish
        for (int i = 0; i < 1200; i++) {
            try {
                passengers[i].join();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
        
        System.out.println("Flight booking process completed.");
    }
}
