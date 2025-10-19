package com.project.bank_system.service;

import java.math.BigDecimal;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.locks.ReentrantLock;

public class BankWithLocks {

    private final Map<String, BigDecimal> database = new HashMap<>();
    private final Map<String, ReentrantLock> locks = new HashMap<>();

    public void createUser(String user) {
        synchronized (this) {
            if (database.containsKey(user)) throw new IllegalArgumentException("User exists");
            database.put(user, BigDecimal.ZERO);
            locks.put(user, new ReentrantLock());
        }
    }

    public void transfer(String fromUser, String toUser, BigDecimal amount) throws InterruptedException {
        ReentrantLock lock1 = locks.get(fromUser);
        ReentrantLock lock2 = locks.get(toUser);

        // Acquire locks in a safe order to avoid deadlock
        boolean acquired = false;
        while (!acquired) {
            if (lock1.tryLock(1, TimeUnit.SECONDS)) {
                try {
                    if (lock2.tryLock(1, TimeUnit.SECONDS)) {
                        try {
                            // Perform transfer
                            withdrawAmount(fromUser, amount);
                            depositAmount(toUser, amount);
                            acquired = true;
                        } finally {
                            lock2.unlock();
                        }
                    }
                } finally {
                    lock1.unlock();
                }
            }
        }
    }

    public void depositAmount(String user, BigDecimal amount) {
        ReentrantLock lock = locks.get(user);
        lock.lock();
        try {
            database.put(user, database.get(user).add(amount));
        } finally {
            lock.unlock();
        }
    }

    public void withdrawAmount(String user, BigDecimal amount) {
        ReentrantLock lock = locks.get(user);
        lock.lock();
        try {
            BigDecimal balance = database.get(user);
            if (balance.compareTo(amount) < 0) throw new RuntimeException("Insufficient funds");
            database.put(user, balance.subtract(amount));
        } finally {
            lock.unlock();
        }
    }
}

