class BankAccount {
    private int balance = 0;

    public synchronized void withdraw(int amount) {
        System.out.println(Thread.currentThread().getName() + " wants to withdraw " + amount);
        
        while (balance < amount) {
            System.out.println("Insufficient balance. Waiting for deposit...");
            try {
                wait();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
        
        balance -= amount;
        System.out.println(Thread.currentThread().getName() + " completed withdrawal.");
        System.out.println("Current Balance: " + balance);
    }

    public synchronized void deposit(int amount) {
        System.out.println(Thread.currentThread().getName() + " depositing " + amount);
        balance += amount;
        System.out.println("Current Balance: " + balance);
        notifyAll();
    }
}

class WithdrawThread extends Thread {
    private BankAccount account;
    private int amount;

    public WithdrawThread(BankAccount account, int amount, String name) {
        super(name);
        this.account = account;
        this.amount = amount;
    }

    @Override
    public void run() {
        account.withdraw(amount);
    }
}

class DepositThread implements Runnable {
    private BankAccount account;
    private int amount;

    public DepositThread(BankAccount account, int amount) {
        this.account = account;
        this.amount = amount;
    }

    @Override
    public void run() {
        try {
            Thread.sleep(2000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        account.deposit(amount);
    }
}

public class BankSystemWithoutComment {
    public static void main(String[] args) {
        BankAccount account = new BankAccount();
        
        WithdrawThread t1 = new WithdrawThread(account, 500, "Alice");
        WithdrawThread t2 = new WithdrawThread(account, 700, "Bob");
        Thread t3 = new Thread(new DepositThread(account, 1000), "Depositor");

        t1.start();
        t2.start();
        t3.start();

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
