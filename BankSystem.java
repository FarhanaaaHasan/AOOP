class BankAccount {

    // Shared bank balance
    private int balance = 0;

    // synchronized = only one thread can use this method at a time
    public synchronized void withdraw(int amount) {

        // Current thread name
        System.out.println(Thread.currentThread().getName()
                + " wants to withdraw " + amount);

        // If balance is not enough
        while (balance < amount) {

            System.out.println("Insufficient balance. Waiting for deposit...");

            try {

                // wait() makes thread sleep until another thread wakes it
                wait();

            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }

        // After enough balance
        balance -= amount;

        System.out.println(Thread.currentThread().getName()
                + " completed withdrawal.");

        System.out.println("Current Balance: " + balance);
    }

    // Deposit method
    public synchronized void deposit(int amount) {

        System.out.println(Thread.currentThread().getName()
                + " depositing " + amount);

        // Add money
        balance += amount;

        System.out.println("Current Balance: " + balance);

        // Wake up all waiting threads
        notifyAll();
    }
}

//////////////////////////////////////////////////////
// Withdraw Thread using Thread class
//////////////////////////////////////////////////////

class WithdrawThread extends Thread {

    private BankAccount account;
    private int amount;

    // Constructor
    public WithdrawThread(BankAccount account,
                          int amount,
                          String name) {

        // Gives thread a name
        super(name);

        this.account = account;
        this.amount = amount;
    }

    @Override
    public void run() {

        // Thread calls withdraw method
        account.withdraw(amount);
    }
}

//////////////////////////////////////////////////////
// Deposit Thread using Runnable interface
//////////////////////////////////////////////////////

class DepositThread implements Runnable {

    private BankAccount account;
    private int amount;

    public DepositThread(BankAccount account,
                         int amount) {

        this.account = account;
        this.amount = amount;
    }

    @Override
    public void run() {

        try {

            // Wait 2 seconds before depositing
            Thread.sleep(2000);

        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        // Deposit money
        account.deposit(amount);
    }
}

//////////////////////////////////////////////////////
// Main Class
//////////////////////////////////////////////////////

public class BankSystem {

    public static void main(String[] args) {

        // Shared bank account object
        BankAccount account = new BankAccount();

        // Withdraw thread 1
        WithdrawThread t1 =
                new WithdrawThread(account,
                        500,
                        "Alice");

        // Withdraw thread 2
        WithdrawThread t2 =
                new WithdrawThread(account,
                        700,
                        "Bob");

        // Deposit thread
        Thread t3 =
                new Thread(
                        new DepositThread(account, 1000),
                        "Depositor");

        // Start all threads
        t1.start();
        t2.start();
        t3.start();

        // Wait until all threads finish
        try {

            t1.join();
            t2.join();
            t3.join();

        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        System.out.println("All transactions completed.");
    }
}
