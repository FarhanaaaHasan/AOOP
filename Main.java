import java.util.Random;
// import java.util.Scanner;

//////////////////////////////////////////////////////
// SHARED RESOURCE CLASS
//////////////////////////////////////////////////////

class Resource {

    // Change this according to question
    // Example:
    // balance
    // seats
    // tickets
    // food
    // courseSeats

    private int resource = 5;

    //////////////////////////////////////////////////////
    // SYNCHRONIZED METHOD
    //////////////////////////////////////////////////////

    public synchronized void useResource(String userName,
                                         int amount) {

        System.out.println(userName
                + " wants to use resource: "
                + amount);

        //////////////////////////////////////////////////////
        // WAIT CONDITION (OPTIONAL)
        //////////////////////////////////////////////////////

        while(resource < amount) {

            System.out.println("Waiting...");

            try {
                wait();
            }

            catch(InterruptedException e) {
                e.printStackTrace();
            }
        }

        //////////////////////////////////////////////////////
        // RESOURCE UPDATE
        //////////////////////////////////////////////////////

        resource -= amount;

        System.out.println(userName
                + " completed operation.");

        System.out.println("Remaining Resource: "
                + resource);

        //////////////////////////////////////////////////////
        // WAKE UP THREADS
        //////////////////////////////////////////////////////

        notifyAll();
    }

    //////////////////////////////////////////////////////
    // ADD RESOURCE METHOD (OPTIONAL)
    //////////////////////////////////////////////////////

    public synchronized void addResource(int amount) {

        System.out.println(Thread.currentThread().getName()
                + " adding resource: "
                + amount);

        resource += amount;

        System.out.println("Updated Resource: "
                + resource);

        notifyAll();
    }
}

//////////////////////////////////////////////////////
// THREAD CLASS USING THREAD
//////////////////////////////////////////////////////

class MyThread extends Thread {

    private Resource resourceObject;
    private int amount;

    public MyThread(Resource resourceObject,
                    int amount,
                    String name) {

        super(name);

        this.resourceObject = resourceObject;
        this.amount = amount;
    }

    @Override
    public void run() {

        resourceObject.useResource(getName(),
                amount);
    }
}

//////////////////////////////////////////////////////
// THREAD CLASS USING RUNNABLE
//////////////////////////////////////////////////////

class MyRunnable implements Runnable {

    private Resource resourceObject;
    private int amount;

    public MyRunnable(Resource resourceObject,
                      int amount) {

        this.resourceObject = resourceObject;
        this.amount = amount;
    }

    @Override
    public void run() {

        try {

            Thread.sleep(2000);

        }

        catch(InterruptedException e) {
            e.printStackTrace();
        }

        resourceObject.addResource(amount);
    }
}

//////////////////////////////////////////////////////
// MAIN CLASS
//////////////////////////////////////////////////////

public class Main {

    public static void main(String[] args) {

        //////////////////////////////////////////////////////
        // RANDOM OBJECT (OPTIONAL)
        //////////////////////////////////////////////////////

        Random random = new Random();

        //////////////////////////////////////////////////////
        // SCANNER OBJECT (OPTIONAL)
        //////////////////////////////////////////////////////

        // Scanner input = new Scanner(System.in);

        //////////////////////////////////////////////////////
        // SHARED OBJECT
        //////////////////////////////////////////////////////

        Resource resourceObject =
                new Resource();

        //////////////////////////////////////////////////////
        // THREAD ARRAY (OPTIONAL)
        //////////////////////////////////////////////////////

        MyThread[] threads =
                new MyThread[5];

        //////////////////////////////////////////////////////
        // CREATE THREADS
        //////////////////////////////////////////////////////

        for(int i = 0; i < 5; i++) {

            //////////////////////////////////////////////////////
            // RANDOM INPUT
            //////////////////////////////////////////////////////

            int amount =
                    random.nextInt(5) + 1;

            //////////////////////////////////////////////////////
            // USER INPUT
            //////////////////////////////////////////////////////

            // int amount = input.nextInt();

            //////////////////////////////////////////////////////
            // CREATE THREAD
            //////////////////////////////////////////////////////

            threads[i] =
                    new MyThread(
                            resourceObject,
                            amount,
                            "User-" + (i + 1)
                    );

            //////////////////////////////////////////////////////
            // START THREAD
            //////////////////////////////////////////////////////

            threads[i].start();
        }

        //////////////////////////////////////////////////////
        // RUNNABLE THREAD
        //////////////////////////////////////////////////////

        Thread t =
                new Thread(
                        new MyRunnable(
                                resourceObject,
                                10
                        ),
                        "Producer"
                );

        t.start();

        //////////////////////////////////////////////////////
        // JOIN
        //////////////////////////////////////////////////////

        for(int i = 0; i < 5; i++) {

            try {

                threads[i].join();

            }

            catch(InterruptedException e) {

                e.printStackTrace();
            }
        }

        try {

            t.join();

        }

        catch(InterruptedException e) {

            e.printStackTrace();
        }

        //////////////////////////////////////////////////////
        // FINAL MESSAGE
        //////////////////////////////////////////////////////

        System.out.println("All threads completed.");

        //////////////////////////////////////////////////////
        // CLOSE SCANNER
        //////////////////////////////////////////////////////

        // input.close();
    }
}
