package service;

import exceptions.InsufficientFundsException;

import java.math.BigDecimal;
import java.util.HashMap;
import java.util.Map;

public class Bank {

    private final Map<String, BigDecimal> database = new HashMap<>();

    // Create a new user
    public void createUser(String user) {
        if (user == null || user.isBlank()) {
            throw new IllegalArgumentException("User name cannot be null or blank");
        }
        synchronized (user.intern()) {
            if (database.containsKey(user)) {
                throw new IllegalArgumentException("User already exists: " + user);
            }
            database.put(user, BigDecimal.ZERO);
            System.out.println("User created: " + user);
        }
    }

    // Get user balance
    public BigDecimal getUserBalance(String user) {
        if (user == null || user.isBlank()) {
            throw new IllegalArgumentException("User name cannot be null or blank");
        }
        BigDecimal balance = database.get(user);
        if (balance == null) {
            throw new IllegalArgumentException("User not found: " + user);
        }
        return balance;
    }

    // Deposit money (thread-safe with null checks)
    public void depositAmount(String user, BigDecimal amount) {
        if (user == null || user.isBlank()) {
            throw new IllegalArgumentException("User name cannot be null or blank");
        }
        if (amount == null) {
            throw new IllegalArgumentException("Deposit amount cannot be null");
        }

        synchronized (user.intern()) {
            System.out.println(Thread.currentThread().getName() + " acquiring lock for deposit on " + user);
            BigDecimal currentBalance = database.get(user);
            if (currentBalance == null) {
                throw new IllegalArgumentException("User not found: " + user);
            }
            if (amount.compareTo(BigDecimal.ZERO) <= 0) {
                throw new IllegalArgumentException("Deposit amount must be positive");
            }

            database.put(user, currentBalance.add(amount));
            System.out.println(Thread.currentThread().getName() + " deposited " + amount + " for " + user +
                    ", new balance: " + database.get(user));
            System.out.println(Thread.currentThread().getName() + " releasing lock for deposit on " + user);
        }
    }

    // Withdraw money (thread-safe with null checks)
    public void withdrawAmount(String user, BigDecimal amount) {
        if (user == null || user.isBlank()) {
            throw new IllegalArgumentException("User name cannot be null or blank");
        }
        if (amount == null) {
            throw new IllegalArgumentException("Withdrawal amount cannot be null");
        }

        synchronized (user.intern()) {
            System.out.println(Thread.currentThread().getName() + " acquiring lock for withdrawal on " + user);

            BigDecimal currentBalance = database.get(user);
            if (currentBalance == null) {
                throw new IllegalArgumentException("User not found: " + user);
            }
            if (amount.compareTo(BigDecimal.ZERO) <= 0) {
                throw new IllegalArgumentException("Withdrawal amount must be positive");
            }

            if (currentBalance.compareTo(amount) < 0) {
                BigDecimal exceededAmount = amount.subtract(currentBalance);
                System.out.println(Thread.currentThread().getName() + " cannot withdraw " + amount +
                        " from " + user + ", exceeded by " + exceededAmount);
                System.out.println(Thread.currentThread().getName() + " releasing lock for withdrawal on " + user);
                throw new InsufficientFundsException(
                        "Insufficient balance. You exceeded by: " + exceededAmount,
                        exceededAmount
                );
            }

            database.put(user, currentBalance.subtract(amount));
            System.out.println(Thread.currentThread().getName() + " withdrew " + amount + " from " + user +
                    ", new balance: " + database.get(user));
            System.out.println(Thread.currentThread().getName() + " releasing lock for withdrawal on " + user);
        }
    }
}
