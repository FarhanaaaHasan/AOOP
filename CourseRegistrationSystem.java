import java.util.Random;

class RegistrationSystem {
    private int seatsRobotics = 150;
    private int seatsSecurity = 150;
    private int seatsEmbedded = 150;

    public synchronized void registerCourse(String studentName, int courseChoice) {
        if (courseChoice == 1) {
            if (seatsRobotics > 0) {
                seatsRobotics--;
                System.out.println(studentName + " successfully registered for Robotics. Seats left: " + seatsRobotics);
            } else {
                System.out.println(studentName + " failed to register for Robotics. Sorry! No seats available.");
            }
        } else if (courseChoice == 2) {
            if (seatsSecurity > 0) {
                seatsSecurity--;
                System.out.println(studentName + " successfully registered for Security. Seats left: " + seatsSecurity);
            } else {
                System.out.println(studentName + " failed to register for Security. Sorry! No seats available.");
            }
        } else if (courseChoice == 3) {
            if (seatsEmbedded > 0) {
                seatsEmbedded--;
                System.out.println(studentName + " successfully registered for Embedded Systems. Seats left: " + seatsEmbedded);
            } else {
                System.out.println(studentName + " failed to register for Embedded Systems. Sorry! No seats available.");
            }
        }
    }
}

class StudentThread extends Thread {
    private RegistrationSystem system;
    private int courseChoice;

    public StudentThread(RegistrationSystem system, String name, int courseChoice) {
        super(name);
        this.system = system;
        this.courseChoice = courseChoice;
    }

    @Override
    public void run() {
        system.registerCourse(getName(), courseChoice);
    }
}

public class CourseRegistrationSystem {
    public static void main(String[] args) {
        RegistrationSystem regSystem = new RegistrationSystem();
        Random random = new Random();
        StudentThread[] students = new StudentThread[800];

        // 800 students trying to register
        for (int i = 0; i < 800; i++) {
            // Generate a random number between 1 and 3 to assign a course
            int courseChoice = random.nextInt(3) + 1; 
            students[i] = new StudentThread(regSystem, "Student-" + (i + 1), courseChoice);
            students[i].start();
        }

        // Wait for all students to finish
        for (int i = 0; i < 800; i++) {
            try {
                students[i].join();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
        
        System.out.println("Course registration process completed.");
    }
}
