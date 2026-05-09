/*
 * Question: 
 * Write a program where a Producer thread adds items to a list, 
 * and a Consumer thread removes items. The list has a maximum capacity of 5. 
 * The Producer must wait() if the list is full, and the Consumer must wait() 
 * if the list is empty. Demonstrate this using multithreading.
 */

import java.util.LinkedList;

class Buffer {
    private LinkedList<Integer> list = new LinkedList<>();
    private int capacity = 5;

    public synchronized void produce(int value) {
        try {
            // WAIT if buffer is full
            while (list.size() == capacity) {
                System.out.println("Buffer full. Producer waiting...");
                wait();
            }
            
            list.add(value);
            System.out.println("Produced: " + value);
            
            // Notify Consumer that an item is available
            notifyAll();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    public synchronized void consume() {
        try {
            // WAIT if buffer is empty
            while (list.size() == 0) {
                System.out.println("Buffer empty. Consumer waiting...");
                wait();
            }
            
            int value = list.removeFirst();
            System.out.println("Consumed: " + value);
            
            // Notify Producer that space is available
            notifyAll();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
}

class Producer extends Thread {
    private Buffer buffer;

    public Producer(Buffer buffer) {
        this.buffer = buffer;
    }

    @Override
    public void run() {
        // Produce 10 items
        for (int i = 1; i <= 10; i++) {
            buffer.produce(i);
            try {
                Thread.sleep(500); // Sleep to simulate time taken to produce
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }
}

class Consumer extends Thread {
    private Buffer buffer;

    public Consumer(Buffer buffer) {
        this.buffer = buffer;
    }

    @Override
    public void run() {
        // Consume 10 items
        for (int i = 1; i <= 10; i++) {
            buffer.consume();
            try {
                Thread.sleep(800); // Consumer is slightly slower than Producer
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }
}

public class ProducerConsumer {
    public static void main(String[] args) {
        Buffer buffer = new Buffer();
        
        Producer p = new Producer(buffer);
        Consumer c = new Consumer(buffer);

        p.start();
        c.start();

        try {
            p.join();
            c.join();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        System.out.println("Production and Consumption Finished.");
    }
}
