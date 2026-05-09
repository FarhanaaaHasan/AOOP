import java.util.Scanner;

class BankAccountWithInput {

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

class WithdrawThreadWithInput extends Thread {

    private BankAccountWithInput account;
    private int amount;

    // Constructor
    public WithdrawThreadWithInput(BankAccountWithInput account,
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

class DepositThreadWithInput implements Runnable {

    private BankAccountWithInput account;
    private int amount;

    public DepositThreadWithInput(BankAccountWithInput account,
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

public class BankSystemWithScanner {

    public static void main(String[] args) {
        
        Scanner scanner = new Scanner(System.in);
        
        System.out.println("=== Bank System Input ===");
        System.out.print("Enter withdrawal amount for Alice: ");
        int aliceWithdraw = scanner.nextInt();
        
        System.out.print("Enter withdrawal amount for Bob: ");
        int bobWithdraw = scanner.nextInt();
        
        System.out.print("Enter deposit amount for Depositor: ");
        int depositAmount = scanner.nextInt();
        System.out.println("=========================\n");

        // Shared bank account object
        BankAccountWithInput account = new BankAccountWithInput();

        // Withdraw thread 1
        WithdrawThreadWithInput t1 =
                new WithdrawThreadWithInput(account,
                        aliceWithdraw,
                        "Alice");

        // Withdraw thread 2
        WithdrawThreadWithInput t2 =
                new WithdrawThreadWithInput(account,
                        bobWithdraw,
                        "Bob");

        // Deposit thread
        Thread t3 =
                new Thread(
                        new DepositThreadWithInput(account, depositAmount),
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
        
        scanner.close();
    }
}
