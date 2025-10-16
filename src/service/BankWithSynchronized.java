package service;

import java.math.BigDecimal;
import java.util.HashMap;
import java.util.Map;

public class BankWithSynchronized {

    private final Map<String, BigDecimal> database = new HashMap<>();

    // Create user
    public synchronized void createUser(String user) {
        if (database.containsKey(user)) throw new IllegalArgumentException("User exists: " + user);
        database.put(user, BigDecimal.valueOf(1000));
        System.out.println("User created: " + user);
    }

    // Transfer money between users (can deadlock!)
    public void transfer(String fromUser, String toUser, BigDecimal amount) {
        synchronized (fromUser.intern()) {
            System.out.println(Thread.currentThread().getName() + " acquired lock on " + fromUser);
            // Simulate some processing time
            try { Thread.sleep(1000); } catch (InterruptedException ignored) {}

            synchronized (toUser.intern()) {
                System.out.println(Thread.currentThread().getName() + " acquired lock on " + toUser);
                BigDecimal fromBalance = database.get(fromUser);
                database.put(fromUser, fromBalance.subtract(amount));
                BigDecimal toBalance = database.get(toUser);
                database.put(toUser, toBalance.add(amount));
                System.out.println(Thread.currentThread().getName() + " transferred " + amount +
                        " from " + fromUser + " to " + toUser);
            }
            System.out.println(Thread.currentThread().getName() + " released lock on " + toUser);
        }
        System.out.println(Thread.currentThread().getName() + " released lock on " + fromUser);
    }
}
