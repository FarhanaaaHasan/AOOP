/*
 * Question: 
 * You have an array of 10,000 integers. Write a program that uses 4 threads 
 * to calculate the total sum of the array to make it faster. 
 * (Thread 1 calculates indexes 0-2499, Thread 2 calculates 2500-4999, etc.)
 */

class SumThread extends Thread {
    private int[] array;
    private int start, end;
    private int partialSum = 0;

    public SumThread(int[] array, int start, int end) {
        this.array = array;
        this.start = start;
        this.end = end;
    }

    @Override
    public void run() {
        for (int i = start; i < end; i++) {
            partialSum += array[i];
        }
    }

    // Method to retrieve the calculated sum for this thread's segment
    public int getPartialSum() {
        return partialSum;
    }
}

public class MultiThreadSum {
    public static void main(String[] args) throws InterruptedException {
        int[] array = new int[10000];
        
        // Fill the array with 1s so we know the expected sum is 10000
        for(int i = 0; i < 10000; i++) {
            array[i] = 1; 
        }

        // Split work among 4 threads
        SumThread t1 = new SumThread(array, 0, 2500);
        SumThread t2 = new SumThread(array, 2500, 5000);
        SumThread t3 = new SumThread(array, 5000, 7500);
        SumThread t4 = new SumThread(array, 7500, 10000);

        // Start all threads simultaneously
        t1.start(); 
        t2.start(); 
        t3.start(); 
        t4.start();
        
        // Wait for all threads to finish their calculations
        t1.join();  
        t2.join();  
        t3.join();  
        t4.join();

        // Aggregate the results
        int totalSum = t1.getPartialSum() + t2.getPartialSum() + t3.getPartialSum() + t4.getPartialSum();
        
        System.out.println("Total Sum calculated by 4 threads is: " + totalSum);
    }
}
