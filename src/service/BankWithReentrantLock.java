package service;


import java.math.BigDecimal;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.locks.ReentrantLock;

public class BankWithReentrantLock {

    private final Map<String, BigDecimal> database = new HashMap<>();
    private final Map<String, ReentrantLock> locks = new HashMap<>();

    // Create a new user
    public synchronized void createUser(String user) {
        if (database.containsKey(user)) {
            throw new IllegalArgumentException("User already exists: " + user);
        }
        database.put(user, BigDecimal.ZERO);
        locks.put(user, new ReentrantLock());
        System.out.println("User created: " + user);
    }

    // Deposit money
    public void depositAmount(String user, BigDecimal amount) {
        ReentrantLock lock = locks.get(user);
        lock.lock();
        try {
            database.put(user, database.get(user).add(amount));
            System.out.println(Thread.currentThread().getName() + " deposited " + amount + " to " + user +
                    ", new balance: " + database.get(user));
        } finally {
            lock.unlock();
        }
    }

    // Withdraw money
    public void withdrawAmount(String user, BigDecimal amount) {
        ReentrantLock lock = locks.get(user);
        lock.lock();
        try {
            BigDecimal balance = database.get(user);
            if (balance.compareTo(amount) < 0) throw new RuntimeException("Insufficient funds");
            database.put(user, balance.subtract(amount));
            System.out.println(Thread.currentThread().getName() + " withdrew " + amount + " from " + user +
                    ", new balance: " + database.get(user));
        } finally {
            lock.unlock();
        }
    }

    // Transfer money between two users with deadlock avoidance
    public void transfer(String fromUser, String toUser, BigDecimal amount) throws InterruptedException {
        ReentrantLock lock1 = locks.get(fromUser);
        ReentrantLock lock2 = locks.get(toUser);

        boolean transferred = false;
        while (!transferred) {
            // Try acquiring the first lock
            if (lock1.tryLock(1, TimeUnit.SECONDS)) {
                try {
                    System.out.println(Thread.currentThread().getName() + " acquired lock on " + fromUser);
                    // Try acquiring the second lock
                    if (lock2.tryLock(1, TimeUnit.SECONDS)) {
                        try {
                            System.out.println(Thread.currentThread().getName() + " acquired lock on " + toUser);
                            // Perform transfer
                            BigDecimal fromBalance = database.get(fromUser);
                            if (fromBalance.compareTo(amount) < 0) {
                                System.out.println(Thread.currentThread().getName() + " insufficient funds for transfer");
                                return;
                            }
                            database.put(fromUser, fromBalance.subtract(amount));
                            database.put(toUser, database.get(toUser).add(amount));
                            System.out.println(Thread.currentThread().getName() + " transferred " + amount +
                                    " from " + fromUser + " to " + toUser);
                            transferred = true;
                        } finally {
                            lock2.unlock();
                            System.out.println(Thread.currentThread().getName() + " released lock on " + toUser);
                        }
                    } else {
                        System.out.println(Thread.currentThread().getName() + " could not acquire lock on " + toUser + ", retrying...");
                    }
                } finally {
                    lock1.unlock();
                    System.out.println(Thread.currentThread().getName() + " released lock on " + fromUser);
                }
            } else {
                System.out.println(Thread.currentThread().getName() + " could not acquire lock on " + fromUser + ", retrying...");
            }

            // Wait a bit before retrying to reduce busy waiting
            Thread.sleep(500);
        }
    }

    public BigDecimal getUserBalance(String user) {
        return database.get(user);
    }
}
